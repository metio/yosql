/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
