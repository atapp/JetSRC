package main;

import aircrafts.Aircraft;
import file_manager.FileImporter;
import utils.StdOut;

public class Jet_SRC {

	public static void main(String[] args) {
		// init program
		ConfigurationManager config = new ConfigurationManager();
		StdOut.println("Welcome to JetSRC. Pick an aircraft by typing associated number:");
		String aircraftSelected = getAircraftSelected();
		
		// create aircraft with configuration
		Aircraft aircraft = new Aircraft(aircraftSelected);
		aircraft.configure(aircraftConfigs.get(aircraftSelected));

	}

}
