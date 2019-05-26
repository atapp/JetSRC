package configuration_manager;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import utils.StdOut;

//ConfigFileFormatExtension is a exception class to help deal with the complex formatting required in the configuration files.
public class ConfigFileFormatException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// ConfigFileException constructor
	// precondition : two strings are passed to the exception
	// postcondition 1 : a date stamped error log is saved 
	// postcondition 2 : a string is printed on the console
	public ConfigFileFormatException(String file, String error){
		super(error);
		try (PrintWriter outfilepw = new PrintWriter("src/configuration_files/error_log.txt");) 
	    {  
			Date myDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
			String myDateString = sdf.format(myDate);
			outfilepw.format("Config File Error at %s at file %s with error %s", myDateString, file, error); // postcondition 1
	                 
	    }
	    catch (FileNotFoundException ex)
	    {
	        ex.printStackTrace();
	    }
		StdOut.println("Formatting excpetion in the "+ file +" configuration file:"); // postcondition 2
		StdOut.println(error);
	}
}
