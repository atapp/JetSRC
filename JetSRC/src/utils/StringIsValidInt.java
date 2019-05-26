package utils;

public class StringIsValidInt {
	public StringIsValidInt() {}
	
	public static boolean isValid(String string) {
		try {
			@SuppressWarnings("unused")
			Integer valueInteger = Integer.getInteger(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
		
	}
}
