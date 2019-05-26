package configuration_manager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ConfigurationManagerTest {

	@Test
	void test() {
		try {
			ConfigurationManager config = new ConfigurationManager();
			config.setup(true);
		} catch (Exception e) {
			fail("Config manager threw and exception");
		}			
	}
}
