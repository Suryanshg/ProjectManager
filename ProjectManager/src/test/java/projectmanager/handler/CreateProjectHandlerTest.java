package projectmanager.handler;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import projectmanager.db.ProjectDAO;
import projectmanager.http.CreateProjectRequest;
import projectmanager.http.CreateProjectResponse;

public class CreateProjectHandlerTest extends LambdaTest {

	void testInput(String incoming, int outgoing) throws Exception {
		CreateProjectHandler handler = new CreateProjectHandler();
		CreateProjectRequest req = new Gson().fromJson(incoming, CreateProjectRequest.class);
		CreateProjectResponse response = handler.handleRequest(req, createContext("create project"));

		assertEquals(outgoing, response.statusCode);
		
		if(response.statusCode == 200) { // Delete the newly created project
			ProjectDAO dao = new ProjectDAO();
			dao.deleteProject(response.projectId);
		}
		
	}

	@Test
	public void createProjectTestPasses(){  
		String SAMPLE_INPUT_STRING = "{\"projectName\": \"test60\" }";
		int RESULT = 200;

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT);
		} catch(Exception ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
	}
	
	
	@Test
	public void createProjectTestFails() {
		String SAMPLE_INPUT_STRING = "{\"projectName\": \"test900\" }";
		int RESULT = 422;
		
		try {
			testInput(SAMPLE_INPUT_STRING, RESULT);
		} catch (Exception ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
	}

}