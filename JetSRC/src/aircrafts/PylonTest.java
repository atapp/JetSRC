package aircrafts;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import stores.AGStore;
import stores.Store;

class PylonTest {

	@Test
	void testGetStores() {
		Pylon pylon = new Pylon(3, 2);
		Store store = new AGStore(1, "1|AIM-9M|A-A|1.8,0,0,0","test", 1.1, 1.1, 1.1, "2G to 45");
		pylon.addStores(store);
		Store retrievedStore = pylon.getStores().get(0);
		assert(store == retrievedStore);
	}

	@Test
	void testAddStores() {
		Pylon pylon = new Pylon(3, 2);
		Store store = new AGStore(1, "1|AIM-9M|A-A|1.8,0,0,0","test", 1.1, 1.1, 1.1, "2G to 45");
		pylon.addStores(store);
		assert(pylon.getStores().size() > 0);
	}

	@Test
	void testSetStores() {
		Pylon pylon = new Pylon(3, 2);
		ArrayList<Store> stores = new ArrayList<Store>();
		Store store1 = new AGStore(1, "1|AIM-9M|A-A|1.8,0,0,0","test", 1.1, 1.1, 1.1, "2G to 45");
		Store store2 = new AGStore(1, "1|AIM-9M|A-A|1.8,0,0,0","test", 1.1, 1.1, 1.1, "2G to 45");
		stores.add(store1);
		stores.add(store2);
		pylon.setStores(stores);
		assertTrue(pylon.getStores().size() == 2);
	}

}
