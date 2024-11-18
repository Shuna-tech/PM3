package pm3.dal;

import pm3.model.Weapons;
import pm3.model.EquippableItems;
import pm3.model.EquipmentSlots;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WeaponsDao {
    protected ConnectionManager connectionManager;
    private static WeaponsDao instance = null;

    protected WeaponsDao() {
        connectionManager = new ConnectionManager();
    }

    public static WeaponsDao getInstance() {
        if (instance == null) {
            instance = new WeaponsDao();
        }
        return instance;
    }

    public Weapons create(Weapons weapon) throws SQLException {
        // Step 1: Create an entry in the EquippableItems table
        EquippableItems equippableItem = EquippableItemsDao.getInstance().create(new EquippableItems(
            weapon.getItemName(),
            weapon.getMaxStackSize(),
            weapon.isMarketAllowed(),
            weapon.getVendorPrice(),
            weapon.getItemLevel(),
            weapon.getSlot(),
            weapon.getRequiredLevel()
        ));

        // Step 2: Create an entry in the Weapons table
        String insertWeapon = "INSERT INTO Weapons(ItemID, PhysicalDamage, AutoAttack, AttackDelay) " +
                            "VALUES (?, ?, ?, ?);";
        Connection connection = null;
        PreparedStatement weaponStmt = null;

        try {
            connection = connectionManager.getConnection();

            // Insert into Weapons table
            weaponStmt = connection.prepareStatement(insertWeapon);
            weaponStmt.setInt(1, equippableItem.getItemID());
            weaponStmt.setInt(2, weapon.getPhysicalDamage());
            weaponStmt.setDouble(3, weapon.getAutoAttack());
            weaponStmt.setDouble(4, weapon.getAttackDelay());
            weaponStmt.executeUpdate();

            // Return the combined Weapons object
            return new Weapons(
                equippableItem.getItemID(),
                equippableItem.getItemName(),
                equippableItem.getMaxStackSize(),
                equippableItem.isMarketAllowed(),
                equippableItem.getVendorPrice(),
                equippableItem.getItemLevel(),
                equippableItem.getSlot(),
                equippableItem.getRequiredLevel(),
                weapon.getPhysicalDamage(),
                weapon.getAutoAttack(),
                weapon.getAttackDelay()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) connection.close();
            if (weaponStmt != null) weaponStmt.close();
        }
    }

    public Weapons getWeaponByItemID(int itemID) throws SQLException {
        String query = 
            "SELECT i.ItemID, i.ItemName, i.MaxStackSize, i.MarketAllowed, i.VendorPrice, " +
            "e.ItemLevel, e.SlotID, e.RequiredLevel, " +
            "w.PhysicalDamage, w.AutoAttack, w.AttackDelay " +
            "FROM Items i " +
            "JOIN EquippableItems e ON i.ItemID = e.ItemID " +
            "JOIN Weapons w ON e.ItemID = w.ItemID " +
            "WHERE i.ItemID = ?";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionManager.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, itemID);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Get the EquipmentSlots object
                EquipmentSlots slot = EquipmentSlotsDao.getInstance().getSlotById(resultSet.getInt("SlotID"));
                
                return new Weapons(
                    resultSet.getInt("ItemID"),
                    resultSet.getString("ItemName"),
                    resultSet.getInt("MaxStackSize"),
                    resultSet.getBoolean("MarketAllowed"),
                    resultSet.getInt("VendorPrice"),
                    resultSet.getInt("ItemLevel"),
                    slot,
                    resultSet.getInt("RequiredLevel"),
                    resultSet.getInt("PhysicalDamage"),
                    resultSet.getDouble("AutoAttack"),
                    resultSet.getDouble("AttackDelay")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
        return null;
    }

    public List<Weapons> getAllWeapons() throws SQLException {
        List<Weapons> weaponsList = new ArrayList<>();
        String query = 
            "SELECT i.ItemID, i.ItemName, i.MaxStackSize, i.MarketAllowed, i.VendorPrice, " +
            "e.ItemLevel, e.SlotID, e.RequiredLevel, " +
            "w.PhysicalDamage, w.AutoAttack, w.AttackDelay " +
            "FROM Items i " +
            "JOIN EquippableItems e ON i.ItemID = e.ItemID " +
            "JOIN Weapons w ON e.ItemID = w.ItemID";
        
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                // Get the EquipmentSlots object
                EquipmentSlots slot = EquipmentSlotsDao.getInstance().getSlotById(resultSet.getInt("SlotID"));
                
                Weapons weapon = new Weapons(
                    resultSet.getInt("ItemID"),
                    resultSet.getString("ItemName"),
                    resultSet.getInt("MaxStackSize"),
                    resultSet.getBoolean("MarketAllowed"),
                    resultSet.getInt("VendorPrice"),
                    resultSet.getInt("ItemLevel"),
                    slot,
                    resultSet.getInt("RequiredLevel"),
                    resultSet.getInt("PhysicalDamage"),
                    resultSet.getDouble("AutoAttack"),
                    resultSet.getDouble("AttackDelay")
                );
                weaponsList.add(weapon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
        return weaponsList;
    }

    public boolean update(Weapons weapon) throws SQLException {
        String updateItems = "UPDATE Items SET ItemName = ?, MaxStackSize = ?, MarketAllowed = ?, VendorPrice = ? WHERE ItemID = ?";
        String updateEquippableItems = "UPDATE EquippableItems SET ItemLevel = ?, SlotID = ?, RequiredLevel = ? WHERE ItemID = ?";
        String updateWeapons = "UPDATE Weapons SET PhysicalDamage = ?, AutoAttack = ?, AttackDelay = ? WHERE ItemID = ?";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement itemsStmt = connection.prepareStatement(updateItems);
                 PreparedStatement equippableStmt = connection.prepareStatement(updateEquippableItems);
                 PreparedStatement weaponStmt = connection.prepareStatement(updateWeapons)) {
                
                // Update Items
                itemsStmt.setString(1, weapon.getItemName());
                itemsStmt.setInt(2, weapon.getMaxStackSize());
                itemsStmt.setBoolean(3, weapon.isMarketAllowed());
                itemsStmt.setInt(4, weapon.getVendorPrice());
                itemsStmt.setInt(5, weapon.getItemID());
                itemsStmt.executeUpdate();

                // Update EquippableItems
                equippableStmt.setInt(1, weapon.getItemLevel());
                equippableStmt.setInt(2, weapon.getSlot().getSlotID());  // Get SlotID from EquipmentSlots object
                equippableStmt.setInt(3, weapon.getRequiredLevel());
                equippableStmt.setInt(4, weapon.getItemID());
                equippableStmt.executeUpdate();

                // Update Weapons
                weaponStmt.setInt(1, weapon.getPhysicalDamage());
                weaponStmt.setDouble(2, weapon.getAutoAttack());
                weaponStmt.setDouble(3, weapon.getAttackDelay());
                weaponStmt.setInt(4, weapon.getItemID());
                weaponStmt.executeUpdate();

                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    public boolean delete(int itemID) throws SQLException {
        String deleteWeapons = "DELETE FROM Weapons WHERE ItemID = ?";
        String deleteEquippableItems = "DELETE FROM EquippableItems WHERE ItemID = ?";
        String deleteItems = "DELETE FROM Items WHERE ItemID = ?";

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement weaponStmt = connection.prepareStatement(deleteWeapons);
                 PreparedStatement equippableStmt = connection.prepareStatement(deleteEquippableItems);
                 PreparedStatement itemsStmt = connection.prepareStatement(deleteItems)) {
                
                weaponStmt.setInt(1, itemID);
                weaponStmt.executeUpdate();

                equippableStmt.setInt(1, itemID);
                equippableStmt.executeUpdate();

                itemsStmt.setInt(1, itemID);
                itemsStmt.executeUpdate();

                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }
}