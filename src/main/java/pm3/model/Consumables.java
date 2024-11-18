package pm3.model;

public class Consumables {
    protected int itemID;
    protected int itemLevel;
    protected String description;

    public Consumables(int itemID, int itemLevel, String description) {
        this.itemID = itemID;
        this.itemLevel = itemLevel;
        this.description = description;
    }

    public int getItemID() { return itemID; }

    public int getItemLevel() { return itemLevel; }
    public void setItemLevel(int itemLevel) { this.itemLevel = itemLevel; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}