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
	public String storeCodeString;
	public Integer storeCodeInteger;
	
	public Store() {
	}
	
	public Store(Integer storeInteger, String code, String name, Double carriageLimit) {
		this.storeCodeInteger = storeInteger;
		this.storeCodeString = code;
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

	public String getStoreCodeString() {
		return storeCodeString;
	}

	public void setConfigCodeString(String storeCodeString) {
		this.storeCodeString = storeCodeString;
	}

	public Integer getStoreCodeInteger() {
		return storeCodeInteger;
	}

	public void setStoreCodeInteger(Integer storeCodeInteger) {
		this.storeCodeInteger = storeCodeInteger;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.name == ((Store) obj).name;
    }

    @Override
    public int hashCode() {
        return 7 + 5*storeCodeInteger; // 5 and 7 are random prime numbers
    }
	
}
