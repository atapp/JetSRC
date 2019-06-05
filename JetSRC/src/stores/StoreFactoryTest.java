package stores;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StoreFactoryTest {

	@Test
	void testGetStore() {
		StoreFactory sFactory = new StoreFactory();
		try {
			sFactory.getStore("A-A","test" , "1.8,0,0,0");
		} catch (Exception e) {
			fail("Store factory threw and exception");
		}
		
	}

}
