package aircrafts;

import java.util.ArrayList;

import stores.Store;

class Pylon {
	public int location; // location on aircraft
	private ArrayList<Store> stores = new ArrayList<>(); // stores on pylon
	private int capacity; // pylon capacity
	
	public Pylon(int location, int capacity) {
		super();
		this.location = location;
		this.capacity = capacity;
	}

	public ArrayList<Store> getStores() {
		return stores;
	}

	public void addStores(Store store) {
		this.stores.add(store);
	}
	
	public void removeStores() {
		this.stores.clear();
	}
}
