package de.xn__ho_hia.yosql.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoggingAPITest {

	@Test
	public void shouldDeclareKnownValues() {
		Assertions.assertNotNull(LoggingAPI.valueOf("NONE"));
	}

	@Test
	public void shouldDeclareKnownValues2() {
		Assertions.assertNotNull(LoggingAPI.valueOf("JDK"));
	}

}
