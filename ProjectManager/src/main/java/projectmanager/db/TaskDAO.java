package projectmanager.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import projectmanager.model.Project;
import projectmanager.model.Task;

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
			// Creating a new project
		} catch (Exception e) {
		}
		try {
			String statement = String.format("SELECT count(*) AS count FROM Task WHERE parentTask %s ?;",
					parentTask == null ? "IS" : "=", projectid == null ? "IS" : "=");
			PreparedStatement ps = conn
					.prepareStatement(statement);
			if (parentTask != null)
				ps.setString(1, parentTask);
			else
				ps.setNull(1, Types.NULL);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				task.outlineNumber = String.valueOf(resultSet.getInt("count") + 1);
				resultSet.close();
				break;
			}
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
			return true;

		} catch (Exception e) {
			throw new Exception("Failed to insert task: " + e.getMessage());
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
		System.out.println("in generateTask");
		UUID id = UUID.fromString(resultSet.getString("id"));
		String title = resultSet.getString("title");
		Boolean completed = resultSet.getBoolean("completed");
		String outlineNumber = resultSet.getString("outlineNumber");

		Task task = new Task(id, title, outlineNumber, completed);

		String parentId = resultSet.getString("parentTask");
		// String projectId = resultSet.getString("Project");

		// Setting up the subTasks
		List<Task> subTasks = getTasksByParent(id.toString());
		task.subTasks = subTasks;

		// TODO: Set up the assignees

		return task;
	}
}
