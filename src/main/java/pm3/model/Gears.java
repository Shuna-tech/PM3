package pm3.model;

public class Gears extends EquippableItems {
    protected int defenseRating;
    protected int magicDefenseRating;
    
    // Constructor for new gears with slot as EquipmentSlots
    public Gears(String itemName, int maxStackSize, boolean marketAllowed, int vendorPrice,
                 int itemLevel, EquipmentSlots slot, int requiredLevel,
                 int defenseRating, int magicDefenseRating) {
        super(itemName, maxStackSize, marketAllowed, vendorPrice, itemLevel, slot, requiredLevel);
        this.defenseRating = defenseRating;
        this.magicDefenseRating = magicDefenseRating;
    }

    // Constructor for existing gears with itemID and slot
    public Gears(int itemID, String itemName, int maxStackSize, boolean marketAllowed, int vendorPrice,
                 int itemLevel, EquipmentSlots slot, int requiredLevel,
                 int defenseRating, int magicDefenseRating) {
        super(itemID, itemName, maxStackSize, marketAllowed, vendorPrice, itemLevel, slot, requiredLevel);
        this.defenseRating = defenseRating;
        this.magicDefenseRating = magicDefenseRating;
    }
    
    public int getDefenseRating() { return defenseRating; }
    public void setDefenseRating(int defenseRating) { this.defenseRating = defenseRating; }
    
    public int getMagicDefenseRating() { return magicDefenseRating; }
    public void setMagicDefenseRating(int magicDefenseRating) { this.magicDefenseRating = magicDefenseRating; }
 
}