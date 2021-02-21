package wtf.metio.yosql.internals.meta.model.data;

import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.internals.meta.model.ConfigurationGroup;
import wtf.metio.yosql.internals.meta.model.ConfigurationSetting;

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
                .setDescription("The input directory for the user written SQL files (default: <strong>src/main/yosql</strong>).")
                .setType(TypeName.get(String.class))
                .setDefaultValue("src/main/yosql")
                .build();
    }

    private static ConfigurationSetting outputBaseDirectory() {
        return ConfigurationSetting.builder()
                .setName("outputBaseDirectory")
                .setDescription("The output directory for the generated classes (default: <strong>generated-sources/yosql</strong>).")
                .setType(TypeName.get(String.class))
                .setDefaultValue("generated-sources/yosql")
                .build();
    }

    private static ConfigurationSetting sqlFilesSuffix() {
        return ConfigurationSetting.builder()
                .setName("sqlFilesSuffix")
                .setDescription("The file ending to use while searching for SQL files (default: <strong>.sql</strong>).")
                .setType(TypeName.get(String.class))
                .setDefaultValue(".sql")
                .build();
    }

    private static ConfigurationSetting sqlFilesCharset() {
        return ConfigurationSetting.builder()
                .setName("sqlFilesCharset")
                .setDescription("The charset to use while reading .sql files (default: <strong>UTF-8</strong>).")
                .setType(TypeName.get(String.class))
                .setDefaultValue("UTF-8")
                .build();
    }

    private static ConfigurationSetting sqlStatementSeparator() {
        return ConfigurationSetting.builder()
                .setName("sqlStatementSeparator")
                .setDescription("The separator to split SQL statements inside a single .sql file (default: <strong>\";\"</strong>).")
                .setType(TypeName.get(String.class))
                .setDefaultValue(";")
                .build();
    }

    private static ConfigurationSetting skipLines() {
        return ConfigurationSetting.builder()
                .setName("skipLines")
                .setDescription("The number of lines to skip in each file (e.g. a copyright header) (default: <strong>0</strong>).")
                .setType(TypeName.get(Integer.class))
                .setDefaultValue(0)
                .build();
    }

    private Files() {
        // data class
    }

}
