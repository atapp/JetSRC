package aircrafts;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class AircraftTest {

	@Test
	void testAircraft() {
		Aircraft aircraft = new Aircraft("CF-18");
		assert(aircraft.name == "CF-18");
	}

	@Test
	void testConfigure() {
		Aircraft aircraft = new Aircraft("CF-18");
		ArrayList<Integer> config = new ArrayList<Integer>();
		config.add(2);
		config.add(1);
		config.add(2);
		config.add(99);
		config.add(3);
		config.add(4);
		config.add(99);
		aircraft.configure(config);
		assert(aircraft.getNumberOfPylons() == 2);
	}

}
