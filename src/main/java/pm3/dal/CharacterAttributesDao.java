package pm3.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pm3.model.*;

public class CharacterAttributesDao {
    protected ConnectionManager connectionManager;

    private static CharacterAttributesDao instance = null;
    protected CharacterAttributesDao() {
        connectionManager = new ConnectionManager();
    }
    public static CharacterAttributesDao getInstance() {
        if(instance == null) {
            instance = new CharacterAttributesDao();
        }
        return instance;
    }

    public CharacterAttributes create(CharacterAttributes characterAttribute) throws SQLException {
        String insertCharacterAttribute =
            "INSERT INTO CharacterAttributes(attributeID, characterID, attributeValue) " +
            "VALUES(?,?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertCharacterAttribute);

            insertStmt.setInt(1, characterAttribute.getAttributes().getAttributeID());
            insertStmt.setInt(2, characterAttribute.getCharacterInfo().getCharacterID());
            insertStmt.setInt(3, characterAttribute.getAttributeValue());
            insertStmt.executeUpdate();

            return characterAttribute;
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

    public List<CharacterAttributes> getCharacterAttributes(int characterID) throws SQLException {
        List<CharacterAttributes> characterAttributes = new ArrayList<CharacterAttributes>();
        String selectCharacterAttributes =
            "SELECT attributeID, characterID, attributeValue " +
            "FROM CharacterAttributes " +
            "WHERE characterID=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectCharacterAttributes);
            selectStmt.setInt(1, characterID);
            results = selectStmt.executeQuery();

            AttributesDao attributesDao = AttributesDao.getInstance();
            CharacterInfoDao characterInfoDao = CharacterInfoDao.getInstance();

            while(results.next()) {
                int attributeID = results.getInt("attributeID");
                int resultCharacterID = results.getInt("characterID");
                int attributeValue = results.getInt("attributeValue");

                Attributes attribute = attributesDao.getAttributeByAttributesID(attributeID);
                CharacterInfo characterInfo = characterInfoDao.getCharactersByCharacterID(resultCharacterID);

                CharacterAttributes characterAttribute = new CharacterAttributes(
                    attribute,
                    characterInfo,
                    attributeValue
                );
                characterAttributes.add(characterAttribute);
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
        return characterAttributes;
    }

    public CharacterAttributes updateCharacterAttribute(CharacterAttributes characterAttribute, int newAttributeValue) throws SQLException {
        String updateCharacterAttribute = 
            "UPDATE CharacterAttributes SET attributeValue=? " +
            "WHERE attributeID=? AND characterID=?";
        Connection connection = null;
        PreparedStatement updateStmt = null;
        try {
            connection = connectionManager.getConnection();
            updateStmt = connection.prepareStatement(updateCharacterAttribute);
            updateStmt.setInt(1, newAttributeValue);
            updateStmt.setInt(2, characterAttribute.getAttributes().getAttributeID());
            updateStmt.setInt(3, characterAttribute.getCharacterInfo().getCharacterID());
            updateStmt.executeUpdate();
            characterAttribute.setAttributeValue(newAttributeValue);
            return characterAttribute;
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

    public CharacterAttributes delete(CharacterAttributes characterAttribute) throws SQLException {
        String deleteCharacterAttribute = 
            "DELETE FROM CharacterAttributes " +
            "WHERE attributeID=? AND characterID=?";
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deleteCharacterAttribute);
            deleteStmt.setInt(1, characterAttribute.getAttributes().getAttributeID());
            deleteStmt.setInt(2, characterAttribute.getCharacterInfo().getCharacterID());
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