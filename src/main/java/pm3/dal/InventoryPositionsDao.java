package pm3.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import pm3.model.*;

public class InventoryPositionsDao {
	protected ConnectionManager connectionManager;

	private static InventoryPositionsDao instance = null;
	protected InventoryPositionsDao() {
		connectionManager = new ConnectionManager();
	}
	public static InventoryPositionsDao getInstance() {
		if(instance == null) {
			instance = new InventoryPositionsDao();
		}
		return instance;
	}
	
	public InventoryPositions create(InventoryPositions inventoryPositions) throws SQLException {
		String insertInventoryPositions =
			"INSERT INTO InventoryPositions(characterID, stackPosition, itemID, stackSize) " +
			"VALUES(?,?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertInventoryPositions);
			insertStmt.setInt(1, inventoryPositions.getCharacterInfo().getCharacterID());
			insertStmt.setInt(2, inventoryPositions.getStackPosition());
			insertStmt.setInt(3, inventoryPositions.getItems().getItemID());
			insertStmt.setInt(4, inventoryPositions.getStackSize());
			insertStmt.executeUpdate();
			
		
			return inventoryPositions;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(insertStmt != null) {
				insertStmt.close();
			}
		}
	}
	
	public List<InventoryPositions> getInventoryPositions(int itemID) throws SQLException {
		
		List<InventoryPositions> inventoryPositions = new ArrayList<InventoryPositions>();
		String selectInventoryPositions =
			"SELECT characterID, stackPosition, itemID, stackSize " +
			"FROM InventoryPositions " +
			"WHERE itemID=?;";
		
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectInventoryPositions);
			selectStmt.setInt(1, itemID);
			results = selectStmt.executeQuery();
			CharacterInfoDao characterInfoDao = CharacterInfoDao.getInstance();
            ItemsDao itemsDao = ItemsDao.getInstance();
			while(results.next()) {
				int characterID = results.getInt("characterID");
				int stackPosition = results.getInt("stackPosition");
				int resItemID = results.getInt("itemID");
				int stackSize = results.getInt("stackSize");
				
				CharacterInfo characterInfo = characterInfoDao.getCharactersByCharacterID(characterID);
                Items item = itemsDao.getItemById(resItemID);
		
                InventoryPositions inventoryPosition = new InventoryPositions(
                        characterInfo, 
                        stackPosition, 
                        item, 
                        stackSize
                    );
				inventoryPositions.add(inventoryPosition);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(selectStmt != null) {
				selectStmt.close();
			}
			if(results != null) {
				results.close();
			}
		}
		return inventoryPositions;
	}
	 
	
	public InventoryPositions updateInventoryPositions(InventoryPositions inventoryPosition, int newStackSize) throws SQLException {
		
		
		String updateInventoryPosition = "UPDATE InventoryPositions SET newStackSize=? WHERE characterID=? AND stackPosition=?";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateInventoryPosition);
			updateStmt.setInt(1, newStackSize);
			updateStmt.setInt(2, inventoryPosition.getCharacterInfo().getCharacterID());
			updateStmt.setInt(3, inventoryPosition.getStackPosition());
			updateStmt.executeUpdate();
			inventoryPosition.setStackSize(newStackSize);
			return inventoryPosition;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(updateStmt != null) {
				updateStmt.close();
			}
		}
	}
	
	public InventoryPositions delete(InventoryPositions inventoryPosition) throws SQLException {

		String deleteInventoryPosition = "DELETE FROM InventoryPositions WHERE characterID=? AND stackPosition=?";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteInventoryPosition);
			deleteStmt.setInt(1, inventoryPosition.getCharacterInfo().getCharacterID());
			deleteStmt.setInt(2, inventoryPosition.getStackPosition());
			deleteStmt.executeUpdate();

	
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(deleteStmt != null) {
				deleteStmt.close();
			}
		}
	}
	
	}


