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
  public Task addTask(Task task) throws Exception {
    try {
      String statement = String.format("SELECT * FROM Task WHERE title = ? AND parentTask %s ? AND Project %s ?;",
          task.parentTask == null ? "IS" : "=", task.projectid == null ? "IS" : "=");
      PreparedStatement ps = conn
          .prepareStatement(statement);
      ps.setString(1, task.title);
      if (task.parentTask != null)
        ps.setString(2, task.parentTask);
      else
        ps.setNull(2, Types.NULL);

      if (task.projectid != null)
        ps.setString(3, task.projectid);
      else
        ps.setNull(3, Types.NULL);

      ResultSet resultSet = ps.executeQuery();
      // If the project is already present then return false
      // resultSet.getString("title");
      while (resultSet.next()) {
        resultSet.close();
        return null;
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
      if (task.parentTask != null)
        ps.setString(4, task.parentTask);
      else
        ps.setNull(4, Types.NULL);
      if (task.projectid != null)
        ps.setString(5, task.projectid);
      else
        ps.setNull(5, Types.NULL);
      ps.setString(6, task.outlineNumber);
      ps.execute();
      return task;

    } catch (Exception e) {
      throw new Exception("Failed to insert task: " + e.getMessage());
    }
  }

  private Task generateTask(ResultSet resultSet) throws Exception {
    UUID id = UUID.fromString(resultSet.getString("id"));
    String title = resultSet.getString("title");
    Boolean completed = resultSet.getBoolean("completed");
    String outlineNumber = resultSet.getString("outlineNumber");
    String parent = resultSet.getString("parent");
    String projectId = resultSet.getString("projectid");

    Task task = new Task(id, title, outlineNumber, completed, parent, projectId);

    return task;
  }
}
