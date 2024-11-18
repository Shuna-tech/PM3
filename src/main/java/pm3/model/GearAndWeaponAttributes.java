package pm3.model;

public class GearAndWeaponAttributes {
    protected Items item;
    protected Attributes attribute;
    protected int attributeBonus;

    public GearAndWeaponAttributes(Items item, Attributes attribute, int attributeBonus) {
        this.item = item;
        this.attribute = attribute;
        this.attributeBonus = attributeBonus;
    }

    public Items getItem() { return item; }


    public Attributes getAttribute() { return attribute; }


    public int getAttributeBonus() { return attributeBonus; }
    public void setAttributeBonus(int attributeBonus) { this.attributeBonus = attributeBonus; }
}