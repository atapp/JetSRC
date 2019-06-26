package aircrafts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import stores.Store;

public class Pylon implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int location; // location on aircraft
	public HashMap<Integer, String> approvedStores = new HashMap<>();
	//public ArrayList<Store> approvedStoresStores = new ArrayList<>();
	private ArrayList<Store> stores = new ArrayList<>(); // stores on pylon
	private int capacity; // pylon capacity
	
	public Pylon(int location, int capacity) {
		//super();
		this.location = location;
		this.capacity = capacity;
	}
	
	public Pylon() {};

	public ArrayList<Store> getStores() {
		return stores;
	}

	public void addStores(Store store) {
		this.stores.add(store);
	}
	
	public void removeStores() {
		this.stores.clear();
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public void setStores(ArrayList<Store> stores) {
		this.stores = stores;
	}
	
	public boolean isEmpty() {
		return stores.isEmpty();
	}
	
}
