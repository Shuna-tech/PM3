package pm3.model;

public class CharacterEquipments {
    protected CharacterInfo character;
    protected EquipmentSlots slot;
    protected EquippableItems item;

    // Constructor with item
    public CharacterEquipments(CharacterInfo character, EquipmentSlots slot, EquippableItems item) {
        this.character = character;
        this.slot = slot;
        this.item = item;
    }

    // Constructor without item (useful when a slot is empty)
    public CharacterEquipments(CharacterInfo character, EquipmentSlots slot) {
        this.character = character;
        this.slot = slot;
        this.item = null;
    }

    // Getters and Setters
    public CharacterInfo getCharacter() {
        return character;
    }

    public void setCharacter(CharacterInfo character) {
        this.character = character;
    }

    public EquipmentSlots getSlot() {
        return slot;
    }

    public void setSlot(EquipmentSlots slot) {
        this.slot = slot;
    }

    public EquippableItems getItem() {
        return item;
    }

    public void setItem(EquippableItems item) {
        this.item = item;
    }
}
