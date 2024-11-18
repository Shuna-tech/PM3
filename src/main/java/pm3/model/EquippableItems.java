package pm3.model;

public class EquippableItems extends Items {
    protected int itemLevel;
    protected EquipmentSlots slot;
    protected int requiredLevel;

    // Constructor for new equippable items
    public EquippableItems(String itemName, int maxStackSize, boolean marketAllowed, int vendorPrice,
                          int itemLevel, EquipmentSlots slot, int requiredLevel) {
        super(itemName, maxStackSize, marketAllowed, vendorPrice);
        this.itemLevel = itemLevel;
        this.slot = slot;
        this.requiredLevel = requiredLevel;
    }

    // Constructor for existing equippable items
    public EquippableItems(int itemID, String itemName, int maxStackSize, boolean marketAllowed, int vendorPrice,
                          int itemLevel, EquipmentSlots slot, int requiredLevel) {
        super(itemID, itemName, maxStackSize, marketAllowed, vendorPrice);
        this.itemLevel = itemLevel;
        this.slot = slot;
        this.requiredLevel = requiredLevel;
    }

    public int getItemLevel() { 
        return itemLevel; 
    }
    
    public void setItemLevel(int itemLevel) { 
        this.itemLevel = itemLevel; 
    }

    public EquipmentSlots getSlot() {
        return slot;
    }

    public void setSlot(EquipmentSlots slot) {
        this.slot = slot;
    }

    public int getRequiredLevel() { 
        return requiredLevel; 
    }
    
    public void setRequiredLevel(int requiredLevel) { 
        this.requiredLevel = requiredLevel; 
    }
}