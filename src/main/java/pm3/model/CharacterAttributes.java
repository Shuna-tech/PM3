package pm3.model;

public class CharacterAttributes {
	protected Attributes attributes;
	protected CharacterInfo characterInfo;
	protected int attributeValue;
	
	public CharacterAttributes(Attributes attributes, CharacterInfo characterInfo, int attributeValue) {
		super();
		this.attributes = attributes;
		this.characterInfo = characterInfo;
		this.attributeValue = attributeValue;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public CharacterInfo getCharacterInfo() {
		return characterInfo;
	}

	public void setCharacterInfo(CharacterInfo characterInfo) {
		this.characterInfo = characterInfo;
	}

	public int getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(int attributeValue) {
		this.attributeValue = attributeValue;
	}
	
	
	
	
	

}