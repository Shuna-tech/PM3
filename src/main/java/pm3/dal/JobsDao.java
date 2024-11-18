package pm3.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pm3.model.Jobs;

public class JobsDao {
	protected ConnectionManager connectionManager;
	private static JobsDao instance = null;
	
	protected JobsDao() {
		connectionManager = new ConnectionManager();
	}
	
	public static JobsDao getInstance() {
		if(instance == null) {
			instance = new JobsDao();
		}
		return instance;
	}
	
	//create a new job
	public Jobs create(Jobs job) throws SQLException {
		String insertJob = "INSERT INTO Jobs(jobName) VALUES(?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertJob,
					Statement.RETURN_GENERATED_KEYS);
			insertStmt.setString(1, job.getJobName());
			insertStmt.executeUpdate();
			
			resultKey = insertStmt.getGeneratedKeys();
			int jobID = -1;
			if(resultKey.next()) {
				jobID = resultKey.getInt(1);
			}else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			job.setJobID(jobID);
			return job;
		}catch(SQLException e) {
			e.printStackTrace();
			throw e;
		}finally {
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
	
	//update the jobName of the job instance
	public Jobs updateJobName(Jobs job, String newJobName) throws SQLException {
		String updateJob = "UPDATE Jobs SET jobName=? WHERE jobID=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateJob);
			updateStmt.setString(1, newJobName);
			updateStmt.setInt(2, job.getJobID());
			updateStmt.executeUpdate();
			
			job.setJobName(newJobName);
			return job;
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
	
	//retrieve the job based on the primary key: jobID
	public Jobs getJobById(int jobID) throws SQLException {
		String selectJob = 
				"SELECT jobID, jobName FROM Jobs WHERE jobID=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectJob);
			selectStmt.setInt(1, jobID);
			results = selectStmt.executeQuery();
			
			if(results.next()) {
				int resultJobID = results.getInt("jobID");
				String jobName = results.getString("jobName");
				
				Jobs job = new Jobs(resultJobID, jobName);
				return job;
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
	
	//retrieve all jobs
	public List<Jobs> getAllJobs() throws SQLException {
		List<Jobs> jobs = new ArrayList<>();
		String selectJobs = "SELECT jobID, jobName FROM Jobs;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectJobs);
			results = selectStmt.executeQuery();
			
			while(results.next()) {
				int jobID = results.getInt("jobID");
				String jobName = results.getString("jobName");
				
				Jobs job = new Jobs(jobID, jobName);
				jobs.add(job);
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
		return jobs;
	}
	
	//delete a job by jobID
	public Jobs delete(Jobs job) throws SQLException {
		String deleteJob = "DELETE FROM Jobs WHERE jobID=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteJob);
			deleteStmt.setInt(1, job.getJobID());
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