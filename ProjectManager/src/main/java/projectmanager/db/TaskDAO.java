package projectmanager.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import projectmanager.model.Project;
import projectmanager.model.Task;
import projectmanager.model.TeammateTask;

public class TaskDAO {
	java.sql.Connection conn;

	public TaskDAO() {
		try {
			conn = DatabaseUtil.connect();
		} catch (Exception e) {
			e.printStackTrace();
			conn = null;
		}
	}

	// Creating a Task
	public Boolean addTask(Task task, String parentTask, String projectid) throws Exception {
		try {
			String statement = String.format("SELECT * FROM Task WHERE title = ? AND parentTask %s ? AND Project %s ?;",
					parentTask == null ? "IS" : "=", projectid == null ? "IS" : "=");
			PreparedStatement ps = conn
					.prepareStatement(statement);
			ps.setString(1, task.title);
			if (parentTask != null)
				ps.setString(2, parentTask);
			else
				ps.setNull(2, Types.NULL);
			if (projectid != null)
				ps.setString(3, projectid);
			else
				ps.setNull(3, Types.NULL);

			ResultSet resultSet = ps.executeQuery();
			// If the task is already present then return false
			// resultSet.getString("title");
			while (resultSet.next()) {
				resultSet.close();
				return false;
			}
		} catch (Exception e) {
		}
		try {

			//  Setting up the outline number
			String statement = String.format("SELECT count(*) AS count FROM Task WHERE parentTask %s ? AND Project %s ?;",
					parentTask == null ? "IS" : "=", projectid == null ? "IS" : "=");
			PreparedStatement ps = conn
					.prepareStatement(statement);
			if (parentTask != null)
				ps.setString(1, parentTask);
			else
				ps.setNull(1, Types.NULL);
			if (projectid != null)
				ps.setString(2, projectid);
			else
				ps.setNull(2, Types.NULL);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				task.outlineNumber = String.valueOf(resultSet.getInt("count") + 1);
				resultSet.close();
				break;
			}
			

			// Creating a new task
			ps = conn.prepareStatement(
					"INSERT INTO Task (id, title, completed, parentTask, Project, outlineNumber) values(?,?,?,?,?,?);",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, task.id.toString());
			ps.setString(2, task.title);
			ps.setBoolean(3, task.completed);
			if (parentTask != null)
				ps.setString(4, parentTask);
			else
				ps.setNull(4, Types.NULL);
			if (projectid != null)
				ps.setString(5, projectid);
			else
				ps.setNull(5, Types.NULL);
			ps.setString(6, task.outlineNumber);
			ps.execute();
			
			// Transfering assignees if the task has a parentTask
			if(parentTask != null) {
				// Extract the parentTask's assignees
				TeammateTaskDAO ttDao = new TeammateTaskDAO();
				
				List<TeammateTask> teammateTasks = ttDao.getAllTeammateTaskForTaskId(parentTask);
				
	
				for(TeammateTask tt: teammateTasks) {
					// Unassign all the teammates from the parentTask
					ttDao.unassignTeammate(tt.projectid, tt.taskid, tt.teammateid);
					
					// Assign all the teammates to the newly created (sub)Task
					ttDao.assignTeammate(tt.projectid, task.id.toString(), tt.teammateid);
				}
			}
			
			return true;

		} catch (Exception e) {
			throw new Exception("Failed to insert task: " + e.getMessage());
		}
	}

	public boolean markTask(String id, int completed) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE Task SET completed = ? WHERE id = ?;");
			ps.setInt(1, completed);
			ps.setString(2, id);
			int numAffected = ps.executeUpdate();
			ps.close();
			return (numAffected == 1);

		} catch (Exception e) {
			throw new Exception("Failed to mark task as complete: " + e.getMessage());
		}
	}

	public boolean renameTask(String id, String title) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE Task SET title = ? WHERE id = ?;");
			ps.setString(1, title);
			ps.setString(2, id);
			int numAffected = ps.executeUpdate();
			ps.close();
			return (numAffected == 1);

		} catch (Exception e) {
			throw new Exception("Failed to rename task: " + e.getMessage());
		}
	}

	public boolean deleteTask(String id) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM Task WHERE id = ?;");
			ps.setString(1, id);
			int numAffected = ps.executeUpdate();
			ps.close();
			return (numAffected == 1);

		} catch (Exception e) {
			throw new Exception("Failed to delete task: " + e.getMessage());
		}
	}

	public boolean deleteAllTasks(String projectid) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE Task SET parentTask = NULL WHERE Project = ?;");
			ps.setString(1, projectid);
			ps.execute();
			ps = conn.prepareStatement("DELETE FROM Task WHERE Project = ?;");
			ps.setString(1, projectid);
			ps.execute();
			ps.close();
			return true;

		} catch (Exception e) {
			throw new Exception("Failed to delete tasks: " + e.getMessage());
		}
	}

	// Retrieves the top level tasks for a project
	public List<Task> getTasksByProject(String projectid) throws Exception {

		List<Task> tasks = new ArrayList<Task>();

		try {
			PreparedStatement ps = conn
					.prepareStatement(
							"SELECT * FROM Task WHERE Project = ? and parentTask is NULL ORDER BY outlineNumber;");
			ps.setString(1, projectid);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				Task t = generateTask(resultSet);
				tasks.add(t);
			}
			resultSet.close();
			ps.close();
			return tasks;

		} catch (Exception e) {
			throw new Exception("Failed in retrieving all tasks: " + e.getMessage());
		}
	}

	// Retrieves the child tasks for a Task
	public List<Task> getTasksByParent(String parentId) throws Exception {

		List<Task> tasks = new ArrayList<Task>();

		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Task WHERE parentTask = ? ORDER BY outlineNumber;");
			ps.setString(1, parentId);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				Task t = generateTask(resultSet);
				tasks.add(t);
			}
			resultSet.close();
			ps.close();
			return tasks;

		} catch (Exception e) {
			throw new Exception("Failed in retrieving all subtasks: " + e.getMessage());
		}
	}

	// Helper function to generate a Task
	private Task generateTask(ResultSet resultSet) throws Exception {
		// System.out.println("in generateTask");
		UUID id = UUID.fromString(resultSet.getString("id"));
		String title = resultSet.getString("title");
		Boolean completed = resultSet.getBoolean("completed");
		String outlineNumber = resultSet.getString("outlineNumber");

		Task task = new Task(id, title, outlineNumber, completed);

		// String parentId = resultSet.getString("parentTask");
		// String projectId = resultSet.getString("Project");

		// Setting up the subTasks
		List<Task> subTasks = getTasksByParent(id.toString());
		task.subTasks = subTasks;

		// TODO: Set up the assignees

		return task;
	}
}
