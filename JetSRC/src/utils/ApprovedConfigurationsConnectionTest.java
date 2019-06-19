package utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ApprovedConfigurationsConnectionTest {

	@Test
	void testAddPreparedStatements() {
		ApprovedConfigurationsConnection configurationsConnection = new ApprovedConfigurationsConnection();
		configurationsConnection.addPreparedStatements();
		assertTrue(configurationsConnection.statements.size() != 0);
		configurationsConnection.closeAllConnections();
	}
}
