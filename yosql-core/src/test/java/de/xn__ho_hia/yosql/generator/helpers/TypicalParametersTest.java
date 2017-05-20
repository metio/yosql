package de.xn__ho_hia.yosql.generator.helpers;

import static de.xn__ho_hia.yosql.generator.helpers.TypicalParameters.connection;
import static de.xn__ho_hia.yosql.generator.helpers.TypicalParameters.dataSource;

import org.junit.jupiter.api.Test;

import de.xn__ho_hia.yosql.testutils.ValidationFile;
import de.xn__ho_hia.yosql.testutils.ValidationFileTest;

@SuppressWarnings("static-method")
final class TypicalParametersTest extends ValidationFileTest {

    @Test
    public void shouldSpecifyDataSource(final ValidationFile validationFile) {
        validate(dataSource(), validationFile);
    }

    @Test
    public void shouldSpecifyConnection(final ValidationFile validationFile) {
        validate(connection(), validationFile);
    }

}
