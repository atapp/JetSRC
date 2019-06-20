package stores;

import configuration_manager.ConfigFileFormatException;
import utils.StringIsValidDouble;

// Store Factory for abstract type Store creation

public class StoreFactory {
	
	// Factory method for Store creation
	// Precondition: String type is A-A, A-G or Other | String name is name of store | String releaseLimits is comma delimited list of limits
	// Postcondition: A new sub class of Store is created
	// Throws: ConfigFileFormatException
	//storeFromConfigArray[1], storeFromConfigArray[0], storeFromConfigArray[2]
	public static Store getStore(Integer storeCode, String storeFromConfig) throws ConfigFileFormatException {
		String[] storeFromConfigArray = storeFromConfig.split("\\|");
		String type = storeFromConfigArray[1];
		String name = storeFromConfigArray[0];
		String releaseLimits = storeFromConfigArray[2];
		// split release limits into array
		String[] releaseLimitArray= releaseLimits.split(",");
		
		// Check release limits are valid (TODO: move to ConfigurationManager)
		if (!(releaseLimitArray.length == 4)) {
			throw new ConfigFileFormatException("Store config file", "Release limits are formatted incorrectly, make sure they are decimal mach numbers seperated by commas");
		}
		for (int i = 0; i < releaseLimitArray.length; i++) {
			if ((i < 3) && (!StringIsValidDouble.isValid(releaseLimitArray[i]))) {
				throw new ConfigFileFormatException("Store config file", "Release limits are formatted incorrectly, make sure they are decimal mach numbers seperated by commas");
			}
		}
		
		// Return corresponding subclass 
		if ("A-A".equalsIgnoreCase(type)) {
			return new AAStore(storeCode, storeFromConfig, name, Double.valueOf(releaseLimitArray[0]), Double.valueOf(releaseLimitArray[2]));
		} else if ("A-G".equalsIgnoreCase(type)) {
			return new AGStore(storeCode, storeFromConfig, name, Double.valueOf(releaseLimitArray[0]), Double.valueOf(releaseLimitArray[1]), Double.valueOf(releaseLimitArray[2]), releaseLimitArray[3]);
		} else {
			return new OtherStore(storeCode, storeFromConfig, name, Double.valueOf(releaseLimitArray[0]));
		}
	}
}
