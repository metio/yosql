/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.palantir.javapoet.ClassName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.blocks.BlocksObjectMother;
import wtf.metio.yosql.codegen.exceptions.CollidingResultColumnsException;
import wtf.metio.yosql.codegen.exceptions.ConflictingColumnTypeException;
import wtf.metio.yosql.codegen.exceptions.UnusableComponentNameException;
import wtf.metio.yosql.codegen.schema.Schemas;
import wtf.metio.yosql.codegen.schema.Schemas.VendorStatement;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("SchemaRecords")
class SchemaRecordsTest {

    private static final String DDL = """
            create table tenant (
                id         uuid not null primary key,
                account_id uuid not null,
                slug       varchar(64) not null,
                nickname   varchar(32),
                created_at timestamp with time zone not null
            )""";

    /**
     * A second table declaring an {@code id} of its own, which is what makes a star over a join
     * ambiguous for a record.
     */
    private static final String ACCOUNT_DDL = """
            create table account (
                id   uuid not null primary key,
                name varchar(64) not null
            )""";

    private static final ClassName TENANT = ClassName.get("com.example.domain", "Tenant");

    private static SchemaRecords records(final String... ddl) {
        return new SchemaRecords(
                Schemas.of(List.of(ddl).stream()
                        .map(sql -> new VendorStatement(Optional.empty(), sql)).toList()),
                BlocksObjectMother.annotationGenerator());
    }

    private static SqlStatement statement(final String sql) {
        return SqlStatement.builder()
                .setSourcePath(Path.of("src", "main", "yosql", "tenant", "findTenant.sql"))
                .setConfiguration(SqlConfiguration.builder().setName("findTenant").build())
                .setRawStatement(sql)
                .build();
    }

    @Nested
    @DisplayName("the shape it works out")
    class Shape {

        @Test
        @DisplayName("one component per selected column, in the order they are selected")
        void shouldFollowTheSelectList() {
            final var shape = records(DDL)
                    .shapeOf(TENANT, statement("select id, slug, created_at from tenant"))
                    .orElseThrow();

            assertAll(
                    () -> assertEquals(3, shape.components().size()),
                    () -> assertEquals("id", shape.components().get(0).name()),
                    () -> assertEquals("java.util.UUID", shape.components().get(0).type().toString()),
                    () -> assertEquals("slug", shape.components().get(1).name()),
                    () -> assertEquals("java.time.Instant", shape.components().get(2).type().toString()));
        }

        @Test
        @DisplayName("a snake_case column becomes a camelCase component")
        void shouldRenameColumns() {
            final var shape = records(DDL)
                    .shapeOf(TENANT, statement("select account_id, created_at from tenant"))
                    .orElseThrow();

            assertAll(
                    () -> assertEquals("accountId", shape.components().get(0).name()),
                    () -> assertEquals("createdAt", shape.components().get(1).name()));
        }

        @Test
        @DisplayName("a nullable column becomes a type that can hold a null")
        void shouldBoxNullableColumns() {
            final var shape = records(DDL)
                    .shapeOf(TENANT, statement("select id, nickname from tenant"))
                    .orElseThrow();

            assertEquals("java.lang.String", shape.components().get(1).type().toString());
        }

        @Test
        @DisplayName("an alias names the component, and the column behind it gives the type")
        void shouldFollowAliases() {
            final var shape = records(DDL)
                    .shapeOf(TENANT, statement("select created_at as at from tenant"))
                    .orElseThrow();

            assertAll(
                    () -> assertEquals("at", shape.components().get(0).name()),
                    () -> assertEquals("java.time.Instant", shape.components().get(0).type().toString()));
        }

        @Test
        @DisplayName("a qualified column reads the table it names, not the first one in scope")
        void shouldFollowTheQualifier() {
            final var shape = records(
                    "create table orders (id bigint not null primary key, customer_id uuid not null)",
                    "create table customers (id uuid not null primary key, name varchar(64) not null)")
                    .shapeOf(TENANT, statement("""
                            select o.id, c.id as customer_id
                            from orders o join customers c on c.id = o.customer_id"""))
                    .orElseThrow();

            assertAll(
                    () -> assertEquals("id", shape.components().get(0).name()),
                    () -> assertEquals("long", shape.components().get(0).type().toString(),
                            "o.id is the orders id"),
                    () -> assertEquals("customerId", shape.components().get(1).name()),
                    () -> assertEquals("java.util.UUID", shape.components().get(1).type().toString(),
                            "c.id is the customers id, whatever orders declares under that name"));
        }

        @Test
        @DisplayName("a star selects everything the table declares")
        void shouldExpandAStar() {
            final var shape = records(DDL).shapeOf(TENANT, statement("select * from tenant")).orElseThrow();

            assertEquals(5, shape.components().size());
        }

    }

