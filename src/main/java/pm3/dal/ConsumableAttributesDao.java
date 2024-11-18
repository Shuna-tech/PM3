package pm3.dal;

import pm3.model.ConsumableAttributes;
import pm3.model.Items;
import pm3.model.Attributes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConsumableAttributesDao {
    protected ConnectionManager connectionManager;
    private static ConsumableAttributesDao instance = null;
    private ItemsDao itemsDao;
    private AttributesDao attributesDao;
    
    protected ConsumableAttributesDao() {
        connectionManager = new ConnectionManager();
        itemsDao = ItemsDao.getInstance();
        attributesDao = AttributesDao.getInstance();
    }
    
    public static ConsumableAttributesDao getInstance() {
        if (instance == null) {
            instance = new ConsumableAttributesDao();
        }
        return instance;
    }
    
    public ConsumableAttributes create(ConsumableAttributes consumableAttr) throws SQLException {
        String insertConsumableAttr = 
            "INSERT INTO ConsumableAttributes(ItemID, AttributeID, AttributeBonusCap, AttributeBonusPercent) " +
            "VALUES(?,?,?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertConsumableAttr);
            // 将对象作为参数直接设置
            insertStmt.setInt(1, consumableAttr.getItem().getItemID());
            insertStmt.setInt(2, consumableAttr.getAttribute().getAttributeID());
            insertStmt.setInt(3, consumableAttr.getAttributeBonusCap());
            insertStmt.setDouble(4, consumableAttr.getAttributeBonusPercent());
            insertStmt.executeUpdate();
            
            return consumableAttr;
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
    
    public ConsumableAttributes getConsumableAttributeByItemIdAndAttributeId(int itemId, int attributeId) 
            throws SQLException {
        String selectConsumableAttr = 
            "SELECT ItemID, AttributeID, AttributeBonusCap, AttributeBonusPercent " +
            "FROM ConsumableAttributes WHERE ItemID=? AND AttributeID=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectConsumableAttr);
            selectStmt.setInt(1, itemId);
            selectStmt.setInt(2, attributeId);
            results = selectStmt.executeQuery();
            
            if (results.next()) {
                // 通过 ID 获取完整的 Items 和 Attributes 对象
                Items item = itemsDao.getItemById(itemId);
                Attributes attribute = attributesDao.getAttributeByAttributesID(attributeId);
                
                return new ConsumableAttributes(
                    item,
                    attribute,
                    results.getInt("AttributeBonusCap"),
                    results.getDouble("AttributeBonusPercent")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (selectStmt != null) {
                selectStmt.close();
            }
            if (results != null) {
                results.close();
            }
        }
        return null;
    }
    
    public List<ConsumableAttributes> getAttributesByItemId(int itemId) throws SQLException {
        List<ConsumableAttributes> attributes = new ArrayList<>();
        String selectAttributes = 
            "SELECT ItemID, AttributeID, AttributeBonusCap, AttributeBonusPercent " +
            "FROM ConsumableAttributes WHERE ItemID=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectAttributes);
            selectStmt.setInt(1, itemId);
            results = selectStmt.executeQuery();
            
            while (results.next()) {
                int attributeId = results.getInt("AttributeID");
                Items item = itemsDao.getItemById(itemId);
                Attributes attribute = attributesDao.getAttributeByAttributesID(attributeId);
                
                attributes.add(new ConsumableAttributes(
                    item,
                    attribute,
                    results.getInt("AttributeBonusCap"),
                    results.getDouble("AttributeBonusPercent")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (selectStmt != null) {
                selectStmt.close();
            }
            if (results != null) {
                results.close();
            }
        }
        return attributes;
    }
    
    public ConsumableAttributes updateConsumableAttribute(ConsumableAttributes consumableAttr) 
            throws SQLException {
        String updateConsumableAttr = 
            "UPDATE ConsumableAttributes SET AttributeBonusCap=?, AttributeBonusPercent=? " +
            "WHERE ItemID=? AND AttributeID=?;";
        Connection connection = null;
        PreparedStatement updateStmt = null;
        
        try {
            connection = connectionManager.getConnection();
            updateStmt = connection.prepareStatement(updateConsumableAttr);
            updateStmt.setInt(1, consumableAttr.getAttributeBonusCap());
            updateStmt.setDouble(2, consumableAttr.getAttributeBonusPercent());
            updateStmt.setInt(3, consumableAttr.getItem().getItemID());
            updateStmt.setInt(4, consumableAttr.getAttribute().getAttributeID());
            updateStmt.executeUpdate();
            
            return consumableAttr;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (updateStmt != null) {
                updateStmt.close();
            }
        }
    }
    
    public void delete(ConsumableAttributes consumableAttr) throws SQLException {
        String deleteConsumableAttr = 
            "DELETE FROM ConsumableAttributes WHERE ItemID=? AND AttributeID=?;";
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deleteConsumableAttr);
            deleteStmt.setObject(1, consumableAttr.getItem());
            deleteStmt.setObject(2, consumableAttr.getAttribute());
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (deleteStmt != null) {
                deleteStmt.close();
            }
        }
    }
}