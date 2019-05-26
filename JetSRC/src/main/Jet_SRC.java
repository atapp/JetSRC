// for explanation see main();

package main;

import aircrafts.Aircraft;
import configuration_manager.ConfigFileFormatException;
import configuration_manager.ConfigurationManager;
import stores.Store;
import stores.StoreFactory;
import utils.StdIn;
import utils.StdOut;
import utils.StringIsValidInt;

public class Jet_SRC {
	
	// CLASS INVARIANTS
	// C1 (config) : An instance of ConfigurationManager
	private static ConfigurationManager config = new ConfigurationManager();
	// C2 (aircraft) : Aircraft instance
	private static Aircraft aircraft;

	public static void main(String[] args) {
		// init program
		config.setup();
		
		// check if multiple aircraft are available... 
		String workingAircraftString; // name of aircraft
		if (config.aircraftTypes.size() == 1) { // if not don't bother asking which one...
			workingAircraftString = config.aircraftTypes.get(0);
			printWelcomeMessage(workingAircraftString); // print welcome message with version
			
		} else { // multiple aircraft available so ask which one
			printWelcomeMessage();
			workingAircraftString = config.aircraftTypes.get(askUserWhichAircraft());
		}
		
		// create aircraft with configuration selected
		aircraft = new Aircraft(workingAircraftString);
		aircraft.configure(
				// corresponding config file 
				config.aircraftConfigs.get(workingAircraftString)
				);
		
		// add stores (repeat till calculation)
		boolean addingStores = true;
		while(addingStores) {
			String[] storeCode = askUserWhichStoreCode(); // returns [pylon,store]
			boolean storeAdded = addStoreToAircraft(storeCode); // returns true if valid
			if (storeAdded) {
				StdOut.println("Store added!");
				StdOut.println(aircraft.toString());
			} else {
				StdOut.println("Problem adding store, try again.");
			}	
		}
		
		
	}
	
	// add store to aircraft - adds a new store to the aircraft
	// Precondition - C2 (aircraft) is initialized and setup, storeInput is a valid String array [pylonSelected, storeSelected].
	// Postcondition : store is added to aircraft returns true
	static boolean addStoreToAircraft(String[] storeInput) {
		Store store;
		Integer storeCode = Integer.valueOf(storeInput[1]);
		String storeFromConfig = config.storesCodes.get(storeCode);
		String[] storeFromConfigArray = storeFromConfig.split("\\|");
		try {
			store = StoreFactory.getStore(storeFromConfigArray[1], storeFromConfigArray[0], storeFromConfigArray[2]);
		} catch (ConfigFileFormatException e) {
			e.printStackTrace();
			return false;
		}
		aircraft.addStoreToPylon(Integer.valueOf(storeInput[0]), store);
		return true;
	}
	
	// askUserWhichStoreCode - gets the users requested store code from those available.
	// Precondition : C1 (config) is initialized and setup, C2 (aircraft) is initialized and setup
	// Postcondition : int is returned indicating the selected store type 
	static String[] askUserWhichStoreCode() {
		String pylonSelected; // part of return value
		String storeSelected; // part of return value
		int numberOfPylons = aircraft.getNumberOfPylons();
		while(true) {
			StdOut.println("Choose a pylon to configure (1 - " + numberOfPylons + ") or type EXIT");
			pylonSelected = StdIn.readLine();
			if (pylonSelected.equals("EXIT")) {
				StdOut.println("Bye!");
				System.exit(0);
			} else if 
				(StringIsValidInt.isValid(pylonSelected) && // check for valid input
				(Integer.valueOf(pylonSelected) <=  numberOfPylons)) // check not out of range
			{
				int[] availableStores = aircraft.getStoresForPylon(pylonSelected);
				for (int i : availableStores) {
					StdOut.println(i + ": "+ config.storesCodes.get(i).split("\\|")[0]);
				}
				break;
			} else {
				StdOut.println("You selected an invalid number"); // invalid input
			}
			
		} // store list displayed - now get valid store input
		while(true) {
			storeSelected = StdIn.readLine();
			if 
				(StringIsValidInt.isValid(storeSelected) && // check for valid input
				(Integer.valueOf(storeSelected) <=  config.storesCodes.size())) // check not out of range
			{
				break; // valid
			} else {
				StdOut.println("You selected an invalid number - try again"); // invalid input
			}
		}
		String[] returnStrings = {pylonSelected,storeSelected};
		return returnStrings;
	}
	
	// askUserWhichAircraft - gets the users requested aircraft.
	// Precondition : C1 (config) is initialized and setup
	// Postcondition : int is returned indicating the selected aircraft type 
	static int askUserWhichAircraft() {
		StdOut.println("Pick an aircraft by typing associated number:");
		for (int i = 1; i < config.aircraftTypes.size(); i++) { // precondition
			StdOut.println(i + ": " + config.aircraftTypes.get(i));
		}
		int userAnswerInt = StdIn.readInt();
		return userAnswerInt; // postcondition
	}
	
	// Welcome message helper
	static void printWelcomeMessage() {
		StdOut.print(
				"#################################################\n\nWelcome to JetSRC\n");
	}
	
	// Welcome message helper (overloaded) if one aircraft
	static void printWelcomeMessage(String aircraft) {
		StdOut.print(
				"#################################################\n\nWelcome to JetSRC (" + aircraft +")\n");
	}

}
