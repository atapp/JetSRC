package stores;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StoreFactoryTest {

	@SuppressWarnings("static-access")
	@Test
	void testGetStore() {
		StoreFactory sFactory = new StoreFactory();
		try {
			sFactory.getStore(1, "AIM-9M|A-A|1.8,0,0,0");
		} catch (Exception e) {
			fail("Store factory threw and exception");
		}
		
	}

}
