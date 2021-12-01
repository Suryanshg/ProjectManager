package projectmanager.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
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
      // If the project is already present then return false
      // resultSet.getString("title");
      while (resultSet.next()) {
        resultSet.close();
        return false;
      }
      // Creating a new project
    } catch (Exception e) {
    }
    try {
      PreparedStatement ps = conn.prepareStatement(
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
      throw new Exception("Failed to delete project: " + e.getMessage());
    }
  }

  private Task generateTask(ResultSet resultSet) throws Exception {
    UUID id = UUID.fromString(resultSet.getString("id"));
    String title = resultSet.getString("title");
    Boolean completed = resultSet.getBoolean("completed");
    String outlineNumber = resultSet.getString("outlineNumber");
    // String parent = resultSet.getString("Parent");
    // String projectId = resultSet.getString("projectid");

    // TODO implement this later
    // Task task = new Task(id, title, outlineNumber, completed);
    // return task;

    return null;
  }
}
