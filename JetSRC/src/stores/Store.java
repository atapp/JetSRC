/**
 * 
 */
package stores;

/**
 * To represent a aircraft store
 * @author simonhogg
 *
 */
public abstract class Store {
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
}
