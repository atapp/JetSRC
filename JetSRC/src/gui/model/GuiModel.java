package gui.model;

import java.util.HashMap;
import java.util.stream.Stream;

import aircrafts.Aircraft;
import aircrafts.Pylon;
import configuration_manager.ConfigurationManager;
import utils.GenericSingletonFactory;

public class GuiModel {
	
	// CLASS INVARIANTS
	
	// C1 (config) Configuration Manager shared instance
	public ConfigurationManager config;
	// C2 (pylonsHashMap) Map of aircraft pylons for UI display
	public HashMap<Integer, HashMap<Integer,String>> pylonsHashMap = new HashMap<>();
	
	public GuiModel() {
	}

	// METHODS
	
	// setup initializes C1, loads default aircraft and builds C2
	public void setup() {
		config = GenericSingletonFactory.getInstance(ConfigurationManager.class);
		config.setup();
		loadNewAircraft("CF-18 0.1");
		buildPylonMap();
	}
	
	// overloading setup initializes C1, loads specified aircraft and builds C2
	public void setup(String aircraft) {
		config = GenericSingletonFactory.getInstance(ConfigurationManager.class);
		config.setup();
		loadNewAircraft(aircraft);
		buildPylonMap();
	}
	
	
	// Add store to pylon - UI helper for Aircraft object interaction
	public boolean addStoreToPylon(String pylonString, String storeString) {
		String[] storeArrayStrings = new String[1];
		storeArrayStrings[0] = pylonString.substring(3);
		String[] storeInput = storeString.split("\\|");
		String[] storeInputComplete = Stream.of(storeArrayStrings, storeInput)
				.flatMap(Stream::of)
                .toArray(String[]::new);
		boolean added = config.aircraft.addStoreToAircraft(storeInputComplete);
		return added;
	}
	
	// Get stores for pylon - UI helper to load available stores
	private HashMap<Integer, String> getStoresForPylon(int pylon) {
		Pylon p = config.aircraft.getPylons().get(pylon);
		return p.approvedStores;
	}
	
	// Helper method for setup
		private void loadNewAircraft(String wString) {
			// create aircraft with configuration selected
					config.aircraft = GenericSingletonFactory.getInstance(Aircraft.class);
					config.aircraft.configure(
							wString,
							// corresponding config file 
							config.aircraftConfigs.get(wString)
							);
					
		}
	
	// Setup helper
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
