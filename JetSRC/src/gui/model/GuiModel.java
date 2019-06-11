package gui.model;

import java.util.HashMap;

import aircrafts.Aircraft;
import aircrafts.Pylon;
import configuration_manager.ConfigurationManager;
import utils.GenericSingletonFactory;

public class GuiModel {
	public ConfigurationManager config;
	public HashMap<Integer, HashMap<Integer,String>> pylonsHashMap = new HashMap<>();
	
	public GuiModel() {
	}

	public void setup() {
		config = GenericSingletonFactory.getInstance(ConfigurationManager.class);
		config.setup();
		loadNewAircraft("CF-18 0.1");
		buildPylonMap();
	}
	
	public void setup(String aircraft) {
		config = GenericSingletonFactory.getInstance(ConfigurationManager.class);
		config.setup();
		loadNewAircraft(aircraft);
		buildPylonMap();
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
