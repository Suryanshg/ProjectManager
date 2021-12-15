package projectmanager.handler;

import static org.junit.Assert.*;
import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Test;

import projectmanager.db.ProjectDAO;
import projectmanager.db.TaskDAO;
import projectmanager.db.TeammateDAO;
import projectmanager.handler.AssignTeammateHandler;
import projectmanager.http.AssignTeammateRequest;
import projectmanager.http.AssignTeammateResponse;

import projectmanager.handler.UnassignTeammateHandler;
import projectmanager.http.UnassignTeammateRequest;
import projectmanager.http.UnassignTeammateResponse;
import projectmanager.model.Project;
import projectmanager.model.Task;
import projectmanager.model.Teammate;

public class AssignUnassignTeammateHandlerTest extends LambdaTest {
  void testAssignInput(String incoming, int outgoing) throws Exception {
    AssignTeammateHandler handler = new AssignTeammateHandler();
    AssignTeammateRequest req = new Gson().fromJson(incoming, AssignTeammateRequest.class);
    AssignTeammateResponse response = handler.handleRequest(req, createContext("create teammate"));

    assertEquals(outgoing, response.statusCode);
  }

  void testUnassignInput(String incoming, int outgoing) throws Exception {
    UnassignTeammateHandler handler = new UnassignTeammateHandler();
    UnassignTeammateRequest req = new Gson().fromJson(incoming, UnassignTeammateRequest.class);
    UnassignTeammateResponse response = handler.handleRequest(req, createContext("create teammate"));

    assertEquals(outgoing, response.statusCode);
  }

  @Test
  public void assignUnassignTeammateTestPasses() throws Exception {

    ProjectDAO projectDAO = new ProjectDAO();
    TeammateDAO teammateDAO = new TeammateDAO();
    TaskDAO taskDAO = new TaskDAO();

    projectDAO.addProject(new Project("new proj"));
    Project project = projectDAO.getProject("new proj");
    String projectid = project.id.toString();

    teammateDAO.addTeammate(new Teammate("TestDelete Teammate"), projectid);
    Teammate teammate = teammateDAO.getTeammateByNameAndProjectId("TestDelete Teammate", projectid);
    String teammateid = teammate.id.toString();

    taskDAO.addTask(new Task("TestDelete Teammate"), null, projectid);
    Task task = taskDAO.getTaskByTitleAndProjectId("TestDelete Teammate", projectid);
    String taskid = task.id.toString();

    String SAMPLE_INPUT_STRING = "{\"projectid\": \"" + projectid + "\", \"taskid\": \"" + taskid
        + "\", \"teammateid\": \"" + teammateid + "\" }";
    int RESULT = 200;

    try {
      testAssignInput(SAMPLE_INPUT_STRING, RESULT);
      testUnassignInput(SAMPLE_INPUT_STRING, RESULT);
      projectDAO.deleteProject(projectid);
    } catch (Exception ioe) {
      Assert.fail("invalid: " + ioe.getMessage());
    }
  }
}