    @Nested
    @DisplayName("when it will not write one")
    class Refuses {

        @Test
        @DisplayName("a column the catalog does not describe")
        void shouldRefuseUnknownColumns() {
            assertTrue(records(DDL).shapeOf(TENANT, statement("select whatever from some_view")).isEmpty());
        }

        @Test
        @DisplayName("an expression no single column produced")
        void shouldRefuseComputedColumns() {
            assertTrue(records(DDL)
                    .shapeOf(TENANT, statement("select count(*) as total from tenant")).isEmpty());
        }

        @Test
        @DisplayName("a subquery, which can produce anything")
        void shouldRefuseSubqueries() {
            assertTrue(records(DDL)
                    .shapeOf(TENANT, statement("select id from (select id from tenant) x")).isEmpty());
        }

        @Test
        @DisplayName("with no schema at all")
        void shouldRefuseWithoutASchema() {
            assertTrue(records().shapeOf(TENANT, statement("select id from tenant")).isEmpty());
        }

        @Test
        @DisplayName("a column whose name is a Java keyword")
        void shouldRefuseKeywordComponents() {
            final var records = records("create table places (lat double precision, long double precision)");
            final var statement = statement("select lat, long from places");

            final var thrown = assertThrows(UnusableComponentNameException.class,
                    () -> records.shapeOf(TENANT, statement));
            assertAll(
                    () -> assertTrue(thrown.getMessage().contains("findTenant"), thrown::getMessage),
                    () -> assertTrue(thrown.getMessage().contains("'long'"), thrown::getMessage));
        }

        @Test
        @DisplayName("a star over a join where both tables declare the same column")
        void shouldRefuseCollidingComponents() {
            final var records = records(DDL, ACCOUNT_DDL);
            final var statement = statement("select * from tenant join account on account.id = tenant.account_id");

            final var thrown = assertThrows(CollidingResultColumnsException.class,
                    () -> records.shapeOf(TENANT, statement));
            assertAll(
                    () -> assertTrue(thrown.getMessage().contains("findTenant"), thrown::getMessage),
                    () -> assertTrue(thrown.getMessage().contains("findTenant.sql"), thrown::getMessage),
                    () -> assertTrue(thrown.getMessage().contains("'id'"), thrown::getMessage));
        }

        @Test
        @DisplayName("but a join whose columns do not collide is written as usual")
        void shouldWriteAJoinWithoutCollisions() {
            final var shape = records(DDL, ACCOUNT_DDL)
                    .shapeOf(TENANT, statement("select tenant.slug, account.name from tenant join account "
                            + "on account.id = tenant.account_id"))
                    .orElseThrow();

            assertAll(
                    () -> assertEquals(2, shape.components().size()),
                    () -> assertEquals("slug", shape.components().get(0).name()),
                    () -> assertEquals("name", shape.components().get(1).name()));
        }

    }

    @Nested
    @DisplayName("across the databases a vendorless statement falls back to")
    class EveryVendor {

        private static SchemaRecords records(final VendorStatement... ddl) {
            return new SchemaRecords(Schemas.of(List.of(ddl)), BlocksObjectMother.annotationGenerator());
        }

        private static VendorStatement forVendor(final String vendor, final String ddl) {
            return new VendorStatement(Optional.of(vendor), ddl);
        }

        @Test
        @DisplayName("a disagreement about what a column holds is reported, not resolved by picking one")
        void shouldRefuseConflictingTypes() {
            final var records = records(
                    forVendor("postgresql", "create table tenant (id uuid not null primary key)"),
                    forVendor("mysql", "create table tenant (id varchar(36) not null primary key)"));

            final var thrown = assertThrows(ConflictingColumnTypeException.class,
                    () -> records.shapeOf(TENANT, statement("select id from tenant")));
            assertAll(
                    () -> assertTrue(thrown.getMessage().contains("java.util.UUID"), thrown::getMessage),
                    () -> assertTrue(thrown.getMessage().contains("java.lang.String"), thrown::getMessage),
                    () -> assertTrue(thrown.getMessage().contains("'id'"), thrown::getMessage));
        }

        @Test
        @DisplayName("dialect spellings that meet in Java are not a disagreement")
        void shouldAcceptTypesThatMeetInJava() {
            final var shape = records(
                    forVendor("postgresql", "create table tenant (id bigserial primary key)"),
                    forVendor("mysql", "create table tenant (id bigint auto_increment primary key)"))
                    .shapeOf(TENANT, statement("select id from tenant"))
                    .orElseThrow();

            assertEquals("long", shape.components().getFirst().type().toString());
        }

