package aircrafts_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import aircrafts.Aircraft;
import stores.Store;
import stores.AAStore;
import stores.AGStore;
import stores.OtherStore;
import utils.*;

public class aircrafts_test {
	
	private static ArrayList<String> aircraftTypes = new ArrayList<>();
	private static HashMap<String, ArrayList<Integer>> aircraftConfigs = new HashMap<>();
	private static HashMap<Integer, String> weaponCodes = new HashMap<>();

	public static void main(String[] args) {
		// init configuration file (currently hard-coded)
		configureWeaponCodes();
		configureAircraft();
		
		// init program
		StdOut.println("Welcome to JetSRC. Pick an aircraft by typing associated number:");
		String aircraftSelected = getAircraftSelected();
		
		// create aircraft with configuration
		Aircraft aircraft = new Aircraft(aircraftSelected);
		aircraft.configure(aircraftConfigs.get(aircraftSelected));
		
		
		
		
		// offer option to add stores
		StdOut.println("Choose a pylon to configure (1 - " + aircraft.getNumberOfPylons() + ") or type EXIT");
		String pylonSelected = StdIn.readLine();
		if (pylonSelected == "EXIT") {
			System.exit(0);
		} else {
			int[] availableStores = aircraft.getStoresForPylon(pylonSelected);
			for (int i : availableStores) {
				StdOut.println(i + ": "+ weaponCodes.get(i));
			}
		}
		int storeSelected = StdIn.readInt();
		String storeName = weaponCodes.get(storeSelected);
		Store store;
		switch (storeName.substring(0, 1)) {
		case "G":
			store = new AGStore(storeName, 1.2);
			break;
		case "A":
			store = new AAStore(storeName, 1.2);
			break;
		default:
			store = new OtherStore(storeName, 1.2);
			break;
		}
		StdOut.println("Pylon " + pylonSelected + " now has a " + store.getType());
			
		
		
	}

	private static String getAircraftSelected() {
		int count = 1;
		for (String i : aircraftTypes) {
			StdOut.println(count + ": " + i);
		}
		String i = StdIn.readLine();
		return aircraftTypes.get(Integer.valueOf(i) - 1);
	}

	private static void configureWeaponCodes() { // TODO add release limits
		weaponCodes.put(1, "AIM-9M");
		weaponCodes.put(2, "ACMI POD");
		weaponCodes.put(3, "FUEL TANK");
		weaponCodes.put(4, "GBU-12");
		weaponCodes.put(5, "GBU-32");
		weaponCodes.put(6, "AIM-120");
		weaponCodes.put(7, "2 x AIM-120");
	}

	
	private static void configureAircraft() {
		// init configuration file (currently hard-coded)
		
		aircraftTypes.add("CF-18");
		// aircraftTypes.add("F-16");
		// aircraftTypes.add("F-22");
		
		ArrayList<Integer> cf18Config = new ArrayList<>();
		
		// Test configuration codes
		// 1: AIM-9 2: ACMI 3: FUEL 4: GBU-12 5: GBU-32 6: AIM-120 7: 2XAIM-120
		
		cf18Config.add(9); // pylons
		// pylon 1
		cf18Config.add(1);
		cf18Config.add(2);
		cf18Config.add(99); // next pylon
		// pylon 2
		cf18Config.add(4);
		cf18Config.add(5);
		cf18Config.add(6);
		cf18Config.add(7);
		cf18Config.add(99);
		// pylon 3
		cf18Config.add(3);
		cf18Config.add(4);
		cf18Config.add(5);
		cf18Config.add(6);
		cf18Config.add(7);
		cf18Config.add(99);
		// pylon 4
		cf18Config.add(6);
		cf18Config.add(99);
		// pylon 5
		cf18Config.add(3);
		cf18Config.add(4);
		cf18Config.add(99);
		// pylon 6
		cf18Config.add(6);
		cf18Config.add(99);
		// pylon 7
		cf18Config.add(3);
		cf18Config.add(4);
		cf18Config.add(5);
		cf18Config.add(6);
		cf18Config.add(7);
		cf18Config.add(99);
		// pylon 8
		cf18Config.add(4);
		cf18Config.add(5);
		cf18Config.add(6);
		cf18Config.add(7);
		cf18Config.add(99);
		// pylon 9
		cf18Config.add(1);
		cf18Config.add(2);
		cf18Config.add(99);
		
		aircraftConfigs.put("CF-18", cf18Config);
	}

}
