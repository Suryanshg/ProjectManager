package projectmanager.handler;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.UUID;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import projectmanager.db.ProjectDAO;
import projectmanager.http.CreateProjectRequest;
import projectmanager.http.CreateProjectResponse;
import projectmanager.http.DeleteProjectRequest;
import projectmanager.http.DeleteProjectResponse;
import projectmanager.model.Project;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class DeleteProjectHandlerTest extends LambdaTest{
	
	
	void testInput(String incoming, int outgoing, boolean deleted) throws Exception {

		DeleteProjectHandler handler = new DeleteProjectHandler();
		DeleteProjectRequest req = new Gson().fromJson(incoming, DeleteProjectRequest.class);
		DeleteProjectResponse response = handler.handleRequest(req, createContext("delete project"));

		assertEquals(outgoing, response.statusCode);
		assertEquals(deleted, response.deleted);
	}
	
	@Test
	public void deleteProjectTestPasses() throws Exception{ 
		
		// Let's make a dummy project with the specified id
		ProjectDAO dao = new ProjectDAO();
		dao.addProject(new Project("testpass"));
		
		Project testProject = dao.getProject("testpass");
		
		String projectid = testProject.id.toString();
		String SAMPLE_INPUT_STRING = "{\"projectid\": \"" + projectid + "\" }";
		int RESULT = 200;

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT, true);
		} catch(Exception ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
	}
	
	@Test
	public void deleteProjectTestFails() throws Exception{ 
		
		// It is not possible that there is a project with this projectid
		String projectid = "test";
		String SAMPLE_INPUT_STRING = "{\"projectid\": \"" + projectid + "\" }";
		int RESULT = 422;

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT, false);
		} catch(Exception ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
	}






}
