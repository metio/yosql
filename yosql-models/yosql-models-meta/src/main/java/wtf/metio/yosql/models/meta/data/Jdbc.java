/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import org.immutables.value.Value;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import javax.lang.model.element.Modifier;
import java.util.List;

public final class Jdbc {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName("Jdbc")
                .setDescription("Configures JDBC related settings.")
                .addSettings(utilityPackageName())
                .addSettings(resultStateClassName())
                .addSettings(resultRowClassName())
                .addSettings(flowStateClassName())
                .addSettings(defaultConverter())
                .addSettings(rowConverters())
                .setImmutableMethods(derivedMethods())
                .build();
    }

    private static ConfigurationSetting utilityPackageName() {
        return ConfigurationSetting.builder()
                .setName("utilityPackageName")
                .setDescription("The package name for all utility classes.")
                .setType(TypicalTypes.STRING)
                .setValue("com.example.persistence.util")
                .build();
    }

    private static ConfigurationSetting resultStateClassName() {
        return ConfigurationSetting.builder()
                .setName("resultStateClassName")
                .setDescription("The class name of the result-state class")
                .setType(TypicalTypes.STRING)
                .setValue("ResultState")
                .build();
    }

    private static ConfigurationSetting resultRowClassName() {
        return ConfigurationSetting.builder()
                .setName("resultRowClassName")
                .setDescription("The class name of the result-row class")
                .setType(TypicalTypes.STRING)
                .setValue("ResultRow")
                .build();
    }

    private static ConfigurationSetting flowStateClassName() {
        return ConfigurationSetting.builder()
                .setName("flowStateClassName")
                .setDescription("The class name of the flow-state class")
                .setType(TypicalTypes.STRING)
                .setValue("FlowState")
                .build();
    }

    private static List<MethodSpec> derivedMethods() {
        return List.of(resultStateClass(), resultRowClass(), flowStateClass());
    }

    private static MethodSpec resultStateClass() {
        return MethodSpec.methodBuilder("resultStateClass")
                .addAnnotation(Value.Lazy.class)
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(ClassName.class)
                .addStatement("return $T.get($L(), $L())", ClassName.class, "utilityPackageName", "resultStateClassName")
                .build();
    }

    private static MethodSpec resultRowClass() {
        return MethodSpec.methodBuilder("resultRowClass")
                .addAnnotation(Value.Lazy.class)
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(ClassName.class)
                .addStatement("return $T.get($L(), $L())", ClassName.class, "utilityPackageName", "resultRowClassName")
                .build();
    }

    private static MethodSpec flowStateClass() {
        return MethodSpec.methodBuilder("flowStateClass")
                .addAnnotation(Value.Lazy.class)
                .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                .returns(ClassName.class)
                .addStatement("return $T.get($L(), $L())", ClassName.class, "utilityPackageName", "flowStateClassName")
                .build();
    }

    private static ConfigurationSetting defaultConverter() {
        return ConfigurationSetting.builder()
                .setName("defaultConverter")
                .setDescription("The default converter to use, if no other is specified on a query itself.")
                .setType(TypeName.get(ResultRowConverter.class))
                .setCliType(TypicalTypes.STRING)
                .setMavenType(TypicalTypes.STRING)
                .setGradleType(ClassName.bestGuess("wtf.metio.yosql.tooling.gradle.DefaultResultRowConverter"))
                .setCliValue("")
                .setMavenValue("")
                .build();
    }

    private static ConfigurationSetting rowConverters() {
        return ConfigurationSetting.builder()
                .setName("rowConverters")
                .setDescription("The converters configured by the user.")
                .setType(TypicalTypes.listOf(ResultRowConverter.class))
                .setCliType(TypicalTypes.listOf(TypicalTypes.STRING))
                .setMavenType(TypicalTypes.listOf(TypicalTypes.STRING))
                .setGradleType(TypicalTypes.gradleContainerOf(ClassName.bestGuess("wtf.metio.yosql.tooling.gradle.UserResultRowConverter")))
                .setMavenValue("")
                .setValue(CodeBlock.builder()
                        .add("$T.of()", List.class)
                        .build())
                .build();
    }

    private Jdbc() {
        // data class
    }

}
