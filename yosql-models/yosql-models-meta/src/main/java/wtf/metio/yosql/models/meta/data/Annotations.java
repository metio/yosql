/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.meta.data;

import com.palantir.javapoet.*;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.configuration.Annotation;
import wtf.metio.yosql.models.configuration.AnnotationMember;
import wtf.metio.yosql.models.meta.ConfigurationExample;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import static wtf.metio.yosql.internals.javapoet.TypicalTypes.gradleContainerOf;
import static wtf.metio.yosql.internals.jdk.Strings.upperCase;

public final class Annotations extends AbstractConfigurationGroup {

    private static final String GROUP_NAME = Annotations.class.getSimpleName();
    private static final String ANNOTATION_SPEC = "AnnotationSpec";
    private static final ClassName ANT_ANNOTATION_TYPE = antType(ANNOTATION_SPEC);
    private static final ClassName GRADLE_ANNOTATION_TYPE = gradleType(ANNOTATION_SPEC);
    private static final ClassName MAVEN_ANNOTATION_TYPE = mavenType(ANNOTATION_SPEC);
    private static final String ANNOTATION_MEMBER_SPEC = "AnnotationMemberSpec";
    private static final ClassName ANT_MEMBER_TYPE = antType(ANNOTATION_MEMBER_SPEC);
    private static final ClassName GRADLE_MEMBER_TYPE = gradleType(ANNOTATION_MEMBER_SPEC);
    private static final ClassName MAVEN_MEMBER_TYPE = mavenType(ANNOTATION_MEMBER_SPEC);
    private static final String AS_ANNOTATION = "asAnnotation";
    private static final String AS_ANNOTATION_MEMBER = "asAnnotationMember";
    private static final String TYPE = "type";
    private static final String MEMBERS = "members";
    private static final String MEMBER = "member";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String CREATE_ANNOTATIONS = "createAnnotations";

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(GROUP_NAME)
                .setDescription("Configures how annotations are applied to the generated code.")
                .addSettings(repositoryAnnotations())
                .addSettings(constructorAnnotations())
                .addSettings(methodAnnotations())
                .addAntTypes(antAnnotationSpecType())
                .addAntTypes(antAnnotationMemberSpecType())
                .addAntMethods(createAntAnnotations())
                .addCliMethods(createCliAnnotations())
                .addGradleTypes(gradleAnnotationSpecType())
                .addGradleTypes(gradleAnnotationMemberSpecType())
                .addImmutableMethods(immutableBuilder(GROUP_NAME))
                .addImmutableMethods(immutableCopyOf(GROUP_NAME))
                .addImmutableAnnotations(immutableAnnotation())
                .addMavenTypes(mavenAnnotationSpecType())
                .addMavenTypes(mavenAnnotationMemberSpecType())
                .addMavenMethods(createMavenAnnotations())
                .build();
    }

    private static ConfigurationSetting repositoryAnnotations() {
        final var name = "repositoryAnnotations";
        final var description = "The additional annotations to be placed on generated repository classes.";
        return ConfigurationSetting.copyOf(listOfAnnotations(name, description))
                .withExplanation("""
                        This is how a generated repository becomes a bean. `org.springframework.stereotype.Repository`
                        makes component scanning find it; `jakarta.enterprise.context.ApplicationScoped` makes CDI
                        find it. Either way constructor injection supplies the `DataSource`, and no configuration
                        class is needed — see [Spring Boot](../../../frameworks/spring-boot/) and
                        [Quarkus](../../../frameworks/quarkus/).

                        Name annotations by their fully-qualified name; the import is written for you. The
                        annotation has to be on the compile classpath of the project being generated for, since
                        the generated code has to compile against it — but it is your dependency, not one of
                        `YoSQL`'s.""")
                .withExamples(ConfigurationExample.builder()
                        .setValue("org.springframework.stereotype.Repository")
                        .setDescription("Spring finds the repository by scanning, and injects the application's `DataSource`:")
                        .setResult("""
                                package com.example.persistence;

                                import org.springframework.stereotype.Repository;

                                @Repository
                                public final class TenantRepository {

                                    // ... rest of generated code

                                }""")
                        .build(),
                ConfigurationExample.builder()
                        .setValue("jakarta.enterprise.context.ApplicationScoped")
                        .setDescription("The same under CDI, so Quarkus and Jakarta EE inject it:")
                        .setResult("""
                                package com.example.persistence;

                                import jakarta.enterprise.context.ApplicationScoped;

                                @ApplicationScoped
                                public final class TenantRepository {

                                    // ... rest of generated code

                                }""")
                        .build());
    }

    private static ConfigurationSetting constructorAnnotations() {
        final var name = "constructorAnnotations";
        final var description = "The additional annotations to be placed on generated constructors.";
        return ConfigurationSetting.copyOf(listOfAnnotations(name, description))
                .withExplanation("""
                        A repository has one constructor, so most containers inject through it without being
                        told. This is for the ones that want to be told, or for a project whose style is to say
                        so anyway — `jakarta.inject.Inject`, `org.springframework.beans.factory.annotation.Autowired`.""")
                .withExamples(ConfigurationExample.builder()
                        .setValue("jakarta.inject.Inject")
                        .setDescription("Marks the constructor for injection explicitly:")
                        .setResult("""
                                package com.example.persistence;

                                import jakarta.inject.Inject;

                                public final class TenantRepository {

                                    @Inject
                                    public TenantRepository(final DataSource dataSource) {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build());
    }

    private static ConfigurationSetting methodAnnotations() {
        final var name = "methodAnnotations";
        final var description = "The additional annotations to be placed on generated methods.";
        return ConfigurationSetting.copyOf(listOfAnnotations(name, description))
                .withExplanation("""
                        Goes on every generated method of every repository, which limits it to annotations that
                        make sense everywhere — a metrics `@Timed`, a `@SuppressWarnings`.

                        Resist putting `@Transactional` here. It would open a transaction per statement, which is
                        the opposite of what a transaction is for; transactions belong to the service calling
                        several statements, and [transactions](../../../sql/transactions/) covers how a repository
                        joins one.

                        To annotate one statement rather than all of them, use
                        [annotations](../../sql/annotations/) in its front matter.""")
                .withExamples(ConfigurationExample.builder()
                        .setValue("io.micrometer.core.annotation.Timed")
                        .setDescription("Every statement is measured:")
                        .setResult("""
                                package com.example.persistence;

                                import io.micrometer.core.annotation.Timed;

                                public final class TenantRepository {

                                    @Timed
                                    public Optional<Tenant> findTenant(final UUID id) {
                                        // ... rest of generated code
                                    }

                                }""")
                        .build());
    }

    private static ConfigurationSetting listOfAnnotations(final String name, final String description) {
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .setAntInitializer(CodeBlock.of(".addAll$L($L($L))\n", upperCase(name), CREATE_ANNOTATIONS, name))
                .setCliInitializer(CodeBlock.of(".addAll$L($L($L))\n", upperCase(name), CREATE_ANNOTATIONS, name))
                .setGradleInitializer(CodeBlock.of(".addAll$L($L().stream().map($T::$L).toList())\n", upperCase(name), gradlePropertyName(name), GRADLE_ANNOTATION_TYPE, AS_ANNOTATION))
                .setMavenInitializer(CodeBlock.of(".addAll$L($L($L))\n", upperCase(name), CREATE_ANNOTATIONS, name))
                .addAntFields(antField(TypicalTypes.listOf(ANT_ANNOTATION_TYPE), name, description, CodeBlock.of("new $T<>()", ArrayList.class)))
                .addAntMethods(antAdder(ANT_ANNOTATION_TYPE, name, description))
                .addCliFields(picocliOption(TypicalTypes.listOf(String.class), GROUP_NAME, name, description))
                .addGradleMethods(gradleProperty(gradleContainerOf(GRADLE_ANNOTATION_TYPE), name, description))
                .addImmutableMethods(immutableMethod(TypicalTypes.listOf(Annotation.class), name, description))
                .addMavenFields(mavenParameter(TypicalTypes.listOf(MAVEN_ANNOTATION_TYPE), name, description, "", CodeBlock.of("new $T<>()", ArrayList.class)))
                .build();
    }

    /**
     * The nested element an Ant build writes an annotation as.
     *
     * <p>Ant configures a nested element the same way it configures a task: by looking for a setter
     * per attribute and an {@code addConfigured}/{@code create} method per child. A class carrying
     * only fields is instantiated and then cannot be told anything, so the element was accepted and
     * every attribute on it refused — which made the whole annotations group unusable from Ant.</p>
     */
    private static TypeSpec antAnnotationSpecType() {
        return TypeSpec.classBuilder(ANNOTATION_SPEC)
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(ClassName.get(String.class), TYPE).build())
                .addField(FieldSpec.builder(TypicalTypes.listOf(ANT_MEMBER_TYPE), MEMBERS)
                        .initializer("new $T<>()", ArrayList.class)
                        .build())
                .addMethod(antSetter(ClassName.get(String.class), TYPE,
                        "The fully qualified name of the annotation."))
                .addMethod(MethodSpec.methodBuilder("addConfigured" + upperCase(MEMBER))
                        .addJavadoc("$L", "One member of this annotation.")
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ANT_MEMBER_TYPE, MEMBER, Modifier.FINAL)
                        .addStatement("this.$L.add($L)", MEMBERS, MEMBER)
                        .build())
                .addMethod(MethodSpec.methodBuilder(AS_ANNOTATION)
                        .addModifiers(Modifier.FINAL)
                        .returns(Annotation.class)
                        .addStatement(CodeBlock.builder()
                                .add("return $T.builder()", Annotation.class)
                                .add("$>\n.set$L($L)", upperCase(TYPE), TYPE)
                                .add("\n.addAll$L($T.ofNullable($L).flatMap($T::stream).map($T::$L).toList())",
                                        upperCase(MEMBERS), Stream.class, MEMBERS, Collection.class, ANT_MEMBER_TYPE, AS_ANNOTATION_MEMBER)
                                .add("\n.build()$<")
                                .build())
                        .build())
                .build();
    }

    private static TypeSpec antAnnotationMemberSpecType() {
        return TypeSpec.classBuilder(ANNOTATION_MEMBER_SPEC)
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(ClassName.get(String.class), TYPE).initializer("$S", "java.lang.String").build())
                .addField(FieldSpec.builder(ClassName.get(String.class), KEY).build())
                .addField(FieldSpec.builder(ClassName.get(String.class), VALUE).build())
                .addMethod(antSetter(ClassName.get(String.class), TYPE, "The type of this member's value."))
                .addMethod(antSetter(ClassName.get(String.class), KEY, "The name of this member."))
                .addMethod(antSetter(ClassName.get(String.class), VALUE, "The value of this member."))
                .addMethod(MethodSpec.methodBuilder(AS_ANNOTATION_MEMBER)
                        .addModifiers(Modifier.FINAL)
                        .returns(AnnotationMember.class)
                        .addStatement(CodeBlock.builder()
                                .add("return $T.builder()", AnnotationMember.class)
                                .add("$>\n.set$L($L)", upperCase(KEY), KEY)
                                .add("\n.set$L($L)", upperCase(VALUE), VALUE)
                                .add("\n.set$L($L)", upperCase(TYPE), TYPE)
                                .add("\n.build()$<")
                                .build())
                        .build())
                .build();
    }

    private static TypeSpec mavenAnnotationSpecType() {
        return TypeSpec.classBuilder(ANNOTATION_SPEC)
                .addModifiers(Modifier.PUBLIC)
                .addField(mavenStringParameter(TYPE))
                .addField(mavenParameter(TypicalTypes.listOf(MAVEN_MEMBER_TYPE), MEMBERS))
                .addMethod(MethodSpec.methodBuilder(AS_ANNOTATION)
                        .addModifiers(Modifier.FINAL)
                        .returns(Annotation.class)
                        .addStatement(CodeBlock.builder()
                                .add("return $T.builder()", Annotation.class)
                                .add("$>\n.set$L($L)", upperCase(TYPE), TYPE)
                                .add("\n.addAll$L($T.ofNullable($L).flatMap($T::stream).map($T::$L).toList())",
                                        upperCase(MEMBERS), Stream.class, MEMBERS, Collection.class, MAVEN_MEMBER_TYPE, AS_ANNOTATION_MEMBER)
                                .add("\n.build()$<")
                                .build())
                        .build())
                .build();
    }

    private static TypeSpec mavenAnnotationMemberSpecType() {
        return TypeSpec.classBuilder(ANNOTATION_MEMBER_SPEC)
                .addModifiers(Modifier.PUBLIC)
                .addField(mavenStringParameter(TYPE, "java.lang.String"))
                .addField(mavenStringParameter(KEY))
                .addField(mavenStringParameter(VALUE))
                .addMethod(MethodSpec.methodBuilder(AS_ANNOTATION_MEMBER)
                        .addModifiers(Modifier.FINAL)
                        .returns(AnnotationMember.class)
                        .addStatement(CodeBlock.builder()
                                .add("return $T.builder()", AnnotationMember.class)
                                .add("$>\n.set$L($L)", upperCase(KEY), KEY)
                                .add("\n.set$L($L)", upperCase(VALUE), VALUE)
                                .add("\n.set$L($L)", upperCase(TYPE), TYPE)
                                .add("\n.build()$<")
                                .build())
                        .build())
                .build();
    }

    private static MethodSpec createAntAnnotations() {
        return MethodSpec.methodBuilder(CREATE_ANNOTATIONS)
                .addModifiers(Modifier.FINAL)
                .returns(TypicalTypes.listOf(Annotation.class))
                .addParameter(ParameterSpec.builder(TypicalTypes.listOf(ANT_ANNOTATION_TYPE), "specs", Modifier.FINAL).build())
                .addStatement("return $T.ofNullable($L).flatMap($T::stream).map($T::$L).toList()",
                        Stream.class, "specs", Collection.class, ANT_ANNOTATION_TYPE, AS_ANNOTATION)
                .build();
    }

    private static MethodSpec createCliAnnotations() {
        return MethodSpec.methodBuilder(CREATE_ANNOTATIONS)
                .addModifiers(Modifier.FINAL)
                .returns(TypicalTypes.listOf(Annotation.class))
                .addParameter(ParameterSpec.builder(TypicalTypes.listOf(String.class), "specs", Modifier.FINAL).build())
                .addStatement("return $T.ofNullable($L).flatMap($T::stream).map($T::fromString).toList()",
                        Stream.class, "specs", Collection.class, Annotation.class)
                .build();
    }

    private static MethodSpec createMavenAnnotations() {
        return MethodSpec.methodBuilder(CREATE_ANNOTATIONS)
                .addModifiers(Modifier.FINAL)
                .returns(TypicalTypes.listOf(Annotation.class))
                .addParameter(ParameterSpec.builder(TypicalTypes.listOf(MAVEN_ANNOTATION_TYPE), "specs", Modifier.FINAL).build())
                .addStatement("return $T.ofNullable($L).flatMap($T::stream).map($T::$L).toList()",
                        Stream.class, "specs", Collection.class, MAVEN_ANNOTATION_TYPE, AS_ANNOTATION)
                .build();
    }

    private static TypeSpec gradleAnnotationSpecType() {
        return TypeSpec.classBuilder(ANNOTATION_SPEC)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addJavadoc("$L", "Configures a single Annotation.")
                .addSuperinterface(TypicalTypes.GRADLE_NAMED)
                .addMethod(gradleConstructor())
                .addMethod(gradleNamedProperty(GRADLE_MEMBER_TYPE, MEMBERS, "The annotation members"))
                .addMethod(MethodSpec.methodBuilder(AS_ANNOTATION)
                        .addModifiers(Modifier.FINAL)
                        .returns(Annotation.class)
                        .addStatement(CodeBlock.builder()
                                .add("return $T.builder()", Annotation.class)
                                .add("$>\n.set$L(getName())", upperCase(TYPE))
                                .add("\n.addAll$L(get$L().stream().map($T::$L).toList())",
                                        upperCase(MEMBERS), upperCase(MEMBERS), GRADLE_MEMBER_TYPE, AS_ANNOTATION_MEMBER)
                                .add("\n.build()$<")
                                .build())
                        .build())
                .build();
    }

    private static TypeSpec gradleAnnotationMemberSpecType() {
        return TypeSpec.classBuilder(ANNOTATION_MEMBER_SPEC)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addJavadoc("$L", "Configures a single AnnotationMember.")
                .addSuperinterface(TypicalTypes.GRADLE_NAMED)
                .addMethod(gradleConstructor())
                .addMethod(gradleStringProperty(VALUE, "The value of the annotation member"))
                .addMethod(gradleStringProperty(TYPE, "The fully-qualified type of the annotation member"))
                .addMethod(MethodSpec.methodBuilder(AS_ANNOTATION_MEMBER)
                        .addModifiers(Modifier.FINAL)
                        .returns(AnnotationMember.class)
                        .addStatement(CodeBlock.builder()
                                .add("return $T.builder()", AnnotationMember.class)
                                .add("$>\n.set$L(getName())", upperCase(KEY))
                                .add("\n.set$L(get$L().get())", upperCase(VALUE), upperCase(VALUE))
                                .add("\n.set$L(get$L().getOrElse($S))", upperCase(TYPE), upperCase(TYPE), "java.lang.String")
                                .add("\n.build()$<")
                                .build())
                        .build())
                .build();
    }

    private Annotations() {
        // data class
    }

}
