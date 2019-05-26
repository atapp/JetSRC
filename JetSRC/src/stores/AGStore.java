package stores;

public class AGStore extends Store implements Jettisonable, Releasable {
	
	private String type = "A/G Store";
	private Double jettisonLimitDouble;
	private Double releaseLimitDouble;
	private String seperationString;
	
	public AGStore(String name, Double carriageLimit, Double releaseLimit, Double jettisonLimit, String seperation) {
		super(name, carriageLimit);
		this.jettisonLimitDouble = jettisonLimit;
		this.releaseLimitDouble = releaseLimit;
		this.seperationString = seperation;
	}

	@Override
	public Double getReleaseLimit() {
		// TODO Auto-generated method stub
		return releaseLimitDouble;
	}

	@Override
	public Double getJettisonLimit() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return type;
	}
	
	public String getSeperationString() {
		return this.seperationString;
	}
	

}
