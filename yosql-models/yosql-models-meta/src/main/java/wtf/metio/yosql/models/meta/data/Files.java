/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterSpec;
import wtf.metio.yosql.models.meta.ConfigurationExample;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static wtf.metio.yosql.internals.javapoet.TypicalTypes.*;
import static wtf.metio.yosql.internals.jdk.Strings.upperCase;

public final class Files extends AbstractConfigurationGroup {

    private static final String GROUP_NAME = Files.class.getSimpleName();
    private static final String PROJECT_BASE_DIRECTORY = "projectBaseDirectory";
    private static final String LAYOUT = "layout";

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(GROUP_NAME)
                .setDescription("Configures how files are handled.")
                .addSettings(inputBaseDirectory())
                .addSettings(outputBaseDirectory())
                .addSettings(sqlFilesSuffix())
                .addSettings(sqlFilesCharset())
                .addSettings(sqlStatementSeparator())
                .addSettings(skipLines())
                .addImmutableMethods(immutableBuilder(GROUP_NAME))
                .addImmutableMethods(immutableCopyOf(GROUP_NAME))
                .addImmutableAnnotations(immutableAnnotation())
                .addAntParameters(ParameterSpec.builder(Path.class, PROJECT_BASE_DIRECTORY, Modifier.FINAL).build())
                .addCliParameters(ParameterSpec.builder(Path.class, PROJECT_BASE_DIRECTORY, Modifier.FINAL).build())
                .addMavenParameters(ParameterSpec.builder(Path.class, PROJECT_BASE_DIRECTORY, Modifier.FINAL).build())
                .addGradleConventionParameters(ParameterSpec.builder(GRADLE_LAYOUT, LAYOUT, Modifier.FINAL).build())
                .build();
    }

    private static ConfigurationSetting inputBaseDirectory() {
        final var name = "inputBaseDirectory";
        final var description = "The input directory for the user written SQL files.";
        final var mavenValue = "src/main/yosql";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setAntInitializer(CodeBlock.of(".set$L($L.resolve($L.toPath()))\n", upperCase(name), PROJECT_BASE_DIRECTORY, name))
                .setCliInitializer(CodeBlock.of(".set$L($L.resolve($L))\n", upperCase(name), PROJECT_BASE_DIRECTORY, name))
                .setGradleInitializer(CodeBlock.of(".set$L($L().get().getAsFile().toPath())\n", upperCase(name), gradlePropertyName(name)))
                .setMavenInitializer(CodeBlock.of(".set$L($L.resolve($L))\n", upperCase(name), PROJECT_BASE_DIRECTORY, name))
                .setGradleConvention(CodeBlock.of("$L().convention($N.getProjectDirectory().dir($S))", gradlePropertyName(name), LAYOUT, mavenValue))
                .addAntFields(antField(ClassName.get(File.class), name, description, CodeBlock.of("new $T($S)", File.class, ".")))
                .addAntMethods(antSetter(ClassName.get(File.class), name, description))
                .addCliFields(picocliOption(ClassName.get(String.class), GROUP_NAME, name, description, "."))
                .addGradleMethods(gradleProperty(GRADLE_DIRECTORY, name, description, GRADLE_INPUT_DIRECTORY))
                .addImmutableMethods(immutableMethod(ClassName.get(Path.class), name, description, CodeBlock.of("$T.of($S)", Path.class, ".")))
                .addMavenFields(mavenParameter(ClassName.get(String.class), name, description, mavenValue, CodeBlock.of("$S", mavenValue)))
                .addExamples(ConfigurationExample.builder()
                        .setValue(".")
                        .setDescription("The default value of the `inputBaseDirectory` configuration option used by the Ant- and CLI-tooling is `.` - the current directory.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(mavenValue)
                        .setDescription("The default value of the `inputBaseDirectory` configuration option used by the Gradle- and Maven-tooling is `src/main/yosql` to better reflect the commonly used project structure expected by those tools.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("some/other/directory")
                        .setDescription("Changing the `inputBaseDirectory` configuration option to `some/other/directory!` configures `YoSQL` to look into the path relative directory `some/other/directory`.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("/an/absolute/path")
                        .setDescription("Changing the `inputBaseDirectory` configuration option to `/an/absolute/path!` configures `YoSQL` to look into the absolute directory path `/an/absolute/path`.")
                        .build())
                .build();
    }

    private static ConfigurationSetting outputBaseDirectory() {
        final var name = "outputBaseDirectory";
        final var description = "The output directory for the generated classes.";
        final var mavenValue = "target/generated-sources/yosql";
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setAntInitializer(CodeBlock.of(".set$L($L.resolve($L.toPath()))\n", upperCase(name), PROJECT_BASE_DIRECTORY, name))
                .setCliInitializer(CodeBlock.of(".set$L($L.resolve($L))\n", upperCase(name), PROJECT_BASE_DIRECTORY, name))
                .setGradleInitializer(CodeBlock.of(".set$L($L().get().getAsFile().toPath())\n", upperCase(name), gradlePropertyName(name)))
                .setMavenInitializer(CodeBlock.of(".set$L($L.resolve($L))\n", upperCase(name), PROJECT_BASE_DIRECTORY, name))
                .setGradleConvention(CodeBlock.of("$L().convention($N.getBuildDirectory().dir($S))", gradlePropertyName(name), LAYOUT, "generated/sources/yosql"))
                .addAntFields(antField(ClassName.get(File.class), name, description, CodeBlock.of("new $T($S)", File.class, ".")))
                .addAntMethods(antSetter(ClassName.get(File.class), name, description))
                .addCliFields(picocliOption(ClassName.get(String.class), GROUP_NAME, name, description, "."))
                .addGradleMethods(gradleProperty(GRADLE_DIRECTORY, name, description, GRADLE_OUTPUT_DIRECTORY))
                .addImmutableMethods(immutableMethod(ClassName.get(Path.class), name, description, CodeBlock.of("$T.of($S)", Path.class, ".")))
                .addMavenFields(mavenParameter(ClassName.get(String.class), name, description, mavenValue, CodeBlock.of("$S", mavenValue)))
                .addExamples(ConfigurationExample.builder()
                        .setValue(".")
                        .setDescription("The default value of the `outputBaseDirectory` configuration option is `.` - the current directory. Note that tooling may change the default output base directory to better reflect a typical project structure used with such a tool.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("some/other/directory")
                        .setDescription("Changing the `outputBaseDirectory` configuration option to `some/other/directory!` configures `YoSQL` to write into the relative directory`some/other/directory`.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("/an/absolute/path")
                        .setDescription("Changing the `outputBaseDirectory` configuration option to `/an/absolute/path!` configures `YoSQL` to write into the absolute directory path `/an/absolute/path`.")
                        .build())
                .build();
    }

    private static ConfigurationSetting skipLines() {
        final var name = "skipLines";
        final var description = "The number of lines to skip in each file (e.g. a copyright header).";
        final var value = 0;
        return setting(GROUP_NAME, name, description, value)
                .addExamples(ConfigurationExample.builder()
                        .setValue("0")
                        .setDescription("The default value of the `skipLines` configuration option is `0` - which does not skip any lines.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("0")
                        .setDescription("Changing the `skipLines` configuration option to `5` configures `YoSQL` skip the first 5 lines in each `.sql` file in encounters.")
                        .build())
                .build();
    }

    private static ConfigurationSetting sqlFilesCharset() {
        final var name = "sqlFilesCharset";
        final var description = "The charset to use while reading .sql files.";
        final var value = StandardCharsets.UTF_8;
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setAntInitializer(CodeBlock.of(".set$L($T.forName($L))\n", upperCase(name), Charset.class, name))
                .setCliInitializer(CodeBlock.of(".set$L($T.forName($L))\n", upperCase(name), Charset.class, name))
                .setGradleInitializer(CodeBlock.of(".set$L($L().get())\n", upperCase(name), gradlePropertyName(name)))
                .setMavenInitializer(CodeBlock.of(".set$L($T.forName($L))\n", upperCase(name), Charset.class, name))
                .setGradleConvention(CodeBlock.of("$L().convention($T.UTF_8)", gradlePropertyName(name), StandardCharsets.class))
                .addAntFields(antField(name, description, value.name()))
                .addAntMethods(antSetter(ClassName.get(String.class), name, description))
                .addCliFields(picocliOption(GROUP_NAME, name, description, value.name()))
                .addGradleMethods(gradleProperty(gradlePropertyOf(ClassName.get(Charset.class)), name, description))
                .addImmutableMethods(immutableMethod(ClassName.get(Charset.class), name, description, CodeBlock.of("$T.UTF_8", StandardCharsets.class)))
                .addMavenFields(mavenParameter(name, description, value.name()))
                .addExamples(ConfigurationExample.builder()
                        .setValue(value.name())
                        .setDescription("The default value of the `sqlFilesCharset` configuration option is `UTF-8` which should work on most systems.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(StandardCharsets.ISO_8859_1.name())
                        .setDescription("Changing the `sqlFilesCharset` configuration option to `ISO-8859-1` configures `YoSQL` to use the `ISO-8859-1` charset while reading your `.sql` files.")
                        .build())
                .build();
    }

    private static ConfigurationSetting sqlFilesSuffix() {
        final var name = "sqlFilesSuffix";
        final var description = "The file ending to use while searching for SQL files.";
        final var value = ".sql";
        return setting(GROUP_NAME, name, description, value)
                .addExamples(ConfigurationExample.builder()
                        .setValue(value)
                        .setDescription("The default value of the `sqlFilesSuffix` configuration option is `.sql`. It matches all files that end with `.sql`.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(".other")
                        .setDescription("Changing the `sqlFilesSuffix` configuration option to `.other` configures `YoSQL` look for files that end in `.other`.")
                        .build())
                .build();
    }

    private static ConfigurationSetting sqlStatementSeparator() {
        final var name = "sqlStatementSeparator";
        final var description = "The separator to split SQL statements inside a single .sql file.";
        final var value = ";";
        return setting(GROUP_NAME, name, description, value)
                .addExamples(ConfigurationExample.builder()
                        .setValue(value)
                        .setDescription("The default value of the `sqlStatementSeparator` configuration option is `.sql`. It matches all files that end with `.sql`.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("|")
                        .setDescription("Changing the `sqlStatementSeparator` configuration option to `|` configures `YoSQL` split `.sql` files using the `|` character.")
                        .build())
                .build();
    }

    private Files() {
        // data class
    }

}
