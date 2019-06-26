package configuration_manager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import stores.AGStore;
import stores.Store;

class XMLSerializerTest {
	
	@TempDir
    static Path sharedTempDir;

	Store store = new AGStore(1, "1|AIM-9M|A-A|1.8,0,0,0","test", 1.1, 1.1, 1.1, "2G to 45");
	Store store2 = new AGStore(1, "1|AIM-9M|A-A|1.8,0,0,0","test", 1.1, 1.1, 1.1, "2G to 45");
	XMLSerializer xmsSerializer = new XMLSerializer();

	
	@Test
	void testSerialize() throws IOException{
		//File createdFile= sharedTempDir.toFile();
		Path createdFilePath = sharedTempDir.resolve("test.xml");
		boolean returnBoolean = xmsSerializer.serialize(store, createdFilePath);
		assertTrue(returnBoolean);
	}

	@Test
	void testDeSerialize() throws IOException {
		Path createdFilePath = sharedTempDir.resolve("test2.xml");
		boolean returnBoolean = xmsSerializer.serialize(store2, createdFilePath);
		assertTrue(returnBoolean);
		Store store3 = xmsSerializer.deSerialize(store2, createdFilePath);
		assertTrue(store3.getName().matches("test1"));
	}
}
