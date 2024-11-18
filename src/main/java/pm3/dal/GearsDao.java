package pm3.dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import pm3.model.Gears;
import pm3.model.Items;
import pm3.model.EquippableItems;
import pm3.model.EquipmentSlots;

public class GearsDao {
    protected ConnectionManager connectionManager;
    private static GearsDao instance = null;
    
    protected GearsDao() {
        connectionManager = new ConnectionManager();
    }
    
    public static GearsDao getInstance() {
        if (instance == null) {
            instance = new GearsDao();
        }
        return instance;
    }
    
    public Gears create(Gears gear) throws SQLException {
        // First create an entry in Items, then EquippableItems
        EquippableItems equippableItem = EquippableItemsDao.getInstance().create(new EquippableItems(
            gear.getItemName(),
            gear.getMaxStackSize(),
            gear.isMarketAllowed(),
            gear.getVendorPrice(),
            gear.getItemLevel(),
            gear.getSlot(),
            gear.getRequiredLevel()
        ));

        String insertGear = "INSERT INTO Gears(ItemID, DefenseRating, MagicDefenseRating) VALUES (?, ?, ?)";

        Connection connection = null;
        PreparedStatement gearStmt = null;

        try {
            connection = connectionManager.getConnection();
            
            // Insert into Gears
            gearStmt = connection.prepareStatement(insertGear);
            gearStmt.setInt(1, equippableItem.getItemID());
            gearStmt.setInt(2, gear.getDefenseRating());
            gearStmt.setInt(3, gear.getMagicDefenseRating());
            gearStmt.executeUpdate();

            // Return the combined Gear object
            return new Gears(
                equippableItem.getItemID(),
                equippableItem.getItemName(),
                equippableItem.getMaxStackSize(),
                equippableItem.isMarketAllowed(),
                equippableItem.getVendorPrice(),
                equippableItem.getItemLevel(),
                equippableItem.getSlot(),
                equippableItem.getRequiredLevel(),
                gear.getDefenseRating(),
                gear.getMagicDefenseRating()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) connection.close();
            if (gearStmt != null) gearStmt.close();
        }
    }

    public List<Gears> getGearByPartialName(String name) throws SQLException {
        List<Gears> gearsList = new ArrayList<>();
        String selectGears = 
            "SELECT i.ItemID, i.ItemName, i.MaxStackSize, i.MarketAllowed, i.VendorPrice, " +
            "e.ItemLevel, e.SlotID, e.RequiredLevel, " +
            "g.DefenseRating, g.MagicDefenseRating " +
            "FROM Items i " +
            "JOIN EquippableItems e ON i.ItemID = e.ItemID " +
            "JOIN Gears g ON e.ItemID = g.ItemID " +
            "WHERE i.ItemName LIKE ?";
            
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectGears);
            selectStmt.setString(1, "%" + name + "%");
            results = selectStmt.executeQuery();
            
            while (results.next()) {
                // Get the EquipmentSlots object
                EquipmentSlots slot = EquipmentSlotsDao.getInstance().getSlotById(results.getInt("SlotID"));
                
                Gears gear = new Gears(
                    results.getInt("ItemID"),
                    results.getString("ItemName"),
                    results.getInt("MaxStackSize"),
                    results.getBoolean("MarketAllowed"),
                    results.getInt("VendorPrice"),
                    results.getInt("ItemLevel"),
                    slot,
                    results.getInt("RequiredLevel"),
                    results.getInt("DefenseRating"),
                    results.getInt("MagicDefenseRating")
                );
                gearsList.add(gear);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (results != null) results.close();
            if (selectStmt != null) selectStmt.close();
            if (connection != null) connection.close();
        }
        return gearsList;
    }
    
    public void delete(Gears gearItem) throws SQLException {
        String deleteGear = "DELETE FROM Gears WHERE ItemID = ?";
        String deleteEquippable = "DELETE FROM EquippableItems WHERE ItemID = ?";
        String deleteItem = "DELETE FROM Items WHERE ItemID = ?";
        
        Connection connection = null;
        PreparedStatement gearStmt = null;
        PreparedStatement equippableStmt = null;
        PreparedStatement itemStmt = null;
        
        try {
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);  
            
            // Delete from Gears
            gearStmt = connection.prepareStatement(deleteGear);
            gearStmt.setInt(1, gearItem.getItemID());
            gearStmt.executeUpdate();
            
            // Delete from EquippableItems
            equippableStmt = connection.prepareStatement(deleteEquippable);
            equippableStmt.setInt(1, gearItem.getItemID());
            equippableStmt.executeUpdate();
            
            // Delete from Items
            itemStmt = connection.prepareStatement(deleteItem);
            itemStmt.setInt(1, gearItem.getItemID());
            itemStmt.executeUpdate();
            
            connection.commit();  
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();  
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (gearStmt != null) gearStmt.close();
            if (equippableStmt != null) equippableStmt.close();
            if (itemStmt != null) itemStmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);  
                connection.close();
            }
        }
    }
}