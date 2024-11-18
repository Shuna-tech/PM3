package pm3.dal;

import pm3.model.EquipmentSlots;
import pm3.model.EquippableItems;
import pm3.model.Items;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquippableItemsDao {
    protected ConnectionManager connectionManager;
    private static EquippableItemsDao instance = null;

    protected EquippableItemsDao() {
        connectionManager = new ConnectionManager();
    }

    public static EquippableItemsDao getInstance() {
        if (instance == null) {
            instance = new EquippableItemsDao();
        }
        return instance;
    }

    public EquippableItems create(EquippableItems equippableItem) throws SQLException {
      
        if (equippableItem.getSlot() == null) {
            throw new SQLException("Equipment slot cannot be null");
        }

        // Step 1: Create an entry in the Items table
        Items item = ItemsDao.getInstance().create(new Items(
            equippableItem.getItemName(),
            equippableItem.getMaxStackSize(),
            equippableItem.isMarketAllowed(),
            equippableItem.getVendorPrice()
        ));

        // Step 2: Create an entry in the EquippableItems table
        String insertEquippableQuery = "INSERT INTO EquippableItems(ItemID, ItemLevel, SlotID, RequiredLevel) " +
                                     "VALUES (?, ?, ?, ?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;

        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertEquippableQuery);
            
            insertStmt.setInt(1, item.getItemID());
            insertStmt.setInt(2, equippableItem.getItemLevel());
            insertStmt.setInt(3, equippableItem.getSlot().getSlotID());  
            insertStmt.setInt(4, equippableItem.getRequiredLevel());
            
            insertStmt.executeUpdate();

            return new EquippableItems(
                item.getItemID(),
                item.getItemName(),
                item.getMaxStackSize(),
                item.isMarketAllowed(),
                item.getVendorPrice(),
                equippableItem.getItemLevel(),
                equippableItem.getSlot(),
                equippableItem.getRequiredLevel()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) connection.close();
            if (insertStmt != null) insertStmt.close();
        }
    }

    public EquippableItems getById(int itemID) throws SQLException {
        String selectEquippableItem = 
            "SELECT i.*, e.ItemLevel, e.SlotID, e.RequiredLevel, s.* " +
            "FROM Items i " +
            "INNER JOIN EquippableItems e ON i.ItemID = e.ItemID " +
            "INNER JOIN EquipmentSlots s ON e.SlotID = s.SlotID " +
            "WHERE i.ItemID = ?;";
            
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;

        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectEquippableItem);
            selectStmt.setInt(1, itemID);
            results = selectStmt.executeQuery();

            if (results.next()) {
                // Get the slot first
                EquipmentSlots slot = EquipmentSlotsDao.getInstance().getSlotById(results.getInt("SlotID"));
                
                return new EquippableItems(
                    results.getInt("ItemID"),
                    results.getString("ItemName"),
                    results.getInt("MaxStackSize"),
                    results.getBoolean("MarketAllowed"),
                    results.getInt("VendorPrice"),
                    results.getInt("ItemLevel"),
                    slot,
                    results.getInt("RequiredLevel")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) connection.close();
            if (selectStmt != null) selectStmt.close();
            if (results != null) results.close();
        }
        return null;
    }
}