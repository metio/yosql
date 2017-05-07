package de.xn__ho_hia.yosql.generator.helpers;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.squareup.javapoet.CodeBlock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@SuppressWarnings({ "nls", "static-method" })
class TypicalCodeBlocksTest {

    @Test
    @SuppressWarnings("unused")
    public void shouldAssignFieldToValueWithSameName(final TestInfo testInfo) throws Exception {
        // given
        final String name = "test";

        // when
        final CodeBlock codeBlock = TypicalCodeBlocks.setFieldToSelf(name);

        // then
        Assertions.assertEquals(validationFile(testInfo), codeBlock.toString());
    }

    @SuppressWarnings("unused")
    private static String validationFile(final TestInfo testInfo) throws Exception {
        final String testName = testInfo.getTestClass().map(Class::getSimpleName).orElse("") +
                testInfo.getTestMethod().map(Method::getName).map("#"::concat).orElse("") +
                ".txt";

        final String resourceDir = "src/test/resources/";
        String path = System.getenv("TEST_SRCDIR");
        if (path == null) {
            path = resourceDir;
        } else {
            path = Paths.get(path, "/__main__/yosql-core/" + resourceDir).toString();
        }
        final Path path2 = Paths.get(path, testName);
        final byte[] readAllBytes = Files.readAllBytes(path2);
        return new String(readAllBytes, StandardCharsets.UTF_8);
    }

}
