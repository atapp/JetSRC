package aircrafts;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class AircraftTest {

	@Test
	void testAircraft() {
		Aircraft aircraft = new Aircraft();
		ArrayList<Integer> config = new ArrayList<Integer>();
		config.add(2);
		config.add(1);
		config.add(2);
		config.add(99);
		config.add(3);
		config.add(4);
		config.add(99);
		aircraft.configure("CF-18", config);
		assert(aircraft.getNumberOfPylons() == 2);
	}

}
