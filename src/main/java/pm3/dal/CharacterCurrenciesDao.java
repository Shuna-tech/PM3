package pm3.dal;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pm3.model.CharacterCurrencies;
import pm3.model.CharacterInfo;
import pm3.model.Currencies;

public class CharacterCurrenciesDao {
    protected ConnectionManager connectionManager;
    private static CharacterCurrenciesDao instance = null;
    
    protected CharacterCurrenciesDao() {
        connectionManager = new ConnectionManager();
    }
    
    public static CharacterCurrenciesDao getInstance() {
        if(instance == null) {
            instance = new CharacterCurrenciesDao();
        }
        return instance;
    }

    // Create a new character currency relationship
    public CharacterCurrencies create(CharacterCurrencies characterCurrency) throws SQLException {
        String insertCharacterCurrency = 
            "INSERT INTO CharacterCurrencies(characterID, currencyID, totalAmountOwned, amountThisWeek) " +
            "VALUES(?,?,?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertCharacterCurrency);
            insertStmt.setInt(1, characterCurrency.getCharacter().getCharacterID());
            insertStmt.setInt(2, characterCurrency.getCurrency().getCurrencyID());
            insertStmt.setInt(3, characterCurrency.getTotalAmountOwned());
            insertStmt.setInt(4, characterCurrency.getAmountThisWeek());
            insertStmt.executeUpdate();
            return characterCurrency;
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

    // Get character currency by characterID and currencyID
    public CharacterCurrencies getCharacterCurrencyByIDs(int characterID, int currencyID) throws SQLException {
        String selectCharacterCurrency = 
            "SELECT characterID, currencyID, totalAmountOwned, amountThisWeek " +
            "FROM CharacterCurrencies " +
            "WHERE characterID=? AND currencyID=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectCharacterCurrency);
            selectStmt.setInt(1, characterID);
            selectStmt.setInt(2, currencyID);
            results = selectStmt.executeQuery();
            CharacterInfoDao characterDao = CharacterInfoDao.getInstance();
            CurrenciesDao currenciesDao = CurrenciesDao.getInstance();
            if(results.next()) {
                int totalAmountOwned = results.getInt("totalAmountOwned");
                int amountThisWeek = results.getInt("amountThisWeek");
                
                CharacterInfo character = characterDao.getCharactersByCharacterID(characterID);
                Currencies currency = currenciesDao.getCurrencyByID(currencyID);
                CharacterCurrencies characterCurrency = new CharacterCurrencies(
                    character, currency, totalAmountOwned, amountThisWeek);
                return characterCurrency;
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

    // Delete a character currency relationship
    public CharacterCurrencies delete(CharacterCurrencies characterCurrency) throws SQLException {
        String deleteCharacterCurrency = 
            "DELETE FROM CharacterCurrencies " +
            "WHERE characterID=? AND currencyID=?;";
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deleteCharacterCurrency);
            deleteStmt.setInt(1, characterCurrency.getCharacter().getCharacterID());
            deleteStmt.setInt(2, characterCurrency.getCurrency().getCurrencyID());
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
    
 // Update a character currency relationship
    public CharacterCurrencies update(CharacterCurrencies characterCurrency) throws SQLException {
        String updateCharacterCurrency = 
            "UPDATE CharacterCurrencies SET totalAmountOwned=?, amountThisWeek=? " +
            "WHERE characterID=? AND currencyID=?";
        Connection connection = null;
        PreparedStatement updateStmt = null;
        try {
            connection = connectionManager.getConnection();
            updateStmt = connection.prepareStatement(updateCharacterCurrency);
            updateStmt.setInt(1, characterCurrency.getTotalAmountOwned());
            updateStmt.setInt(2, characterCurrency.getAmountThisWeek());
            updateStmt.setInt(3, characterCurrency.getCharacter().getCharacterID());
            updateStmt.setInt(4, characterCurrency.getCurrency().getCurrencyID());
            updateStmt.executeUpdate();
            return characterCurrency;
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
    