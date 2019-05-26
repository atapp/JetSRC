package utils;

public class StringIsValidDouble {
	public StringIsValidDouble() {}
	
	public static boolean isValid(String string) {
		try {
			@SuppressWarnings("unused")
			Double valueDouble = Double.parseDouble(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
		
	}
}
