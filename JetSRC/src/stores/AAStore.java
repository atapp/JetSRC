package stores;

public class AAStore extends Store implements Jettisonable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5L;
	private String type = "A/A Store";
	private Double jettisonLimitDouble;
	
	public AAStore(String name, Double carriageLimit, Double jettisonLimit) {
		super(name, carriageLimit);
		this.jettisonLimitDouble = jettisonLimit;
	}
	
	public AAStore() {};

	@Override
	public Double getJettisonLimit() {
		return jettisonLimitDouble;
	}

	@Override
	public String[] approvedAircraft() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] approvedStations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		return type;
	}

	public Double getJettisonLimitDouble() {
		return jettisonLimitDouble;
	}

	public void setJettisonLimitDouble(Double jettisonLimitDouble) {
		this.jettisonLimitDouble = jettisonLimitDouble;
	}

	public void setType(String type) {
		this.type = type;
	}

}
