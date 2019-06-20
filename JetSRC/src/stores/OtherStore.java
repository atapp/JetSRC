package stores;

public class OtherStore extends Store {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7400520617158230457L;
	private String type = "Other Store";
	
	public OtherStore(Integer storeCode, String code, String name, Double releaseLimit) {
		super(storeCode, code, name, releaseLimit);
	}
	
	public OtherStore() {};
	
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
