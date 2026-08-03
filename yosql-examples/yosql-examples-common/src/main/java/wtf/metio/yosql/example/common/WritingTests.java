/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.example.common;

import org.immutables.value.Value;

import java.util.function.BiFunction;
import java.util.function.ToIntBiFunction;

@Value.Immutable
public interface WritingTests {

    System.Logger LOG = System.getLogger(WritingTests.class.getName());

    static ImmutableWritingTests.InsertCompanyBuildStage builder() {
        return ImmutableWritingTests.builder();
    }

    ToIntBiFunction<Integer, String> insertCompany();

    ToIntBiFunction<Integer, String> insertPerson();

    BiFunction<int[], String[], int[]> insertCompanyBatch();

    BiFunction<int[], String[], int[]> insertPersonBatch();

    ToIntBiFunction<String, Integer> updateUser();

    ToIntBiFunction<Integer, String> insertUser();

    BiFunction<int[], String[], int[]> insertUserBatch();

    ToIntBiFunction<Integer, String> insertItem();

    BiFunction<int[], String[], int[]> insertItemBatch();

    @Value.Lazy
    default void runWritingTests() {
        try {
            // A write returns the rows it changed, so the update count is the statement reporting
            // that it did what it was asked. Discarding it is how a write that silently matched
            // nothing goes unnoticed.
            Verify.equal(1, insertCompany().applyAsInt(1, "one"), "insert company one");
            Verify.equal(1, insertCompany().applyAsInt(2, "two"), "insert company two");
            Verify.equal(1, insertCompany().applyAsInt(3, "three"), "insert company three");

            Verify.equal(1, insertPerson().applyAsInt(1, "eve"), "insert person eve");
            Verify.equal(1, insertPerson().applyAsInt(2, "adam"), "insert person adam");
            Verify.equal(1, insertPerson().applyAsInt(3, "bob"), "insert person bob");

            Verify.equal(1, insertUser().applyAsInt(-1, "special user"), "insert special user");
            Verify.equal(1, insertUser().applyAsInt(1, "admin"), "insert admin user");
            Verify.equal(1, insertUser().applyAsInt(2, "not-an-admin"), "insert non-admin user");
            Verify.equal(1, insertUser().applyAsInt(3, "regular-user"), "insert regular user");

            Verify.equal(1, insertItem().applyAsInt(1, "iPhone 47 eXtreme"), "insert first item");
            Verify.equal(1, insertItem().applyAsInt(2, "Android 49"), "insert second item");
            Verify.equal(1, insertItem().applyAsInt(3, "GenericPhone 38"), "insert third item");

            // A batch answers with one count per element, which is the only way to see that every
            // element of the array reached the database rather than just the first.
            Verify.equal(3, insertCompanyBatch()
                    .apply(new int[]{4, 5, 6}, new String[]{"three", "five", "six"}).length,
                    "company batch counts");
            Verify.equal(3, insertPersonBatch()
                    .apply(new int[]{4, 5, 6}, new String[]{"alice", "frank", "joe"}).length,
                    "person batch counts");
        } catch (final RuntimeException exception) {
            LOG.log(System.Logger.Level.ERROR, "Error while running WRITING tests", exception);
            System.exit(1);
        }
    }

}
