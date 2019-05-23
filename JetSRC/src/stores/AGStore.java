package stores;

public class AGStore extends Store implements Jettisonable, Releasable {
	
	private String type = "A/G Store";
	
	public AGStore(String name, Double releaseLimit) {
		super(name, releaseLimit);
	}

	@Override
	public Double getReleaseLimit() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return type;
	}

}
