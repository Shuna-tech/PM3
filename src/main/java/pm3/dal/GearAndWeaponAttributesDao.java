package pm3.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pm3.model.GearAndWeaponAttributes;
import pm3.model.Items;
import pm3.model.Attributes;

public class GearAndWeaponAttributesDao {
    protected ConnectionManager connectionManager;
    private static GearAndWeaponAttributesDao instance = null;
    protected ItemsDao itemsDao;         // Add reference to ItemsDao
    protected AttributesDao attributesDao;  // Add reference to AttributesDao
    
    protected GearAndWeaponAttributesDao() {
        connectionManager = new ConnectionManager();
        itemsDao = ItemsDao.getInstance();         // Initialize ItemsDao
        attributesDao = AttributesDao.getInstance(); // Initialize AttributesDao
    }
    
    public static GearAndWeaponAttributesDao getInstance() {
        if (instance == null) {
            instance = new GearAndWeaponAttributesDao();
        }
        return instance;
    }
    
    public GearAndWeaponAttributes create(GearAndWeaponAttributes gearAttr) throws SQLException {
        String insertGearAttr = "INSERT INTO GearAndWeaponAttributes(ItemID, AttributeID, AttributeBonus) VALUES(?,?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertGearAttr);
            
            // Get IDs from the Item and Attribute objects
            insertStmt.setInt(1, gearAttr.getItem().getItemID());
            insertStmt.setInt(2, gearAttr.getAttribute().getAttributeID());
            insertStmt.setInt(3, gearAttr.getAttributeBonus());
            
            insertStmt.executeUpdate();
            
            // Retrieve the created object with full Item and Attribute objects
            return new GearAndWeaponAttributes(
                gearAttr.getItem(),
                gearAttr.getAttribute(),
                gearAttr.getAttributeBonus()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (insertStmt != null) {
                insertStmt.close();
            }
        }
    }
    
    // Add a method to read GearAndWeaponAttributes
    public GearAndWeaponAttributes getGearAttributeByIds(int itemId, int attributeId) throws SQLException {
        String selectGearAttr = 
            "SELECT ItemID, AttributeID, AttributeBonus " +
            "FROM GearAndWeaponAttributes " +
            "WHERE ItemID=? AND AttributeID=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectGearAttr);
            selectStmt.setInt(1, itemId);
            selectStmt.setInt(2, attributeId);
            results = selectStmt.executeQuery();
            
            if (results.next()) {
                // Fetch the full Item and Attribute objects using their respective DAOs
                Items item = itemsDao.getItemById(itemId);
                Attributes attribute = attributesDao.getAttributeByAttributesID(attributeId);
                int attributeBonus = results.getInt("AttributeBonus");
                
                return new GearAndWeaponAttributes(
                    item,
                    attribute,
                    attributeBonus
                );
            }
            return null;
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (results != null) {
                results.close();
            }
            if (selectStmt != null) {
                selectStmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
    
    
}