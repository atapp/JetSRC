package stores;

import configuration_manager.ConfigFileFormatException;
import utils.StringIsValidDouble;

public class StoreFactory {
	public static Store getStore(String type, String name, String releaseLimits) throws ConfigFileFormatException {
		// crjs values (carriage, release, jettison, seperation)
		String[] releaseLimitArray= releaseLimits.split(",");
		if (!(releaseLimitArray.length == 4)) {
			throw new ConfigFileFormatException("Store config file", "Release limits are formatted incorrectly, make sure they are decimal mach numbers seperated by commas");
		}
		for (int i = 0; i < releaseLimitArray.length; i++) {
			if ((i < 3) && (!StringIsValidDouble.isValid(releaseLimitArray[i]))) {
				throw new ConfigFileFormatException("Store config file", "Release limits are formatted incorrectly, make sure they are decimal mach numbers seperated by commas");
			}
		}
		if ("A-A".equalsIgnoreCase(type)) {
			return new AAStore(name, Double.valueOf(releaseLimitArray[0]), Double.valueOf(releaseLimitArray[2]));
		} else if ("A-G".equalsIgnoreCase(type)) {
			return new AGStore(name, Double.valueOf(releaseLimitArray[0]), Double.valueOf(releaseLimitArray[1]), Double.valueOf(releaseLimitArray[2]), releaseLimitArray[3]);
		} else {
			return new OtherStore(name, Double.valueOf(releaseLimitArray[0]));
		}
	}
}
