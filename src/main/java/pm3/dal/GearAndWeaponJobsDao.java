package pm3.dal;

import pm3.model.GearAndWeaponJobs;
import pm3.model.Items;
import pm3.model.Jobs;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GearAndWeaponJobsDao {
    protected ConnectionManager connectionManager;
    private static GearAndWeaponJobsDao instance = null;
    private ItemsDao itemsDao;
    private JobsDao jobsDao;
    
    protected GearAndWeaponJobsDao() {
        connectionManager = new ConnectionManager();
        itemsDao = ItemsDao.getInstance();
        jobsDao = JobsDao.getInstance();
    }
    
    public static GearAndWeaponJobsDao getInstance() {
        if (instance == null) {
            instance = new GearAndWeaponJobsDao();
        }
        return instance;
    }

    public GearAndWeaponJobs create(GearAndWeaponJobs gearAndWeaponJobs) throws SQLException {
        String query = "INSERT INTO GearAndWeaponJobs (itemID, jobID) VALUES (?, ?)";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(query);
            insertStmt.setInt(1, gearAndWeaponJobs.getItem().getItemID()); 
            insertStmt.setInt(2, gearAndWeaponJobs.getJob().getJobID()); 
            insertStmt.executeUpdate();
            
            return gearAndWeaponJobs;
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

    public GearAndWeaponJobs getByItemId(int itemId) throws SQLException {
        String query = "SELECT * FROM GearAndWeaponJobs WHERE itemID = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = connectionManager.getConnection(); 
            statement = connection.prepareStatement(query);
            statement.setInt(1, itemId);  
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                // 获取完整的 Items 和 Jobs 对象
                Items item = itemsDao.getItemById(itemId);
                Jobs job = jobsDao.getJobById(resultSet.getInt("jobID"));
                return new GearAndWeaponJobs(item, job); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return null;
    }

    public List<GearAndWeaponJobs> getAll() throws SQLException {
        List<GearAndWeaponJobs> items = new ArrayList<>();
        String query = "SELECT * FROM GearAndWeaponJobs";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = connectionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
                // 获取完整的 Items 和 Jobs 对象
                Items item = itemsDao.getItemById(resultSet.getInt("itemID"));
                Jobs job = jobsDao.getJobById(resultSet.getInt("jobID"));
                items.add(new GearAndWeaponJobs(item, job));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return items;
    }

    public GearAndWeaponJobs update(GearAndWeaponJobs gearAndWeaponJobs) throws SQLException {
        String query = "UPDATE GearAndWeaponJobs SET jobID = ? WHERE itemID = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = connectionManager.getConnection();  
            statement = connection.prepareStatement(query);
            statement.setInt(1, gearAndWeaponJobs.getJob().getJobID());  
            statement.setInt(2, gearAndWeaponJobs.getItem().getItemID());  
            
            if (statement.executeUpdate() > 0) {
                return gearAndWeaponJobs;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        return null;
    }

    public boolean delete(int itemId) throws SQLException {
        String query = "DELETE FROM GearAndWeaponJobs WHERE itemID = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            connection = connectionManager.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, itemId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }
}