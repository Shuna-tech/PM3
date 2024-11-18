package pm3.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pm3.model.CharacterInfo;
import pm3.model.CharacterJobs;
import pm3.model.Jobs;

public class CharacterJobsDao {
    protected ConnectionManager connectionManager;
    private static CharacterJobsDao instance = null;
    
    protected CharacterJobsDao() {
        connectionManager = new ConnectionManager();
    }
    
    public static CharacterJobsDao getInstance() {
        if(instance == null) {
            instance = new CharacterJobsDao();
        }
        return instance;
    }
    
    //create a new character job
    public CharacterJobs create(CharacterJobs characterJob) throws SQLException {
        String insertCharacterJob = 
            "INSERT INTO CharacterJobs(characterID, jobID, level, experiencePoints, isCurrentJob) " +
            "VALUES(?,?,?,?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertCharacterJob);
            insertStmt.setInt(1, characterJob.getCharacter().getCharacterID());
            insertStmt.setInt(2, characterJob.getJob().getJobID());    
            insertStmt.setInt(3, characterJob.getLevel());
            insertStmt.setInt(4, characterJob.getExperiencePoints());
            insertStmt.setBoolean(5, characterJob.isCurrentJob());
            insertStmt.executeUpdate();
            return characterJob;
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
    
    //get character job by character and job
    public CharacterJobs getCharacterJobByCharacterAndJob(CharacterInfo character, Jobs job) throws SQLException {    
        String selectCharacterJob = 
            "SELECT characterID, jobID, level, experiencePoints, isCurrentJob " +
            "FROM CharacterJobs " +
            "WHERE characterID=? AND jobID=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectCharacterJob);
            selectStmt.setInt(1, character.getCharacterID());
            selectStmt.setInt(2, job.getJobID());    
            results = selectStmt.executeQuery();
            
            if(results.next()) {
                int level = results.getInt("level");
                int experiencePoints = results.getInt("experiencePoints");
                boolean isCurrentJob = results.getBoolean("isCurrentJob");
                
                CharacterJobs characterJob = new CharacterJobs(character, job,    
                    level, experiencePoints, isCurrentJob);
                return characterJob;
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
    
    //retrieve all jobs for a character
    public List<CharacterJobs> getCharacterJobsByCharacter(CharacterInfo character) throws SQLException {
        List<CharacterJobs> characterJobs = new ArrayList<>();
        String selectCharacterJobs =
            "SELECT characterID, jobID, level, experiencePoints, isCurrentJob " +
            "FROM CharacterJobs " +
            "WHERE characterID=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectCharacterJobs);
            selectStmt.setInt(1, character.getCharacterID());
            results = selectStmt.executeQuery();
            JobsDao jobsDao = JobsDao.getInstance();    
            
            while(results.next()) {
                int jobID = results.getInt("jobID");
                Jobs job = jobsDao.getJobById(jobID);   
                int level = results.getInt("level");
                int experiencePoints = results.getInt("experiencePoints");
                boolean isCurrentJob = results.getBoolean("isCurrentJob");
                
                CharacterJobs characterJob = new CharacterJobs(character, job,   
                    level, experiencePoints, isCurrentJob);
                characterJobs.add(characterJob);
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
        return characterJobs;
    }
    
    //update the level and experience points of a character job
    public CharacterJobs updateLevelAndExperience(CharacterJobs characterJob, 
            int newLevel, int newExperiencePoints) throws SQLException {
        String updateCharacterJob = 
            "UPDATE CharacterJobs " +
            "SET level=?, experiencePoints=? " +
            "WHERE characterID=? AND jobID=?;";
        Connection connection = null;
        PreparedStatement updateStmt = null;
        try {
            connection = connectionManager.getConnection();
            updateStmt = connection.prepareStatement(updateCharacterJob);
            updateStmt.setInt(1, newLevel);
            updateStmt.setInt(2, newExperiencePoints);
            updateStmt.setInt(3, characterJob.getCharacter().getCharacterID());
            updateStmt.setInt(4, characterJob.getJob().getJobID());    
            updateStmt.executeUpdate();
            
            characterJob.setLevel(newLevel);
            characterJob.setExperiencePoints(newExperiencePoints);
            return characterJob;
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
    
    //delete a character job
    public CharacterJobs delete(CharacterJobs characterJob) throws SQLException {
        String deleteCharacterJob = "DELETE FROM CharacterJobs WHERE characterID=? AND jobID=?;";
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deleteCharacterJob);
            deleteStmt.setInt(1, characterJob.getCharacter().getCharacterID());
            deleteStmt.setInt(2, characterJob.getJob().getJobID());    
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