/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.AnnotationSpec;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.meta.ConfigurationExample;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;

public final class Files {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(Files.class.getSimpleName())
                .setDescription("Configures how files are handled.")
                .addSettings(inputBaseDirectory())
                .addSettings(outputBaseDirectory())
                .addSettings(sqlFilesSuffix())
                .addSettings(sqlFilesCharset())
                .addSettings(sqlStatementSeparator())
                .addSettings(skipLines())
                .build();
    }

    private static ConfigurationSetting inputBaseDirectory() {
        return ConfigurationSetting.builder()
                .setName("inputBaseDirectory")
                .setDescription("The input directory for the user written SQL files.")
                .setType(TypicalTypes.PATH)
                .setCliType(TypicalTypes.STRING)
                .setMavenType(TypicalTypes.STRING)
                .setValue(Paths.get(".")) // TODO: remove?
                .setCliValue(".")
                .setGradleValue("src/main/yosql")
                .setMavenValue("src/main/yosql")
                .setGradleAnnotations(List.of(AnnotationSpec.builder(TypicalTypes.GRADLE_INPUT_DIRECTORY).build()))
                .addExamples(ConfigurationExample.builder()
                        .setValue(".")
                        .setDescription("The default value of the `inputBaseDirectory` configuration option is `.` - the current directory. Note that tooling may change the default input base directory to better reflect a typical project structure used with such a tool.")
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
        return ConfigurationSetting.builder()
                .setName("outputBaseDirectory")
                .setDescription("The output directory for the generated classes.")
                .setType(TypicalTypes.PATH)
                .setCliType(TypicalTypes.STRING)
                .setGradleType(TypicalTypes.PATH)
                .setMavenType(TypicalTypes.STRING)
                .setValue(Paths.get(".")) // TODO: remove?
                .setCliValue(".")
                .setGradleValue("generated/sources/yosql")
                .setMavenValue("target/generated-sources/yosql")
                .setGradleAnnotations(List.of(AnnotationSpec.builder(TypicalTypes.GRADLE_OUTPUT_DIRECTORY).build()))
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
        return ConfigurationSetting.builder()
                .setName("skipLines")
                .setDescription("The number of lines to skip in each file (e.g. a copyright header).")
                .setType(TypicalTypes.INTEGER)
                .setValue(0)
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
        return ConfigurationSetting.builder()
                .setName("sqlFilesCharset")
                .setDescription("The charset to use while reading .sql files.")
                .setType(TypicalTypes.CHARSET)
                .setCliType(TypicalTypes.STRING)
                .setMavenType(TypicalTypes.STRING)
                .setValue(StandardCharsets.UTF_8)
                .setCliValue(StandardCharsets.UTF_8.name())
                .setMavenValue(StandardCharsets.UTF_8.name())
                .addExamples(ConfigurationExample.builder()
                        .setValue("UTF-8")
                        .setDescription("The default value of the `sqlFilesCharset` configuration option is `UTF-8` which should work on most systems.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("ISO-8859-1")
                        .setDescription("Changing the `sqlFilesCharset` configuration option to `ISO-8859-1` configures `YoSQL` to use the `ISO-8859-1` charset while reading your `.sql` files.")
                        .build())
                .build();
    }

    private static ConfigurationSetting sqlFilesSuffix() {
        return ConfigurationSetting.builder()
                .setName("sqlFilesSuffix")
                .setDescription("The file ending to use while searching for SQL files.")
                .setType(TypicalTypes.STRING)
                .setValue(".sql")
                .addExamples(ConfigurationExample.builder()
                        .setValue(".sql")
                        .setDescription("The default value of the `sqlFilesSuffix` configuration option is `.sql`. It matches all files that end with `.sql`.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(".other")
                        .setDescription("Changing the `sqlFilesSuffix` configuration option to `.other` configures `YoSQL` look for files that end in `.other`.")
                        .build())
                .build();
    }

    private static ConfigurationSetting sqlStatementSeparator() {
        return ConfigurationSetting.builder()
                .setName("sqlStatementSeparator")
                .setDescription("The separator to split SQL statements inside a single .sql file.")
                .setType(TypicalTypes.STRING)
                .setValue(";")
                .addExamples(ConfigurationExample.builder()
                        .setValue(";")
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
