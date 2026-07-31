/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.model.generator;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheException;

import java.io.IOException;
import java.io.Writer;

/**
 * Mustache factory that does not encode html tags - it passes its supplied raw values as-is to the template.
 */
public final class RawTextMustacheFactory extends DefaultMustacheFactory {

    @Override
    public void encode(final String value, final Writer writer) {
        try {
            writer.write(value);
        } catch (final IOException exception) {
            throw new MustacheException(exception);
        }
    }

}
