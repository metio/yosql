/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

/**
 * All known configuration group/settings tags.
 */
public final class Tags {

    public static final String CLASSES = "classes";
    public static final String FIELDS = "fields";
    public static final String METHODS = "methods";
    public static final String THREADS = "threads";
    public static final String FRONT_MATTER = "frontmatter"; // TODO: tag all settings that can be changed in the frontmatter and link to sitemap/tags in website

    private Tags() {
        // constants class
    }

}