        @Test
        @DisplayName("a vendor's own type spellings are read for a statement that names no vendor")
        void shouldReadVendorSpellingsForAVendorlessStatement() {
            // The shape of a project with one database: the DDL says which one, the statements have
            // no other database to be told apart from and so say nothing.
            final var shape = records(forVendor("postgresql", """
                    create table attachment (
                        id      bigserial primary key,
                        payload bytea not null,
                        at      timestamptz not null
                    )"""))
                    .shapeOf(TENANT, statement("select id, payload, at from attachment"))
                    .orElseThrow(() -> new AssertionError(
                            "no shape at all: every column is spelled the way PostgreSQL spells it"));

            assertAll(
                    () -> assertEquals("long", shape.components().get(0).type().toString()),
                    () -> assertEquals("byte[]", shape.components().get(1).type().toString()),
                    () -> assertEquals("java.time.Instant", shape.components().get(2).type().toString()));
        }

        @Test
        @DisplayName("the answer does not depend on which vendor's DDL was read first")
        void shouldNotDependOnVendorOrder() {
            final var postgres = forVendor("postgresql", "create table tenant (id uuid not null primary key)");
            final var mysql = forVendor("mysql", "create table tenant (id varchar(36) not null primary key)");

            assertAll(
                    () -> assertThrows(ConflictingColumnTypeException.class,
                            () -> records(postgres, mysql).shapeOf(TENANT, statement("select id from tenant"))),
                    () -> assertThrows(ConflictingColumnTypeException.class,
                            () -> records(mysql, postgres).shapeOf(TENANT, statement("select id from tenant"))));
        }

    }

    @Test
    @DisplayName("the record it writes is an ordinary record")
    void shouldWriteARecord() {
        final var shape = records(DDL)
                .shapeOf(TENANT, statement("select id, nickname, created_at from tenant"))
                .orElseThrow();

        final var written = records(DDL).generateRecord(shape).getType().toString();

        assertAll(
                () -> assertTrue(written.contains("record Tenant("), written),
                () -> assertTrue(written.contains("java.util.UUID id"), written),
                () -> assertTrue(written.contains("java.lang.String nickname"), written),
                () -> assertTrue(written.contains("java.time.Instant createdAt"), written));
    }

    @Nested
    @DisplayName("why it could not")
    class WhyNot {

        private static final String DOCUMENT_DDL = """
                create table document (
                    id      uuid not null primary key,
                    payload jsonb not null
                )""";

        @Test
        @DisplayName("names the column whose type maps to nothing, and asks for the vendor")
        void shouldPointAtTheVendorForAVendorSpecificType() {
            final var why = records(DOCUMENT_DDL)
                    .whyNot(statement("select payload from document"));

            assertAll(
                    () -> assertTrue(why.contains("'document.payload'"), why),
                    () -> assertTrue(why.contains("jsonb"), why),
                    () -> assertTrue(why.contains("schema.vendor"), why));
        }

        @Test
        @DisplayName("says the vendor it looked the type up for once one is declared")
        void shouldNameTheVendorItUsed() {
            final var configured = SqlStatement.builder()
                    .setSourcePath(Path.of("src", "main", "yosql", "document", "findDocument.sql"))
                    .setConfiguration(SqlConfiguration.builder()
                            .setName("findDocument")
                            .setVendor("Oracle")
                            .build())
                    .setRawStatement("select payload from document")
                    .build();

            final var why = records(DOCUMENT_DDL).whyNot(configured);

            assertAll(
                    () -> assertTrue(why.contains("Oracle"), why),
                    () -> assertTrue(!why.contains("schema.vendor"), why));
        }

        @Test
        @DisplayName("says so plainly when nothing was read")
        void shouldSayWhenNoSchemaWasRead() {
            final var records = new SchemaRecords(Schemas.empty(),
                    BlocksObjectMother.annotationGenerator());

            final var why = records.whyNot(statement("select id from tenant"));

            assertAll(
                    () -> assertTrue(why.startsWith("no schema was read at all"), why),
                    () -> assertTrue(why.contains("schema.sqlStatementsDirectory"), why));
        }

        @Test
        @DisplayName("names the column the schema does not hold")
        void shouldNameAnUnknownColumn() {
            final var why = records(DDL).whyNot(statement("select id, missing from tenant"));

            assertTrue(why.contains("missing"), why);
        }

        @Test
        @DisplayName("says a computed expression is not a column")
        void shouldSayWhenSomethingIsNotAColumn() {
            final var why = records(DDL).whyNot(statement("select count(*) as total from tenant"));

            assertTrue(why.contains("total"), why);
        }

    }

}
