package de.xn__ho_hia.yosql.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SqlTypeTest {

	@Test
	void shouldDefineKnownValue() {
		Assertions.assertNotNull(SqlType.valueOf("READING"));
	}
	
}
