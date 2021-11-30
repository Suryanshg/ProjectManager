package projectmanager.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

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

  // Creating a Project
  public boolean addTask(Task task, String projectId) throws Exception {
    try {
      PreparedStatement ps = conn
          .prepareStatement("SELECT * FROM Task WHERE title = ? AND Project = ?;");
      ps.setString(1, task.title);
      ps.setString(2, projectId);

      ResultSet resultSet = ps.executeQuery();
      // If the project is already present then return false
      // resultSet.getString("title");
      while (resultSet.next()) {
        System.out.println(resultSet.getRow());
        generateTask(resultSet);
        resultSet.close();
        return false;
      }
      // Creating a new project
    } catch (Exception e) {
    }
    try {
      PreparedStatement ps = conn.prepareStatement(
          "INSERT INTO Task (id, title, completed, parentTask, Project, outlineNumber) values(?,?,?,?,?,?);");
      ps.setString(1, task.id.toString());
      ps.setString(2, task.title);
      ps.setBoolean(3, task.completed);
      ps.setString(4, task.parentTask == null ? null : task.parentTask.id.toString());
      ps.setString(5, projectId);
      ps.setString(6, task.outlineNumber);
      ps.execute();
      return true;
    } catch (Exception e) {
      throw new Exception("Failed to insert task: " + e.getMessage());
    }
  }

  private Task generateTask(ResultSet resultSet) throws Exception {
    UUID id = UUID.fromString(resultSet.getString("id"));
    String title = resultSet.getString("title");
    Boolean completed = resultSet.getBoolean("completed");
    String outlineNumber = resultSet.getString("outlineNumber");
    Task parentTask;
    try {
      parentTask = (Task) resultSet.getObject("parentTask"); // assume all task data is present
    } catch (Exception e) {
      parentTask = null;
    }
    Task task = new Task(id, title, outlineNumber, completed, parentTask);

    return task;
  }
}
