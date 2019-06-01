package utils;

public class StringIsValidType<T> {
	public StringIsValidType() {};
	
	public static boolean isValid(String string, Class<?> T) {
		if (T.getClass().equals(Double.class))
			try {
				@SuppressWarnings("unused")
				Double valueDouble = Double.parseDouble(string);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}	
		else if (T.getClass().equals(Integer.class)){
			try {
				@SuppressWarnings("unused")
				Integer valueInteger = Integer.getInteger(string);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}	
		}
		else return false;
		
	}
}
