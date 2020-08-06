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

    NONE,

    ANNOTATION_API,

    PROCESSING_API,

    // TODO: move to i18n class?
    DEFAULT,

}
