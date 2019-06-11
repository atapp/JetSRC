package aircrafts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import configuration_manager.ConfigFileFormatException;
import configuration_manager.ConfigurationManager;
import stores.Store;
import stores.StoreFactory;
import utils.GenericSingletonFactory;

public class Aircraft implements Serializable {
	
	private static final long serialVersionUID = 2996265557227528324L;
	
	String name = new String();
	HashMap<Integer, Pylon> pylons = new HashMap<>();
	Set<String> approvedConfigs = new HashSet<>();
	HashMap<String, Double> currentParameters = new HashMap<>();
	ConfigurationManager configurationManager;
	
	public Aircraft() {}
	
	// precondition - a config list with details of pylons and approved configurations
	public void configure(String name, ArrayList<Integer> config) {
		configurationManager = GenericSingletonFactory.getInstance(ConfigurationManager.class);
		this.name = name;
		int numberPylons = config.get(0);
		// config file has number of pylons as first entry
		for (int i = 1; i < numberPylons + 1; i++) {
			Pylon pylon = new Pylon(i, 1);
			pylons.put(i, pylon);
		}
		// the rest of config file details approved configurations
		int count = 1;
		Boolean pylonNumberAdded = false;
		StringBuilder s = new StringBuilder();
		for (int i = 1; i < config.size(); i++) {
			if (config.get(i) == 99) { // signals move to next pylon
				approvedConfigs.add(s.toString()); // add approved configs first
				s.setLength(0); // clear string builder
				pylonNumberAdded = false;
				count++;
			} else {
				// build string
				if (!pylonNumberAdded) {
					s.append(String.valueOf(count));
					pylonNumberAdded = true;
				}
				Pylon p = pylons.get(count);
				String storeString = config.get(i)+ "|" + configurationManager.storesCodes.get(config.get(i));
				p.approvedStores.put(config.get(i), storeString);
				pylons.replace(count, p);
				s.append(String.valueOf(config.get(i)));	
			}
		}
	}
	
	public int getNumberOfPylons() {
		return pylons.size();
	}
	
	// pre-condition : pylon number as string
	// post-condition : stores allowed on pylon as int array
	public int[] getStoresForPylon(String p) {
		String[] storesFromConfig = approvedConfigs.stream()
				.filter(c -> c.startsWith(p))
				.toArray(String[]::new);
		
		int[] storesForPylon = Arrays.stream(storesFromConfig[0].split("")).mapToInt(Integer::parseInt).toArray();
	
		return Arrays.copyOfRange(storesForPylon, 1, storesForPylon.length);
	}

	public void addStoreToPylon(Integer pylon, Store store) {
		Pylon p = pylons.get(pylon);
		p.addStores(store);
		pylons.replace(pylon, p);
	}
	
	public String headerForFile() {
		StringBuilder returnStringBuilder = new StringBuilder();
		returnStringBuilder.append(this.name);
		returnStringBuilder.append("_");
		for (int i = 0; i < this.getNumberOfPylons(); i++) {
			Pylon pylon = this.pylons.get(i+1);
			if (pylon.getStores().size() > 0){
				for (int j = 0; j < pylon.getStores().size(); j++) {
					returnStringBuilder.append("Pylon");
					returnStringBuilder.append(pylon.location);
					returnStringBuilder.append("-");
					returnStringBuilder.append(pylon.getStores().get(j).toString());
					returnStringBuilder.append("_");
				}
			}
		}
		return returnStringBuilder.toString();
	}
	
	// add store to aircraft - adds a new store to the aircraft
	// Precondition - C2 (aircraft) is initialized and setup, storeInput is a valid String array [pylonSelected, storeSelected].
	// Postcondition : store is added to aircraft returns true
	public boolean addStoreToAircraft(String[] storeInput) {
		Store store;
		Integer storeCode = Integer.valueOf(storeInput[1]);
		String storeFromConfig = configurationManager.storesCodes.get(storeCode);
		String[] storeFromConfigArray = storeFromConfig.split("\\|");
		try {
			store = StoreFactory.getStore(storeFromConfigArray[1], storeFromConfigArray[0], storeFromConfigArray[2]);
		} catch (ConfigFileFormatException e) {
			e.printStackTrace();
			return false;
		}
		addStoreToPylon(Integer.valueOf(storeInput[0]), store);
		return true;
	}
	
	public String toString() {
		StringBuilder returnStringBuilder = new StringBuilder();
		for (int i = 0; i < this.getNumberOfPylons(); i++) {
			Pylon pylon = this.pylons.get(i+1);
			returnStringBuilder.append("Pylon ");
			returnStringBuilder.append(pylon.location);
			returnStringBuilder.append(": ");
			if (pylon.getStores().size() > 0){
				for (int j = 0; j < pylon.getStores().size(); j++) {
					returnStringBuilder.append(pylon.getStores().get(j).getType());
					returnStringBuilder.append("\n");
				}
			} else {
				returnStringBuilder.append("Empty");
				returnStringBuilder.append("\n");
			}
		}
		return returnStringBuilder.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<Integer, Pylon> getPylons() {
		return pylons;
	}

	public void setPylons(HashMap<Integer, Pylon> pylons) {
		this.pylons = pylons;
	}

	public Set<String> getApprovedConfigs() {
		return approvedConfigs;
	}

	public void setApprovedConfigs(Set<String> approvedConfigs) {
		this.approvedConfigs = approvedConfigs;
	}

	public HashMap<String, Double> getCurrentParameters() {
		return currentParameters;
	}

	public void setCurrentParameters(HashMap<String, Double> currentParameters) {
		this.currentParameters = currentParameters;
	}
	
}
