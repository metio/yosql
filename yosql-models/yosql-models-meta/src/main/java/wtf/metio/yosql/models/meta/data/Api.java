/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.models.constants.api.AnnotationApis;
import wtf.metio.yosql.models.constants.api.LoggingApis;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.meta.ConfigurationExample;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

public final class Api {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(Api.class.getSimpleName())
                .setDescription("Configures which external APIs should be used.")
                .addSettings(daoApi())
                .addSettings(loggingApi())
                .addSettings(annotationApi())
                .build();
    }

    private static ConfigurationSetting daoApi() {
        return ConfigurationSetting.builder()
                .setName("daoApi") // TODO: rename to persistenceApi
                .setDescription("The persistence API to use.")
                .setExplanation("`YoSQL` supports multiple persistence APIs to interact with a database. We recommend that you pick the one that is already available in your project. In case you are starting fresh, `YoSQL` will default to the JDBC implementation because it requires no external dependencies and thus your project should be able to compile the generated code just fine. Some `YoSQL` tooling like the Maven plugin might auto-detect certain settings in your project to make your life easier, however you are always in full control and can change very aspect of the generated code.")
                .setType(TypeName.get(PersistenceApis.class))
                .setValue(PersistenceApis.JDBC)
                .addExamples(ConfigurationExample.builder()
                        .setValue(PersistenceApis.JDBC.name())
                        .setDescription("The `javax.sql` based implementation of `YoSQL` to access your database. It does not require any dependencies outside from standard JDK classes exposed by the [JDBC](https://en.wikipedia.org/wiki/Java_Database_Connectivity) API.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(PersistenceApis.R2DBC.name())
                        .setDescription("The `R2DBC` based implementation.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(PersistenceApis.SPRING_JDBC.name())
                        .setDescription("The `spring-jdbc` based implementation. It uses the `JdbcTemplate` or `NamedParameterJdbcTemplate` class to execute SQL statements and map result to your domain objects.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(PersistenceApis.JOOQ.name())
                        .setDescription("The `jOOQ` based implementation. It uses the `DSLContext` class to execute SQL statements and map results to your domain objects.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(PersistenceApis.JPA.name())
                        .setDescription("The `JPA` based implementation. It uses the `EntityManager` class to execute SQL statements and map results to your domain objects.")
                        .build())
                .build();
    }

    private static ConfigurationSetting loggingApi() {
        return ConfigurationSetting.builder()
                .setName("loggingApi")
                .setDescription("The logging API to use.")
                .setType(TypeName.get(LoggingApis.class))
                .setValue(LoggingApis.NONE)
                .addExamples(ConfigurationExample.builder()
                        .setValue(LoggingApis.NONE.name())
                        .setDescription("The default `no-op` implementation for a logging generator. It won't generate any logging statements in your generated code.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(LoggingApis.JUL.name())
                        .setDescription("The `java.util.logging` based implementation for a logging generator. The generated code does not require any external non-JDK classes. All loggers use the [basePackageName](/configuration/repositories/basepackagename/) as their base name.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(LoggingApis.LOG4J.name())
                        .setDescription("The [log4j](https://logging.apache.org/log4j/2.x/) based implementation for a logging generator. All loggers use the [basePackageName](/configuration/repositories/basepackagename/) as their base name.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(LoggingApis.SLF4J.name())
                        .setDescription("The [slf4j](http://www.slf4j.org/) based implementation for a logging generator. All loggers use the [basePackageName](/configuration/repositories/basepackagename/) as their base name.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(LoggingApis.TINYLOG.name())
                        .setDescription("The [Tinylog](https://tinylog.org/v2/) based implementation for a logging generator.")
                        .build())
                .build();
    }

    private static ConfigurationSetting annotationApi() {
        return ConfigurationSetting.builder()
                .setName("annotationApi")
                .setDescription("The annotation API to use.")
                .setType(TypeName.get(AnnotationApis.class))
                .setValue(AnnotationApis.PROCESSING_API)
                .addExamples(ConfigurationExample.builder()
                        .setValue("PROCESSING_API")
                        .setDescription("The default value of the `annotationApi` configuration option is `PROCESSING_API`. Setting the option to `PROCESSING_API` therefore produces the same code generated as the default configuration.")
                        .setResult("""
                                package com.example.persistence;

                                import javax.annotation.processing.Generated;

                                @Generated(
                                    value = "YoSQL",
                                    date = "<current_timestamp>",
                                    comments = "DO NOT MODIFY - automatically generated by YoSQL"
                                )
                                public class SomeRepository {

                                    @Generated(
                                        value = "YoSQL",
                                        date = "<current_timestamp>",
                                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                                    )
                                    private Object someField;

                                    @Generated(
                                        value = "YoSQL",
                                        date = "<current_timestamp>",
                                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                                    )
                                    public void someMethod() {
                                        // ... some code
                                    }

                                    // ... rest of generated code

                                }""")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("ANNOTATION_API")
                        .setDescription("Changing the `annotationApi` configuration option to `ANNOTATION_API` uses the annotation API instead.")
                        .setResult("""
                                package com.example.persistence;

                                import javax.annotation.Generated;

                                @Generated(
                                    value = "YoSQL",
                                    date = "<current_timestamp>",
                                    comments = "DO NOT MODIFY - automatically generated by YoSQL"
                                )
                                public class SomeRepository {

                                    @Generated(
                                          value = "YoSQL",
                                          date = "<current_timestamp>",
                                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                                    )
                                    private Object someField;

                                    @Generated(
                                        value = "YoSQL",
                                        date = "<current_timestamp>",
                                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                                    )
                                    public void someMethod() {
                                      // ... some code
                                    }

                                    // ... rest of generated code (same as above)

                                }""")
                        .build())
                .build();
    }

    private Api() {
        // data class
    }

}
