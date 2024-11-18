package pm3.dal;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pm3.model.Attributes;

public class AttributesDao {
    protected ConnectionManager connectionManager;
    private static AttributesDao instance = null;
    
    protected AttributesDao() {
        connectionManager = new ConnectionManager();
    }
    
    public static AttributesDao getInstance() {
        if (instance == null) {
            instance = new AttributesDao();
        }
        return instance;
    }
    
    public Attributes create(Attributes attribute) throws SQLException {
        String insertAttribute = "INSERT INTO Attributes(AttributeName) VALUES(?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        ResultSet resultKey = null;
        
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertAttribute, PreparedStatement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, attribute.getAttributeName());
            insertStmt.executeUpdate();
            resultKey = insertStmt.getGeneratedKeys();
            if (resultKey.next()) {
                int generatedAttributeId = resultKey.getInt(1);
                return new Attributes(generatedAttributeId, attribute.getAttributeName());
            } else {
                throw new SQLException("Unable to retrieve auto-generated key.");
            }
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
            if (resultKey != null) {
                resultKey.close();
            }
        }
    }

    
    public Attributes getAttributeByAttributesID(int attributeID) throws SQLException {
        String selectAttribute = "SELECT AttributeID, AttributeName FROM Attributes WHERE AttributeID = ?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectAttribute);
            selectStmt.setInt(1, attributeID);
            results = selectStmt.executeQuery();
            
            if (results.next()) {
                int resultAttributeID = results.getInt("AttributeID");
                String attributeName = results.getString("AttributeName");
                return new Attributes(resultAttributeID, attributeName);
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

	public void delete(Attributes attribute1) {
		// TODO Auto-generated method stub
		
	}

}