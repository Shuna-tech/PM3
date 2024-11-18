package pm3.model;

public class InventoryPositions {
	protected CharacterInfo characterInfo;
	protected int stackPosition;
	protected Items items;
	protected int stackSize;
	
	public InventoryPositions(CharacterInfo characterInfo, int stackPosition, Items items, int stackSize) {
		super();
		this.characterInfo = characterInfo;
		this.stackPosition = stackPosition;
		this.items = items;
		this.stackSize = stackSize;
	}

	public CharacterInfo getCharacterInfo() {
		return characterInfo;
	}

	public void setCharacterInfo(CharacterInfo characterInfo) {
		this.characterInfo = characterInfo;
	}

	public int getStackPosition() {
		return stackPosition;
	}

	public void setStackPosition(int stackPosition) {
		this.stackPosition = stackPosition;
	}

	public Items getItems() {
		return items;
	}

	public void setItems(Items items) {
		this.items = items;
	}

	public int getStackSize() {
		return stackSize;
	}

	public void setStackSize(int stackSize) {
		this.stackSize = stackSize;
	}
	

	
	

}