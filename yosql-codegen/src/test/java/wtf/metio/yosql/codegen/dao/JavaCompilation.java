/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Hands generated source to {@code javac} and reports what it said.
 *
 * <p>Every other test here compares generated code against a string somebody wrote down, and that
 * string was copied from the generator's own output — so output that does not compile becomes the
 * expectation and the suite goes green over it. This asks the only question those cannot: whether
 * the file YoSQL hands the user is Java.</p>
 *
 * <p>Nothing is written to disk. The class files go to memory and are then discarded; only the
 * diagnostics matter.</p>
 */
public final class JavaCompilation {

    private JavaCompilation() {
        // utility class
    }

    /**
     * @param sources fully qualified name to source text
     * @return every error {@code javac} reported, empty when it compiled
     */
    public static List<String> errorsIn(final List<Source> sources) {
        final var compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("no java compiler on this JVM — run the tests on a JDK");
        }
        final var diagnostics = new DiagnosticCollector<JavaFileObject>();
        final var units = sources.stream().map(Source::asFileObject).toList();
        try (final var standard = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8);
             final var manager = new InMemoryClassFiles(standard)) {
            final var options = List.of("-classpath", classpath(), "-proc:none");
            compiler.getTask(null, manager, diagnostics, options, null, units).call();
        } catch (final IOException exception) {
            throw new IllegalStateException(exception);
        }
        return diagnostics.getDiagnostics().stream()
                .filter(diagnostic -> diagnostic.getKind() == Diagnostic.Kind.ERROR)
                .map(JavaCompilation::describe)
                .toList();
    }

    private static String describe(final Diagnostic<? extends JavaFileObject> diagnostic) {
        final var source = diagnostic.getSource();
        final var where = source == null ? "" : source.getName() + ":" + diagnostic.getLineNumber() + " ";
        return where + diagnostic.getMessage(null);
    }

    /**
     * What the generated code compiles against: whatever this module's tests run with.
     *
     * <p>Surefire may hand the JVM a single jar whose manifest carries the real class path, so the
     * property alone is not always the answer.</p>
     */
    private static String classpath() {
        final var declared = System.getProperty("java.class.path", "");
        final var entries = new ArrayList<String>();
        for (final var entry : declared.split(java.io.File.pathSeparator)) {
            entries.add(entry);
            entries.addAll(manifestClassPathOf(entry));
        }
        return String.join(java.io.File.pathSeparator, entries);
    }

    private static List<String> manifestClassPathOf(final String entry) {
        final var path = Path.of(entry);
        if (!entry.endsWith(".jar") || !Files.isRegularFile(path)) {
            return List.of();
        }
        try (final var jar = new JarFile(path.toFile())) {
            final var manifest = jar.getManifest();
            if (manifest == null) {
                return List.of();
            }
            final var declared = manifest.getMainAttributes().getValue("Class-Path");
            if (declared == null || declared.isBlank()) {
                return List.of();
            }
            return java.util.Arrays.stream(declared.split(" "))
                    .filter(value -> !value.isBlank())
                    .map(value -> URI.create(value).getSchemeSpecificPart())
                    .collect(Collectors.toList());
        } catch (final IOException exception) {
            return List.of();
        }
    }

    /**
     * @param qualifiedName the class the source declares
     * @param code the source text
     */
    public record Source(String qualifiedName, String code) {

        JavaFileObject asFileObject() {
            return new SimpleJavaFileObject(
                    URI.create("string:///" + qualifiedName.replace('.', '/')
                            + JavaFileObject.Kind.SOURCE.extension),
                    JavaFileObject.Kind.SOURCE) {
                @Override
                public CharSequence getCharContent(final boolean ignoreEncodingErrors) {
                    return code;
                }
            };
        }

    }

    /**
     * Compiles and then loads, so that a test can ask the running JVM what the generated code
     * actually says rather than compare its text against a second copy of the same escaping rules.
     *
     * @return a loader over the compiled classes
     * @throws IllegalStateException when the sources do not compile
     */
    public static ClassLoader compileAndLoad(final List<Source> sources) {
        final var compiler = ToolProvider.getSystemJavaCompiler();
        final var diagnostics = new DiagnosticCollector<JavaFileObject>();
        final var units = sources.stream().map(Source::asFileObject).toList();
        final var classes = new HashMap<String, byte[]>();
        try (final var standard = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8);
             final var manager = new InMemoryClassFiles(standard, classes)) {
            final var options = List.of("-classpath", classpath(), "-proc:none");
            if (!compiler.getTask(null, manager, diagnostics, options, null, units).call()) {
                throw new IllegalStateException(diagnostics.getDiagnostics().stream()
                        .filter(diagnostic -> diagnostic.getKind() == Diagnostic.Kind.ERROR)
                        .map(JavaCompilation::describe)
                        .collect(Collectors.joining("\n")));
            }
        } catch (final IOException exception) {
            throw new IllegalStateException(exception);
        }
        return new ClassLoader(JavaCompilation.class.getClassLoader()) {
            @Override
            protected Class<?> findClass(final String name) throws ClassNotFoundException {
                final var bytes = classes.get(name);
                if (bytes == null) {
                    throw new ClassNotFoundException(name);
                }
                return defineClass(name, bytes, 0, bytes.length);
            }
        };
    }

    /**
     * Keeps the class files out of the build directory — they are a by-product. Their bytes are kept
     * in memory so that {@link #compileAndLoad(List)} can define them.
     */
    private static final class InMemoryClassFiles extends ForwardingJavaFileManager<JavaFileManager> {

        private final Map<String, byte[]> classes;

        private InMemoryClassFiles(final JavaFileManager delegate) {
            this(delegate, null);
        }

        private InMemoryClassFiles(final JavaFileManager delegate, final Map<String, byte[]> classes) {
            super(delegate);
            this.classes = classes;
        }

        @Override
        public JavaFileObject getJavaFileForOutput(
                final Location location,
                final String className,
                final JavaFileObject.Kind kind,
                final FileObject sibling) {
            return new SimpleJavaFileObject(
                    URI.create("memory:///" + className.replace('.', '/') + kind.extension), kind) {
                @Override
                public OutputStream openOutputStream() {
                    return new ByteArrayOutputStream() {
                        @Override
                        public void close() {
                            if (classes != null) {
                                classes.put(className, toByteArray());
                            }
                        }
                    };
                }
            };
        }

        @Override
        public boolean hasLocation(final Location location) {
            return location != StandardLocation.CLASS_OUTPUT && super.hasLocation(location);
        }

    }

}
