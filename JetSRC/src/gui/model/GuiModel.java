package gui.model;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.stream.Stream;

import aircrafts.Aircraft;
import aircrafts.Pylon;
import configuration_manager.ConfigurationManager;
import utils.GenericSingletonFactory;
import utils.StdOut;
import utils.StringIsValidInt;

public class GuiModel {
	
	// CONSTANTS
	String defaultAircraftString = "CF-18 0.1";
	String storeInputRegexString = "\\|";
	
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
		
		// we need to work on the shared instance of the configuration manager
		config = GenericSingletonFactory.getInstance(ConfigurationManager.class);
		config.setup();
		
		// Helper method in this class which sets up default aircraft
		loadNewAircraft(defaultAircraftString);
		// Helper method in this class which builds C2
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
		
		// we need to convert the pylon string and store string to the input for the aircraft
		String[] storeArrayStrings = new String[1];
		// just grab pylon number
		storeArrayStrings[0] = pylonString.substring(3);
		// split store by constant 
		String[] storeInput = storeString.split(storeInputRegexString);
		// addStoreToAircraft requires a string array so we combine them
		String[] storeInputComplete = Stream.of(storeArrayStrings, storeInput)
				.flatMap(Stream::of)
                .toArray(String[]::new);
		boolean added = config.aircraft.addStoreToAircraft(storeInputComplete);
		// Let the world know it worked
		return added;
	}

	
	// Clear pylon of stores
	
	public void clearStoreFromPylon(String pylonString) {
		if (StringIsValidInt.isValid(pylonString)) {
			Integer pylon = Integer.valueOf(pylonString);
			config.aircraft.removeStoresFromPylon(pylon);
		} else {
			StdOut.println("Problem with pylonString in clearStoreFromPylon");
		}
		
	}
	
	// save current aircraft
	
	public void saveCurrentAircraft() {
		Aircraft a = config.aircraft;
		config.saveAircraft(a);
	}
	
	public void getSavedAircraft(String aircraft) {
		Aircraft a;
		try {
			a = (Aircraft)config.retrieveSavedAircraft(aircraft);
			config.aircraft = a;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// DATABASE INTERFACE
	
	// get all data
	
	
	
	// HELPER METHODS
	
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
	
	// Get stores for pylon - UI helper to load available stores
	private HashMap<Integer, String> getStoresForPylon(int pylon) {
		Pylon p = config.aircraft.getPylons().get(pylon);
		return p.approvedStores;
	}
}
