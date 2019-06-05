package configuration_manager;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;

import utils.StdOut;

public class XMLSerializer {
	
	public XMLSerializer(){};
	
	public <T extends Serializable> boolean serialize(T type, Path p) {
		
		try(FileOutputStream fileStream = new FileOutputStream(p.toFile())){
				XMLEncoder encoder = new XMLEncoder(fileStream);
			    encoder.writeObject(type);
			    encoder.close();
        }catch(Exception e){
            StdOut.println("Exception Occurred while Serializing, message is: "+ e.toString());

            return false;
        }

        return true;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Serializable> T deSerialize(T type, Path p){
		T returnObject = null;
		try(FileInputStream fileStream = new FileInputStream(p.toFile())){
	            XMLDecoder decoder = new XMLDecoder(fileStream);
				returnObject = (T)decoder.readObject();
				decoder.close();
	        }catch(IOException  e){
	            StdOut.println("Exception Occurred while De-serializing, message is "+ e.getMessage());
	        }

	        return returnObject;
	}
}
