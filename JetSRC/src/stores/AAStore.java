package stores;

public class AAStore extends Store implements Jettisonable {
	
	private String type = "A/A Store";
	private Double jettisonLimitDouble;
	
	public AAStore(String name, Double carriageLimit, Double jettisonLimit) {
		super(name, carriageLimit);
		this.jettisonLimitDouble = jettisonLimit;
	}

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

}
