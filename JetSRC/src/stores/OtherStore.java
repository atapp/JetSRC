package stores;

public class OtherStore extends Store {
	
	private String type = "Other Store";
	
	public OtherStore(String name, Double releaseLimit) {
		super(name, releaseLimit);
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
