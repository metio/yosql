/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.AnnotationSpec;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
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
                .build();
    }

    private static ConfigurationSetting sqlFilesSuffix() {
        return ConfigurationSetting.builder()
                .setName("sqlFilesSuffix")
                .setDescription("The file ending to use while searching for SQL files.")
                .setType(TypicalTypes.STRING)
                .setValue(".sql")
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
                .build();
    }

    private static ConfigurationSetting sqlStatementSeparator() {
        return ConfigurationSetting.builder()
                .setName("sqlStatementSeparator")
                .setDescription("The separator to split SQL statements inside a single .sql file.")
                .setType(TypicalTypes.STRING)
                .setValue(";")
                .build();
    }

    private static ConfigurationSetting skipLines() {
        return ConfigurationSetting.builder()
                .setName("skipLines")
                .setDescription("The number of lines to skip in each file (e.g. a copyright header).")
                .setType(TypicalTypes.INTEGER)
                .setValue(0)
                .build();
    }

    private Files() {
        // data class
    }

}
