package gui.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import aircrafts.Aircraft;
import aircrafts.Pylon;
import configuration_manager.ConfigurationManager;
import sun.print.PSPrinterJob.PluginPrinter;
import utils.GenericSingletonFactory;
import utils.StdOut;

class GuiModelTest {
	
	static private ConfigurationManager config;
	private HashMap<Integer, HashMap<Integer,String>> pylonsHashMap = new HashMap<>();
	
	@BeforeAll
	static void setup() {
		config = new ConfigurationManager();
		config.setup();
	}

	@Test
	void testSetup() {
		// Test model is able to deliver pylons has map for GUI.
		assertTrue(config.aircraftTypes.size() > 0);
	}
	
	@Test
	void testLoadNewAircraft() {
		loadNewAircraft("CF-18 0.1");
		assertFalse(config.aircraftTypes.size() == 0);
	}
	
	@Test
	void testBuildPylonMap() {
		loadNewAircraft("CF-18 0.1");
		buildPylonMap();
		System.out.println(pylonsHashMap.get(1).get(1));
		assertTrue(pylonsHashMap.get(1).get(1).equalsIgnoreCase("1|null"));
	}
	
	private void loadNewAircraft(String wString) {
		// create aircraft with configuration selected
				config.aircraft = GenericSingletonFactory.getInstance(Aircraft.class);
				config.aircraft.configure(
						wString,
						// corresponding config file 
						config.aircraftConfigs.get(wString)
						);
				
	}
	
	private HashMap<Integer, String> getStoresForPylon(int pylon) {
		Pylon p = config.aircraft.getPylons().get(pylon);
		return p.approvedStores;
	}
	
	private void buildPylonMap() {
		for (int i = 0; i < config.aircraft.getNumberOfPylons(); i++) {
			// take pylon number
			int pylonNumber = i+1;
			// get available stores fro that pylon
			HashMap<Integer, String> availableStores = getStoresForPylon(pylonNumber);
			// add pylon number and stores (number, string) to map
			pylonsHashMap.put(pylonNumber, availableStores);
		}
	}

}
