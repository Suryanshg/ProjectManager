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

public class TeammateDAO {
    java.sql.Connection conn;

    public TeammateDAO() {
        try {
            conn = DatabaseUtil.connect();
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

        }

        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Teammate (id, name, Project) values (?,?,?);", Statement.RETURN_GENERATED_KEYS);
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


}
