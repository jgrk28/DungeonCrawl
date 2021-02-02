package traveller_server;

import java.util.Objects;

/**
 * This class represents a character
 * Overrides equals and hashcode
 */
public class CharacterImpl implements Character {
	String name;
	
	/**
	 * Constructs a new character
	 * @param name - the name of the character
	 */
	public CharacterImpl(String name) {
		this.name = name;
	}

	/**
	 * Returns true if the given Character has the same name as this Character
	 * @param name - the name of the given Character
	 * @return true if the character name is the same, false otherwise
	 */
	private Boolean checkCharacterName(String name) {
		return name.equals(this.name);
	}
	
	/**
	 * Overrides the equals method
	 * @param obj - the given object
	 * @return true if the Characters are the same Character, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Character)) {
			return false;
		}
		CharacterImpl otherCharacter = (CharacterImpl) obj;
		return otherCharacter.checkCharacterName(this.name);	
	}
	
	/**
	 * Overrides the hashcode method
	 * @return the corresponding hashcode
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}
}
