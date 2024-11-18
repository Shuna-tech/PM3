package pm3.model;

public class ConsumableAttributes {
    protected Items item;
    protected Attributes attribute;
    protected int attributeBonusCap;
    protected double attributeBonusPercent;

    public ConsumableAttributes(Items item, Attributes attribute, int attributeBonusCap, double attributeBonusPercent) {
        this.item = item;
        this.attribute = attribute;
        this.attributeBonusCap = attributeBonusCap;
        this.attributeBonusPercent = attributeBonusPercent;
    }

    public Items getItem() { return item; }


    public Attributes getAttribute() { return attribute; }


    public int getAttributeBonusCap() { return attributeBonusCap; }
    public void setAttributeBonusCap(int attributeBonusCap) { this.attributeBonusCap = attributeBonusCap; }

    public double getAttributeBonusPercent() { return attributeBonusPercent; }
    public void setAttributeBonusPercent(double attributeBonusPercent) { this.attributeBonusPercent = attributeBonusPercent; }
}