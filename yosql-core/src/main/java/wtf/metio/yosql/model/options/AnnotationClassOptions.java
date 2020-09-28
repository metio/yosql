/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.options;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * Enumeration of all known options for the "@Generated" annotation class itself.
 */
@LocaleData(@Locale("en"))
@BaseName("annotation-class-options")
public enum AnnotationClassOptions {

    /**
     * Option that disables adding a new annotation
     */
    NONE,

    /**
     * Uses the "javax.annotation.Generated" annotation
     */
    ANNOTATION_API,

    /**
     * Uses the "javax.annotation.Generated" annotation
     */
    PROCESSING_API;

}
