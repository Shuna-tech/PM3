package pm3.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import pm3.model.Currencies;

public class CurrenciesDao {
    protected ConnectionManager connectionManager;
    private static CurrenciesDao instance = null;
    
    protected CurrenciesDao() {
        connectionManager = new ConnectionManager();
    }
    
    public static CurrenciesDao getInstance() {
        if(instance == null) {
            instance = new CurrenciesDao();
        }
        return instance;
    }

    // Create a new currency
    public Currencies create(Currencies currency) throws SQLException {
        String insertCurrency = "INSERT INTO Currencies(currencyName, max_amount, weeklycap) VALUES(?,?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        ResultSet resultKey = null;
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertCurrency,
                    Statement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, currency.getCurrencyName());
            insertStmt.setInt(2, currency.getMaxAmount());
            insertStmt.setInt(3, currency.getWeeklyCap());
            insertStmt.executeUpdate();
            
            resultKey = insertStmt.getGeneratedKeys();
            int currencyID = -1;
            if(resultKey.next()) {
                currencyID = resultKey.getInt(1);
            } else {
                throw new SQLException("Unable to retrieve auto-generated key.");
            }
            currency.setCurrencyID(currencyID);
            return currency;
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(connection != null) {
                connection.close();
            }
            if(insertStmt != null) {
                insertStmt.close();
            }
            if(resultKey != null) {
                resultKey.close();
            }
        }
    }

    // Get a currency by ID
    public Currencies getCurrencyByID(int currencyID) throws SQLException {
        String selectCurrency = "SELECT currencyID, currencyName, max_amount, weeklycap FROM Currencies WHERE currencyID=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectCurrency);
            selectStmt.setInt(1, currencyID);
            results = selectStmt.executeQuery();
            if(results.next()) {
                int resultCurrencyID = results.getInt("currencyID");
                String currencyName = results.getString("currencyName");
                int maxAmount = results.getInt("max_amount");
                int weeklyCap = results.getInt("weeklycap");
                
                return new Currencies(resultCurrencyID, currencyName, maxAmount, weeklyCap);
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
        return null;
    }


    // Delete a currency
    public Currencies delete(Currencies currency) throws SQLException {
        String deleteCurrency = "DELETE FROM Currencies WHERE currencyID=?;";
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deleteCurrency);
            deleteStmt.setInt(1, currency.getCurrencyID());
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
    
    // Update a currency
    public Currencies update(Currencies currency) throws SQLException {
        String updateCurrency = "UPDATE Currencies SET currencyName=?, max_amount=?, weeklycap=? WHERE currencyID=?";
        Connection connection = null;
        PreparedStatement updateStmt = null;
        try {
            connection = connectionManager.getConnection();
            updateStmt = connection.prepareStatement(updateCurrency);
            updateStmt.setString(1, currency.getCurrencyName());
            updateStmt.setInt(2, currency.getMaxAmount());
            updateStmt.setInt(3, currency.getWeeklyCap());
            updateStmt.setInt(4, currency.getCurrencyID());
            updateStmt.executeUpdate();
            return currency;
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
}