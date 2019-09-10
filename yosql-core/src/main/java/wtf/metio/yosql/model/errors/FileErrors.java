package wtf.metio.yosql.model.errors;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@LocaleData(@Locale("en"))
@BaseName("file-errors")
public enum FileErrors {

    NO_READ_PERMISSION,
    NO_WRITE_PERMISSION,
    NOT_A_DIRECTORY,
    NOT_EXISTS,
    DIRECTORY_CREATION_FAILED,
    CANNOT_CREATE_DIRECTORY,

}
