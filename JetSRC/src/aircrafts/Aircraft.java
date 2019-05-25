package aircrafts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import stores.Store;

public class Aircraft {
	String name = new String();
	HashMap<Integer, Pylon> pylons = new HashMap<>();
	Set<String> approvedConfigs = new HashSet<>();
	HashMap<String, Double> currentParameters = new HashMap<>();
	
	public Aircraft(String name) {
		this.name = name;
		//configure(configfile);
	}
	
	// precondition - a config list with details of pylons and approved configurations
	public void configure(ArrayList<Integer> config) {
		int numberPylons = config.get(0);
		// config file has number of pylons as first entry
		for (int i = 1; i < numberPylons + 1; i++) {
			Pylon pylon = new Pylon(i, 1);
			pylons.put(i, pylon);
		}
		// the rest of config file details approved configurations
		int count = 1;
		Boolean pylonAdded = false;
		StringBuilder s = new StringBuilder();
		for (int i = 1; i < config.size(); i++) {
			if (config.get(i) == 99) { // signals move to next pylon
				approvedConfigs.add(s.toString()); // add approved configs first
				s.setLength(0); // clear string builder
				pylonAdded = false;
			} else {
				// build string
				if (!pylonAdded) {
					s.append(String.valueOf(count));
					pylonAdded = true;
					count++;
				}
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

	public void addStoreToPylon(int pylon, Store store) {
		Pylon p = pylons.get(pylon);
		p.addStores(store);
		pylons.replace(pylon, p);
	}
	
}
