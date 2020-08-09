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
