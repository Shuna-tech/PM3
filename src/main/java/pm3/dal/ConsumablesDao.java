package pm3.dal;

import pm3.model.Consumables;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConsumablesDao {
    protected ConnectionManager connectionManager;
    private static ConsumablesDao instance = null;
    
    protected ConsumablesDao() {
        connectionManager = new ConnectionManager();
    }
    
    public static ConsumablesDao getInstance() {
        if (instance == null) {
            instance = new ConsumablesDao();
        }
        return instance;
    }
    
    /**
     * Create a new Consumable item in the Consumables table.
     * Note: The corresponding Items record should already exist.
     */
    public Consumables create(Consumables consumable) throws SQLException {
        String insertConsumable = "INSERT INTO Consumables(ItemID, ItemLevel, Description) VALUES(?,?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertConsumable);
            insertStmt.setInt(1, consumable.getItemID());
            insertStmt.setInt(2, consumable.getItemLevel());
            insertStmt.setString(3, consumable.getDescription());
            insertStmt.executeUpdate();
            
            // Since we're using an existing ItemID, just return the same object
            return new Consumables(consumable.getItemID(), consumable.getItemLevel(), consumable.getDescription());
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
    
    /**
     * Get a Consumable by its ItemID.
     */
    public Consumables getConsumableByItemId(int itemId) throws SQLException {
        String selectConsumable = "SELECT ItemID, ItemLevel, Description FROM Consumables WHERE ItemID=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectConsumable);
            selectStmt.setInt(1, itemId);
            results = selectStmt.executeQuery();
            
            if (results.next()) {
                return new Consumables(
                    results.getInt("ItemID"),
                    results.getInt("ItemLevel"),
                    results.getString("Description")
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
    
    /**
     * Update a Consumable's information.
     */
    public Consumables updateConsumable(Consumables consumable) throws SQLException {
        String updateConsumable = "UPDATE Consumables SET ItemLevel=?, Description=? WHERE ItemID=?;";
        Connection connection = null;
        PreparedStatement updateStmt = null;
        
        try {
            connection = connectionManager.getConnection();
            updateStmt = connection.prepareStatement(updateConsumable);
            updateStmt.setInt(1, consumable.getItemLevel());
            updateStmt.setString(2, consumable.getDescription());
            updateStmt.setInt(3, consumable.getItemID());
            updateStmt.executeUpdate();
            
            return consumable;
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
    
    /**
     * Delete a Consumable. Note: This only removes the consumable-specific data,
     * not the corresponding Items record.
     */
    public void delete(Consumables consumable) throws SQLException {
        String deleteConsumable = "DELETE FROM Consumables WHERE ItemID=?;";
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deleteConsumable);
            deleteStmt.setInt(1, consumable.getItemID());
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
    
    /**
     * Get all Consumables of a specific item level.
     */
    public List<Consumables> getConsumablesByItemLevel(int itemLevel) throws SQLException {
        List<Consumables> consumables = new ArrayList<>();
        String selectConsumables = "SELECT ItemID, ItemLevel, Description FROM Consumables WHERE ItemLevel=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectConsumables);
            selectStmt.setInt(1, itemLevel);
            results = selectStmt.executeQuery();
            
            while (results.next()) {
                consumables.add(new Consumables(
                    results.getInt("ItemID"),
                    results.getInt("ItemLevel"),
                    results.getString("Description")
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
        return consumables;
    }
    public Consumables updateDescription(Consumables consumableItem, String newDescription) throws SQLException {
        String updateDescription = "UPDATE Consumables SET Description = ? WHERE ItemID = ?;";
        Connection connection = null;
        PreparedStatement updateStmt = null;

        try {
            connection = connectionManager.getConnection();
            updateStmt = connection.prepareStatement(updateDescription);
            updateStmt.setString(1, newDescription);
            updateStmt.setInt(2, consumableItem.getItemID());
            updateStmt.executeUpdate();

            // Update the local object to reflect the change
            consumableItem.setDescription(newDescription);
            return consumableItem;
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
    
}