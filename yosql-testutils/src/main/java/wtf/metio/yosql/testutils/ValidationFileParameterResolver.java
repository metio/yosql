/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.testutils;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * JUnit5 {@link ParameterResolver} for {@link ValidationFile}s.
 */
public final class ValidationFileParameterResolver implements ParameterResolver {

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) {
        final var testClass = extensionContext.getTestClass();
        final var testMethod = extensionContext.getTestMethod();
        if (testClass.isPresent() && testMethod.isPresent()) {
            final var fileName = testClass.get().getName().replace(".", "/") + "/" +
                    testMethod.get().getName();
            return new TxtValidationFile(fileName);
        }
        throw new IllegalStateException("No test class or test method found.");
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) {
        final var parameter = parameterContext.getParameter();
        final var type = parameter.getType();
        return ValidationFile.class.isAssignableFrom(type);
    }

    private static final class TxtValidationFile implements ValidationFile {

        private final String fileName;

        public TxtValidationFile(final String fileName) {
            this.fileName = fileName;
        }

        @Override
        public String read(int number) {
            try {
                final var pathToValidationFile = Paths.get("src/test/resources/")
                        .resolve(fileName + number + ".txt");
                final var readAllBytes = Files.readAllBytes(pathToValidationFile);
                return new String(readAllBytes, StandardCharsets.UTF_8);
            } catch (final IOException exception) {
                throw new IllegalArgumentException(exception);
            }
        }

    }

}
