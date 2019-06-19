package gui.model;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;

import aircrafts.Aircraft;
import aircrafts.Pylon;
import configuration_manager.ConfigurationManager;
import utils.ApprovedConfigurationsConnection;
import utils.GenericSingletonFactory;
import utils.StdOut;
import utils.StringIsValidInt;

/**
 * Class for Model interaction from UI.
 *
 * @author Simon Hogg
 */
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
	/**
     * Setup initializes C1(Configuration Manager), loads default aircraft and builds C2(Pylon HashMap)
     * @category Setup
     * @param nil
     * @return void
     */
	// 
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
	
	/**
     * Add store to pylon - UI helper for Aircraft object - adds the selected store to the aircraft pylon
     * @category UI Interaction
     * @param String pylonString - the string of the pylon selected
     * @param String stroeString - the string of the store selected
     * @return boolean - if successful
     */
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
	
	// filterAircraftDropDowns
	/**
     * Edits C2(pylonHashMap) to the filtered stores from the database 
     * when the user selects a store to add to the aircraft.
     * @category Database Interface
     * @param selectedStores is a HashMap<String, String> of the selected pylon DropDowns
     * @return void
     */
	public void filterAircraftDropDowns(HashMap<String, String> selectedStores) {
		
		// we need the number of pylons selected to build our arrays
		int numberOfPylonsSelected = selectedStores.size();
		String[] pylons = new String[numberOfPylonsSelected];
		String[] stores = new String[numberOfPylonsSelected];
		
		// no we stream the selectedStores and build the arrays
		int counter = 0; // use a counter with the entrySet
		for (Entry<String, String> mapEntry : selectedStores.entrySet()) {
		    pylons[counter] = mapEntry.getKey();
		    stores[counter] = mapEntry.getValue();
		    counter++;
		}
		
		// lets get the filtered database values
		// 1st we grab the connection
		ApprovedConfigurationsConnection conn = GenericSingletonFactory.getInstance(ApprovedConfigurationsConnection.class);
		// then we get the results from the database
		ResultSet results = conn.filter(pylons, stores);
		
		// we need a list from the results set
		
		HashMap<String,LinkedHashSet<String>> pylonValuesMap = new HashMap<>(); // Map of pylon values
		List<String> colNames = new LinkedList<>(); // List of column names
		
		try {
			// took me a while to figure out how to iterate over the columns and add each row!
			ResultSetMetaData rsmd = results.getMetaData(); 
			int colCount = rsmd.getColumnCount();
			for (int i = 1; i <= colCount; i++) {
		        String colName = rsmd.getColumnName(i);
		        colNames.add(colName);
	
		        //Load the Map initially with keys(columnnames) and empty set
		        pylonValuesMap.put(colName, new LinkedHashSet<String>());
			}

	    
	    	while (results.next()) { //Iterate the resultset for each row
	    		
		    	for (String colName : colNames) {
		    		//Get the list mapped to column name
		    		LinkedHashSet<String> pylonDataSet = pylonValuesMap.get(colName);
	
		    		//Add the current row's column data to list
		    		pylonDataSet.add(results.getString(colName));
	
		    		//add the updated list of column data to the map now
		    		pylonValuesMap.put(colName, pylonDataSet);
		        }
	    	}
		} catch (SQLException e) {
		    StdOut.println(e.getMessage());
		} finally {
			try {
				results.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			
		}
		
		// Refresh the pylon map to feed the UI
		pylonsHashMap.clear();
		pylonValuesMap.forEach((k,v) -> {
			if (k.length() > 4 && StringIsValidInt.isValid(k.substring(5,6))) { // make sure the values make sense
				
				// The pylon needs to be an integer
				Integer pylon = Integer.valueOf(k.substring(5,6));
				
				// Each pylon needs a hashmap of stores
				HashMap<Integer, String> value = new HashMap<Integer, String>();
				
				// we need to convert the pylonValuesMap to the pylonsHashMap
				v.forEach(e -> {
					if (StringIsValidInt.isValid(e)) {
						Integer storeCodeInteger = Integer.valueOf(e);
						value.put(storeCodeInteger, e+"|"+config.storesCodes.get(storeCodeInteger));
					}
				});
				pylonsHashMap.put(pylon, value);
			} else {
				StdOut.println("Inproper formatting in guimodel.filterAircraftDropDown from pylonsValueMap");
			}
		});
	}
	
	
	
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
