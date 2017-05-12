package de.xn__ho_hia.yosql.testutils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A validation file is used by test to compare complex {@link String strings} against the expected content of the
 * validation file.
 */
public interface ValidationFile {

    /**
     * @return The content of this validation file.
     */
    default String read() {
        return read(StandardCharsets.UTF_8);
    }

    /**
     * @param charset
     *            The charset to use while reading the validation file.
     * @return The content of this validation file, read with given charset.
     */
    String read(Charset charset);

}
