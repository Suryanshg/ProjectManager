package projectmanager.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import projectmanager.model.Project;
import projectmanager.model.Task;
import projectmanager.model.Teammate;
import projectmanager.model.TeammateTask;
import projectmanager.db.TeammateTaskDAO;

public class TeammateDAO {
    java.sql.Connection conn;
    TeammateTaskDAO ttDAO;

    public TeammateDAO() {
        try {
            conn = DatabaseUtil.connect();
            ttDAO = new TeammateTaskDAO();
        } catch (Exception e) {
            e.printStackTrace();
            conn = null;
        }
    }

    public Boolean addTeammate(Teammate teammate, String projectid) throws Exception {
        try {
            String statement = "SELECT * FROM Teammate WHERE name = ? AND Project = ?;";
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setString(1, teammate.name);
            ps.setString(2, projectid);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                resultSet.close();
                return false;
            }

        } catch (Exception e) {
            throw new Exception("Failed to find existing teammates: " + e.getMessage());
        }

        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Teammate (id, name, Project) values (?,?,?);",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, String.valueOf(teammate.id));
            ps.setString(2, teammate.name);
            ps.setString(3, projectid);
            ps.execute();
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to insert teammate: " + e.getMessage());
        }
    }

    public Boolean deleteTeammate(String id) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Teammate WHERE id = ?;");
            ps.setString(1, id);
            int numAffected = ps.executeUpdate();
            ps.close();
            return (numAffected == 1);
        } catch (Exception e) {
            throw new Exception("Failed to delete teammate: " + e.getMessage());
        }
    }

    public Teammate getTeammateById(String id) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Teammate WHERE id = ?;");
            ps.setString(1, id);
            ResultSet resultSet = ps.executeQuery();
            Teammate t = null;
            while (resultSet.next()) {
                t = generateTeammate(resultSet);
            }
            resultSet.close();
            ps.close();
            return t;
        } catch (Exception e) {
            throw new Exception("Failed in retrieving teammate: " + e.getMessage());
        }
    }

    public List<Teammate> getTeammatesByProjectId(String projectId) throws Exception {
        List<Teammate> teammates = new ArrayList<Teammate>();

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Teammate WHERE Project = ? ORDER BY name;");
            ps.setString(1, projectId);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Teammate t = generateTeammate(resultSet);
                teammates.add(t);
            }

            resultSet.close();
            ps.close();
            return teammates;
        } catch (Exception e) {
            throw new Exception("Failed in retrieving all teammates: " + e.getMessage());
        }

    }

    public Teammate getTeammateByNameAndProjectId(String name, String projectid) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Teammate WHERE name = ? and Project = ?;");
            ps.setString(1, name);
            ps.setString(2, projectid);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            Teammate t = generateTeammate(resultSet);
            resultSet.close();
            ps.close();
            return t;
        } catch (Exception e) {
            throw new Exception("Failed in retrieving teammate: " + e.getMessage());
        }
    }

    public boolean deleteAllTeammates(String projectid) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Teammate WHERE Project = ?;");
            ps.setString(1, projectid);
            ps.executeUpdate();
            ps.close();
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to delete tasks: " + e.getMessage());
        }
    }

    private Teammate generateTeammate(ResultSet resultSet) throws Exception {
        // System.out.println("in generateTeammate");
        UUID id = UUID.fromString(resultSet.getString("id"));
        String name = resultSet.getString("name");
        String projectid = resultSet.getString("Project");

        // TODO: Set up related tasks

        Teammate t = new Teammate(id, name, projectid);
        List<TeammateTask> tTasks = ttDAO.getAllTeammateTaskForTeammateId(id.toString());
        t.tasks = new ArrayList<String>();
        for (TeammateTask tt : tTasks) {
            t.tasks.add(tt.taskid);
        }
        return t;
    }

}
