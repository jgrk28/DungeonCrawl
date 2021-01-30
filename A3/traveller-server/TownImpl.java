
public class TownImpl implements Town {
	String name;
	
	public TownImpl(String name) {
		this.name = name;
	}

	@Override
	public Boolean checkTownName(String name) {
		return name.equals(this.name);
	}
}
