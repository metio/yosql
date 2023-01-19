/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
                RepositoriesConfiguration.builder()
                        .setRepositoryNamePrefix("Prefix")
                        .setRepositoryNameSuffix("Suffix")
                        .setRepositoryInterfacePrefix("Inter")
                        .setRepositoryInterfaceSuffix("Face")
                        .build());
    }

    @Test
    void repositoryName() {
        final var config = SqlConfiguration.builder().build();
        final var name = configurer.repositoryClassName(config, inputBaseDirectory.resolve("test.sql"));
        assertEquals("com.example.persistence.PrefixSuffix", name);
    }

    @Test
    void repositoryNameInSubdirectory() {
        final var config = SqlConfiguration.builder().build();
        final var name = configurer.repositoryClassName(config, inputBaseDirectory.resolve("sub/test.sql"));
        assertEquals("com.example.persistence.PrefixSubSuffix", name);
    }

    @Test
    void repositoryNameInSubSubdirectory() {
        final var config = SqlConfiguration.builder().build();
        final var name = configurer.repositoryClassName(config, inputBaseDirectory.resolve("foo/bar/test.sql"));
        assertEquals("com.example.persistence.foo.PrefixBarSuffix", name);
    }

    @Test
    void repositoryNameWithoutParent() {
        final var config = SqlConfiguration.builder().build();
        final var name = configurer.repositoryClassName(config, Paths.get("test.sql"));
        assertEquals("com.example.persistence.PrefixSuffix", name);
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
        assertEquals("TestSuffix",
                configurer.repositoryWithNameSuffix("Test"));
    }

    @Test
    void repositoryWithNameSuffixUsingExistingSuffix() {
        assertEquals("TestSuffix",
                configurer.repositoryWithNameSuffix("TestSuffix"));
    }

    @Test
    void repositoryWithNameSuffixUsingPackage() {
        assertEquals("com.example.TestSuffix",
                configurer.repositoryWithNameSuffix("com.example.TestSuffix"));
    }

    @Test
    void repositoryWithNameSuffixUsingLowerCaseSuffix() {
        assertEquals("TestsuffixSuffix",
                configurer.repositoryWithNameSuffix("Testsuffix"));
    }

    @Test
    void repositoryWithNamePrefix() {
        assertEquals("PrefixTest",
                configurer.repositoryClassWithNamePrefix("Test"));
    }

    @Test
    void repositoryWithNamePrefixUsingExistingPrefix() {
        assertEquals("PrefixRepository",
                configurer.repositoryClassWithNamePrefix("PrefixRepository"));
    }

    @Test
    void repositoryWithNamePrefixUsingPackage() {
        assertEquals("com.example.PrefixRepository",
                configurer.repositoryClassWithNamePrefix("com.example.PrefixRepository"));
    }

    @Test
    void repositoryWithNamePrefixUsingLowerCasePrefix() {
        assertEquals("PrefixprefixTest",
                configurer.repositoryClassWithNamePrefix("prefixTest"));
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
    void repositoryInBasePackageWithEmptyBasePackage() {
        configurer = new DefaultRepositoryNameConfigurer(
                LoggingObjectMother.logger(),
                files,
                RepositoriesConfiguration.builder()
                        .setRepositoryNamePrefix("Prefix")
                        .setRepositoryNameSuffix("Suffix")
                        .setRepositoryInterfacePrefix("Inter")
                        .setRepositoryInterfaceSuffix("Face")
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
                        .setRepositoryNamePrefix("Prefix")
                        .setRepositoryNameSuffix("Suffix")
                        .setRepositoryInterfacePrefix("Inter")
                        .setRepositoryInterfaceSuffix("Face")
                        .setBasePackageName("")
                        .build());

        assertEquals("Test",
                configurer.repositoryInBasePackage("Test"));
    }

    @Test
    void extractRawRepositoryName() {
        assertEquals("PrefixSuffix",
                configurer.extractRawRepositoryName(Paths.get("test.sql")));
    }

    @Test
    void extractRawRepositoryNameInDirectory() {
        assertEquals("foo",
                configurer.extractRawRepositoryName(Paths.get("foo/test.sql")));
    }

    @Test
    void extractRawRepositoryNameInSubDirectory() {
        assertEquals("foo/bar",
                configurer.extractRawRepositoryName(Paths.get("foo/bar/test.sql")));
    }

    @Test
    void extractRawRepositoryNameInSubSubDirectory() {
        assertEquals("foo/bar/baz",
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
        assertEquals("com.example.persistence.PrefixSuffix",
                altered.repository().orElseThrow());
    }

    @Test
    void shouldHandleConfiguredAffixes() {
        assertAll(
                () -> assertEquals("com.example.prefix.suffix.InterRepositoryFace",
                        configurer.interfaceName("com.example.prefix.suffix.PrefixRepositorySuffix")),
                () -> assertEquals("com.example.prefix.suffix.InterRepositoryFace",
                        configurer.interfaceName("com.example.prefix.suffix.RepositorySuffix")),
                () -> assertEquals("com.example.prefix.suffix.InterRepositoryFace",
                        configurer.interfaceName("com.example.prefix.suffix.PrefixRepository")),
                () -> assertEquals("com.example.prefix.suffix.InterRepositoryFace",
                        configurer.interfaceName("com.example.prefix.suffix.Repository"))
        );
    }

    @Test
    void shouldHandleEmptyNameAffixes() {
        final var config = RepositoriesConfiguration.builder()
                .setRepositoryNamePrefix("")
                .setRepositoryNameSuffix("")
                .setRepositoryInterfacePrefix("Inter")
                .setRepositoryInterfaceSuffix("Face")
                .build();
        configurer = new DefaultRepositoryNameConfigurer(
                LoggingObjectMother.logger(),
                files,
                config);

        assertAll(
                () -> assertEquals("com.example.prefix.suffix.InterPrefixRepositorySuffixFace",
                        configurer.interfaceName("com.example.prefix.suffix.PrefixRepositorySuffix")),
                () -> assertEquals("com.example.prefix.suffix.InterRepositorySuffixFace",
                        configurer.interfaceName("com.example.prefix.suffix.RepositorySuffix")),
                () -> assertEquals("com.example.prefix.suffix.InterPrefixRepositoryFace",
                        configurer.interfaceName("com.example.prefix.suffix.PrefixRepository")),
                () -> assertEquals("com.example.prefix.suffix.InterRepositoryFace",
                        configurer.interfaceName("com.example.prefix.suffix.Repository")),
                () -> assertEquals("com.example.prefix.suffix.InterRepositoryFace",
                        configurer.interfaceName("com.example.prefix.suffix.InterRepository")),
                () -> assertEquals("com.example.prefix.suffix.IInterRepositoryFace",
                        configurer.interfaceName("com.example.prefix.suffix.InterRepositoryFace"))
        );
    }

    @Test
    void shouldHandleConfiguredEmptyInterfaceAffixes() {
        final var config = RepositoriesConfiguration.builder()
                .setRepositoryNamePrefix("Prefix")
                .setRepositoryNameSuffix("Suffix")
                .setRepositoryInterfacePrefix("")
                .setRepositoryInterfaceSuffix("")
                .build();
        configurer = new DefaultRepositoryNameConfigurer(
                LoggingObjectMother.logger(),
                files,
                config);

        assertAll(
                () -> assertEquals("com.example.prefix.suffix.Repository",
                        configurer.interfaceName("com.example.prefix.suffix.PrefixRepositorySuffix")),
                () -> assertEquals("com.example.prefix.suffix.Repository",
                        configurer.interfaceName("com.example.prefix.suffix.RepositorySuffix")),
                () -> assertEquals("com.example.prefix.suffix.Repository",
                        configurer.interfaceName("com.example.prefix.suffix.PrefixRepository")),
                () -> assertEquals("com.example.prefix.suffix.IRepository",
                        configurer.interfaceName("com.example.prefix.suffix.Repository"))
        );
    }

    @Test
    void shouldHandleConfiguredEmptyAffixes() {
        final var config = RepositoriesConfiguration.builder()
                .setRepositoryNamePrefix("")
                .setRepositoryNameSuffix("")
                .setRepositoryInterfacePrefix("")
                .setRepositoryInterfaceSuffix("")
                .build();
        configurer = new DefaultRepositoryNameConfigurer(
                LoggingObjectMother.logger(),
                files,
                config);

        assertAll(
                () -> assertEquals("com.example.prefix.suffix.IRepository",
                        configurer.interfaceName("com.example.prefix.suffix.Repository")),
                () -> assertEquals("com.example.prefix.suffix.IIslands",
                        configurer.interfaceName("com.example.prefix.suffix.Islands"))
        );
    }

}
