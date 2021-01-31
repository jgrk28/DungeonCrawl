package traveller_server;

import java.util.Objects;

public class CharacterImpl implements Character {
	String name;
	
	public CharacterImpl(String name) {
		this.name = name;
	}

	@Override
	public Boolean checkCharacterName(String name) {
		return name.equals(this.name);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Character)) {
			return false;
		}
		Character otherCharacter = (Character) obj;
		return otherCharacter.checkCharacterName(this.name);	
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}
}
