package stores;

public class AAStore extends Store implements Jettisonable {
	
	private String type = "A/A Store";
	
	public AAStore(String name, Double releaseLimit) {
		super(name, releaseLimit);
	}

	@Override
	public Double getJettisonLimit() {
		// TODO Auto-generated method stub
		return null;
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
