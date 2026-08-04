/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.internals.testing.configs.FilesConfigurations;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("DefaultRepositoryNameConfigurer")
class DefaultRepositoryNameConfigurerTest {

    private DefaultRepositoryNameConfigurer configurer;
    private Path inputBaseDirectory;
    private FilesConfiguration files;

    @BeforeEach
    void setUp() {
        files = FilesConfigurations.maven();
        inputBaseDirectory = files.inputBaseDirectory();
        configurer = new DefaultRepositoryNameConfigurer(
                LoggingObjectMother.logger(),
                files,
                RepositoriesConfiguration.builder().build());
    }

    @Test
    void repositoryName() {
        final var config = SqlConfiguration.builder().build();
        final var name = configurer.repositoryClassName(config, inputBaseDirectory.resolve("test.sql"));
        assertEquals("com.example.persistence.Repository", name);
    }

    @Test
    void repositoryNameInSubdirectory() {
        final var config = SqlConfiguration.builder().build();
        final var name = configurer.repositoryClassName(config, inputBaseDirectory.resolve("sub/test.sql"));
        assertEquals("com.example.persistence.SubRepository", name);
    }

    @Test
    void repositoryNameInSubSubdirectory() {
        final var config = SqlConfiguration.builder().build();
        final var name = configurer.repositoryClassName(config, inputBaseDirectory.resolve("foo/bar/test.sql"));
        assertEquals("com.example.persistence.foo.BarRepository", name);
    }

    @Test
    void repositoryNameWithoutParent() {
        final var config = SqlConfiguration.builder().build();
        final var name = configurer.repositoryClassName(config, Paths.get("test.sql"));
        assertEquals("com.example.persistence.Repository", name);
    }

    @Test
    void upperCaseClassName() {
        assertEquals("Test",
                DefaultRepositoryNameConfigurer.upperCaseClassName("Test"));
    }

    @Test
    void upperCaseClassNameWithLowerCase() {
        assertEquals("Test",
                DefaultRepositoryNameConfigurer.upperCaseClassName("test"));
    }

    @Test
    void upperCaseClassNameWithFullyQualifiedName() {
        assertEquals("com.example.Test",
                DefaultRepositoryNameConfigurer.upperCaseClassName("com.example.test"));
    }

    @Test
    void upperCaseClassNameWithoutClassName() {
        assertEquals("com.example.",
                DefaultRepositoryNameConfigurer.upperCaseClassName("com.example."));
    }

    @Test
    void repositoryWithNameSuffix() {
        assertEquals("TestRepository",
                configurer.repositoryWithNameSuffix("Test"));
    }

    @Test
    void repositoryWithNameSuffixUsingExistingSuffix() {
        assertEquals("TestRepository",
                configurer.repositoryWithNameSuffix("TestRepository"));
    }

    @Test
    void repositoryWithNameSuffixUsingPackage() {
        assertEquals("com.example.TestRepository",
                configurer.repositoryWithNameSuffix("com.example.TestRepository"));
    }

    @Test
    void repositoryWithNameSuffixUsingLowerCaseSuffix() {
        assertEquals("TestsuffixRepository",
                configurer.repositoryWithNameSuffix("Testsuffix"));
    }





    @Test
    void repositoryInBasePackage() {
        assertEquals("com.example.persistence.Test",
                configurer.repositoryInBasePackage("Test"));
    }

    @Test
    void repositoryInBasePackageWithSubpackage() {
        assertEquals("com.example.persistence.domain.Test",
                configurer.repositoryInBasePackage("domain.Test"));
    }

    @Test
    void repositoryInBasePackageWithOverlappingSubpackage() {
        assertEquals("com.example.persistence.domain.Test",
                configurer.repositoryInBasePackage("persistence.domain.Test"));
    }

    @Test
    void repositoryInBasePackageWithFullyQualifiedName() {
        assertEquals("com.example.persistence.YourRepository",
                configurer.repositoryInBasePackage("com.example.persistence.YourRepository"));
    }

    @Test
    void repositoryInBasePackageWithFullyQualifiedNameAndSubpackage() {
        assertEquals("com.example.persistence.tenant.TenantRepository",
                configurer.repositoryInBasePackage("com.example.persistence.tenant.TenantRepository"));
    }

    @Test
    void repositoryInBasePackageWithSubpackageRepeatingABaseSegment() {
        assertEquals("com.example.persistence.example.Test",
                configurer.repositoryInBasePackage("example.Test"));
    }

