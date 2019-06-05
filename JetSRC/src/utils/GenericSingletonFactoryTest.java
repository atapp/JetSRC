package utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import aircrafts.Aircraft;

class GenericSingletonFactoryTest {

	@Test
	void testGetInstance() {
		Aircraft aircrzft = GenericSingletonFactory.getInstance(Aircraft.class);
		assertTrue(aircrzft.getClass() == Aircraft.class);
		
	}

}
