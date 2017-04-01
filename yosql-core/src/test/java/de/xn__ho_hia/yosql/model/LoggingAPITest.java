package de.xn__ho_hia.yosql.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
class LoggingAPITest {

    @Test
    void shouldDeclareKnownValues() {
        System.out.println("executed"); //$NON-NLS-1$
        Assertions.assertNotNull(LoggingAPI.valueOf("NONE")); //$NON-NLS-1$
    }

    @Test
    void shouldDeclareKnownValues2() {
        Assertions.assertNotNull(LoggingAPI.valueOf("JDK")); //$NON-NLS-1$
    }

}
