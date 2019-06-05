/**
 * 
 */
package stores;

import java.io.Serializable;

/**
 * To represent a aircraft store
 * @author simonhogg
 *
 */
public abstract class Store implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	protected String name = "Generic Store";
	private Double carriageLimit;
	
	public Store() {
	}
	
	public Store(String name, Double carriageLimit) {
		this.name = name;
		this.carriageLimit = carriageLimit;
	}
	
	public void displayName() {
		System.out.println(name);
	}
	
	public abstract String[] approvedAircraft();
	public abstract int[] approvedStations();

	public Double getCarriageLimit() {
		return carriageLimit;
	}
	
	public abstract String getType();

	public void setCarriageLimit(Double carriageLimit) {
		this.carriageLimit = carriageLimit;
	}
	
	public String toString() {
		return this.name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