    @Test
    void repositoryInBasePackageWithUnrelatedPackage() {
        assertEquals("com.example.persistence.org.other.Test",
                configurer.repositoryInBasePackage("org.other.Test"));
    }

    @Test
    void repositoryInBasePackageWithEmptyBasePackage() {
        configurer = new DefaultRepositoryNameConfigurer(
                LoggingObjectMother.logger(),
                files,
                RepositoriesConfiguration.builder()
                        .setBasePackageName("")
                        .build());

        assertEquals("persistence.domain.Test",
                configurer.repositoryInBasePackage("persistence.domain.Test"));
    }

    @Test
    void repositoryInBasePackageWithEmptyBasePackageWithSimpleClass() {
        configurer = new DefaultRepositoryNameConfigurer(
                LoggingObjectMother.logger(),
                files,
                RepositoriesConfiguration.builder()
                        .setBasePackageName("")
                        .build());

        assertEquals("Test",
                configurer.repositoryInBasePackage("Test"));
    }

    @Test
    void extractRawRepositoryName() {
        assertEquals("Repository",
                configurer.extractRawRepositoryName(Paths.get("test.sql")));
    }

    @Test
    void extractRawRepositoryNameInDirectory() {
        assertEquals("foo",
                configurer.extractRawRepositoryName(Paths.get("foo/test.sql")));
    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    void extractRawRepositoryNameInSubDirectory() {
        assertEquals("foo/bar",
                configurer.extractRawRepositoryName(Paths.get("foo/bar/test.sql")));
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void extractRawRepositoryNameInSubDirectoryWindows() {
        assertEquals("foo\\bar",
                configurer.extractRawRepositoryName(Paths.get("foo/bar/test.sql")));
    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    void extractRawRepositoryNameInSubSubDirectory() {
        assertEquals("foo/bar/baz",
                configurer.extractRawRepositoryName(Paths.get("foo/bar/baz/test.sql")));
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void extractRawRepositoryNameInSubSubDirectoryWindows() {
        assertEquals("foo\\bar\\baz",
                configurer.extractRawRepositoryName(Paths.get("foo/bar/baz/test.sql")));
    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    void dottedRepositoryName() {
        assertEquals("foo.bar.baz",
                configurer.dottedRepositoryName("foo/bar/baz"));
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void dottedRepositoryNameWindows() {
        assertEquals("foo.bar.baz",
                configurer.dottedRepositoryName("foo\\bar\\baz"));
    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    void dottedRepositoryNameUpperCase() {
        assertEquals("Foo.Bar.Baz",
                configurer.dottedRepositoryName("Foo/Bar/Baz"));
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void dottedRepositoryNameUpperCaseWindows() {
        assertEquals("Foo.Bar.Baz",
                configurer.dottedRepositoryName("Foo\\Bar\\Baz"));
    }

    @Test
    void configureName() {
        final var config = SqlConfiguration.builder().build();
        final var altered = configurer.configureNames(config,
                inputBaseDirectory.resolve("test.sql"));
        assertEquals("com.example.persistence.Repository",
                altered.repository().orElseThrow());
    }


    @Test
    @DisplayName("an interface is the repository without the suffix that made it a repository")
    void shouldNameTheInterfaceAfterTheRepository() {
        configurer = new DefaultRepositoryNameConfigurer(
                LoggingObjectMother.logger(), files, RepositoriesConfiguration.builder().build());

        assertAll(
                () -> assertEquals("com.example.persistence.Tenant",
                        configurer.interfaceName("com.example.persistence.TenantRepository")),
                () -> assertEquals("com.example.persistence.Order",
                        configurer.interfaceName("com.example.persistence.OrderRepository")));
    }

    @Test
    @DisplayName("a repository not named after the suffix keeps the two apart with an I")
    void shouldPrefixWhenThereIsNothingToStrip() {
        configurer = new DefaultRepositoryNameConfigurer(
                LoggingObjectMother.logger(), files, RepositoriesConfiguration.builder().build());

        assertAll(
                () -> assertEquals("com.example.persistence.ITenants",
                        configurer.interfaceName("com.example.persistence.Tenants")),
                () -> assertEquals("com.example.persistence.IRepository",
                        configurer.interfaceName("com.example.persistence.Repository")));
    }

}
