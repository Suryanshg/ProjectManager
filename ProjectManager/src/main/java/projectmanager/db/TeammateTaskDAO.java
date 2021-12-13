package projectmanager.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import projectmanager.model.TeammateTask;

public class TeammateTaskDAO {
	java.sql.Connection conn;

	public TeammateTaskDAO() {
		try {
			conn = DatabaseUtil.connect();
		} catch (Exception e) {
			e.printStackTrace();
			conn = null;
		}
	}

	public boolean getTeammateTask(String projectid, String taskid, String teammateid) throws Exception {
		if (projectid == null || taskid == null || teammateid == null)
			return false;
		try {
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM TeammateTask WHERE Project = ? AND taskId = ? AND teammateId = ?;");
			ps.setString(1, projectid);
			ps.setString(2, taskid);
			ps.setString(3, teammateid);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				return true;
			}
			ps.close();
			return false;
		} catch (Exception e) {
			throw new Exception("Failed to get teammate task: " + e.getMessage());
		}
	}

	public boolean assignTeammate(String projectid, String taskid, String teammateid) throws Exception {
		try {
			if (getTeammateTask(projectid, taskid, teammateid))
				return false;
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO TeammateTask (Project, taskId, teammateId) values(?,?,?);");
			ps.setString(1, projectid);
			ps.setString(2, taskid);
			ps.setString(3, teammateid);
			ps.execute();
			ps.close();
			return true;
		} catch (Exception e) {
			throw new Exception("Failed to assign teammate: " + e.getMessage());
		}
	}

	public boolean unassignTeammate(String projectid, String taskid, String teammateid) throws Exception {
		try {
			if (!getTeammateTask(projectid, taskid, teammateid))
				return false;
			PreparedStatement ps = conn.prepareStatement(
					"DELETE FROM TeammateTask WHERE Project = ? AND taskId = ? AND teammateId = ?;");
			ps.setString(1, projectid);
			ps.setString(2, taskid);
			ps.setString(3, teammateid);
			ps.execute();
			ps.close();
			return true;
		} catch (Exception e) {
			throw new Exception("Failed to unassign teammate: " + e.getMessage());
		}
	}

	public boolean unassignAllTeammates(String projectid) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement(
					"DELETE FROM TeammateTask WHERE Project = ?;");
			ps.setString(1, projectid);
			ps.execute();
			ps.close();
			return true;
		} catch (Exception e) {
			throw new Exception("Failed to unassign teammate: " + e.getMessage());
		}
	}

	public List<TeammateTask> getAllTeammateTaskForProjectId(String projectid) throws Exception {
		
		List<TeammateTask> teammateTasks = new ArrayList<TeammateTask>();
		
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM TeammateTask WHERE Project = ?;");
			ps.setString(1, projectid);
			ResultSet resultSet =  ps.executeQuery();
			
			while(resultSet.next()) {
				
				TeammateTask tt = new TeammateTask(resultSet.getString("Project"), 
												   resultSet.getString("taskId"), 
						                           resultSet.getString("teammateId"));
				
				teammateTasks.add(tt);
			}

		} catch (Exception e) {
			throw new Exception("Failed to get all TeammateTasks for the project: " + e.getMessage());
		}
		
		return teammateTasks;
	}
	
	public List<TeammateTask> getAllTeammateTaskForTaskId(String taskid) throws Exception{
		List<TeammateTask> teammateTasks = new ArrayList<TeammateTask>();
		
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM TeammateTask WHERE taskId = ?;");
			ps.setString(1, taskid);
			ResultSet resultSet = ps.executeQuery();
			
			while(resultSet.next()) {
				TeammateTask tt = new TeammateTask(resultSet.getString("Project"), 
						   						   resultSet.getString("taskId"), 
						   						   resultSet.getString("teammateId"));
				teammateTasks.add(tt);
			}
			
		} catch (Exception e) {
			throw new Exception("Failed to get all TeammateTask for the task: " + e.getMessage());
		}
		
		return teammateTasks;
	}
	
	


}
