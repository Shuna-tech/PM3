package pm3.model;

public class Attributes {
    protected int attributeID;
    protected String attributeName;

    public Attributes(int attributeID, String attributeName) {
        this.attributeID = attributeID;
        this.attributeName = attributeName;
    }

    public Attributes(String attributeName) {
        this.attributeName = attributeName;
    }

	public int getAttributeID() { return attributeID; }
  

    public String getAttributeName() { return attributeName; }
    public void setAttributeName(String attributeName) { this.attributeName = attributeName; }
}