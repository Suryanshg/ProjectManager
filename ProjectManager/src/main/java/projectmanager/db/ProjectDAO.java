package projectmanager.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import projectmanager.model.Project;

public class ProjectDAO {

	java.sql.Connection conn;

	public ProjectDAO() {

		try {
			conn = DatabaseUtil.connect();
		} catch (Exception e) {
			e.printStackTrace();
			conn = null;
		}
	}

	// TODO: Need to add more stuff for future iterations and populate the other
	// DAOs.

	// Creating a Project
	public boolean addProject(Project project) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Project WHERE name = ?;");
			ps.setString(1, project.name);
			ResultSet resultSet = ps.executeQuery();

			// If the project is already present then return false
			while (resultSet.next()) {
				Project c = generateProject(resultSet);
				resultSet.close();
				return false;
			}
		} catch (Exception e) {
		}
		try {
			// Creating a new project
			PreparedStatement ps = conn.prepareStatement("INSERT INTO Project (name,isArchived,id) values(?,?,?);");
			ps.setString(1, project.name);
			ps.setBoolean(2, project.isArchived);
			ps.setString(3, project.id.toString());
			ps.execute();
			return true;

		} catch (Exception e) {
			throw new Exception("Failed to insert project: " + e.getMessage());
		}
	}

	// Retrieving a Project by name
	public Project getProject(String name) throws Exception {

		try {
			Project project = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Project WHERE name=?;");
			ps.setString(1, name);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				project = generateProject(resultSet);
			}
			resultSet.close();
			ps.close();

			return project;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed in retrieving the project: " + e.getMessage());
		}
	}

	// Retrieving a Project by ID
	public Project getProjectByID(String id) throws Exception {

		try {
			Project project = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Project WHERE id=?;");
			ps.setString(1, id);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				project = generateProject(resultSet);
			}
			resultSet.close();
			ps.close();

			return project;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed in retrieving the project: " + e.getMessage());
		}
	}

	// Retrieving all Projects
	public List<Project> getAllProjects() throws Exception {

		List<Project> projects = new ArrayList<Project>();
		try {
			Statement statement = conn.createStatement();
			String query = "SELECT * FROM Project";
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				Project p = generateProject(resultSet);
				projects.add(p);
			}
			resultSet.close();
			statement.close();
			return projects;

		} catch (Exception e) {
			throw new Exception("Failed in retrieving all projects: " + e.getMessage());
		}
	}

	// Deleting a project by its id
	public boolean deleteProject(String id) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM Project WHERE id = ?;");
			ps.setString(1, id);
			int numAffected = ps.executeUpdate();
			ps.close();

			return (numAffected == 1);

			// TODO: Need to add code to delete the records linked to the deleted project in
			// the Teammate and Task table

		} catch (Exception e) {
			throw new Exception("Failed to delete project: " + e.getMessage());
		}
	}

	// Helper method to generate a project from the retrieved resultSet
	private Project generateProject(ResultSet resultSet) throws Exception {
		UUID id = UUID.fromString(resultSet.getString("id"));
		String name = resultSet.getString("name");
		boolean isArchived = resultSet.getBoolean("isArchived"); // Setting up the isArchived
		Project project = new Project(id, name, isArchived);
		// TODO: Add DAO related code for retrieving related Teammates and Tasks
		TaskDAO taskDao = new TaskDAO();
		
		// Adding the tasks related to this project
		project.tasks = taskDao.getTasksByProject(project.id.toString());

		return project;
	}
}
