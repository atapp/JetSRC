package configuration_manager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import stores.AGStore;
import stores.Store;

class XMLSerializerTest {
	
	@TempDir
    static Path sharedTempDir;

	Store store = new AGStore("test", 0.0, 0.0, 0.0, "test");
	Store store2 = new AGStore("test1", 0.0, 0.0, 0.0, "test");
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
