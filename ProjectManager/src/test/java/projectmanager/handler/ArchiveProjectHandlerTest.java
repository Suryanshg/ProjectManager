package projectmanager.handler;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import projectmanager.db.ProjectDAO;
import projectmanager.http.ArchiveProjectRequest;
import projectmanager.http.ArchiveProjectResponse;
import projectmanager.http.DeleteProjectRequest;
import projectmanager.http.DeleteProjectResponse;
import projectmanager.model.Project;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ArchiveProjectHandlerTest extends LambdaTest {

	void testInput(String incoming, int outgoing, boolean archived) throws Exception {

		ArchiveProjectHandler handler = new ArchiveProjectHandler();
		ArchiveProjectRequest req = new Gson().fromJson(incoming, ArchiveProjectRequest.class);
		ArchiveProjectResponse response = handler.handleRequest(req, createContext("archive project"));

		assertEquals(outgoing, response.statusCode);
		if (response.statusCode == 200)
			assertEquals(archived, response.archived);
	}

	@Test
	public void archiveProjectTestPasses() throws Exception {

		// Create a dummy project
		ProjectDAO dao = new ProjectDAO();
		dao.addProject(new Project("testpass"));

		Project testProject = dao.getProject("testpass");

		String projectid = testProject.id.toString();
		String SAMPLE_INPUT_STRING = "{\"projectid\": \"" + projectid + "\" }";
		int RESULT = 200;

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT, true);

			// Delete the newly created project
			dao.deleteProject(projectid);
		} catch (Exception ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
	}

	@Test
	public void archivedMiddlewareTest() throws Exception {

		// Create a dummy project
		ProjectDAO dao = new ProjectDAO();
		dao.addProject(new Project("testpass"));

		Project testProject = dao.getProject("testpass");

		String projectid = testProject.id.toString();
		String SAMPLE_INPUT_STRING = "{\"projectid\": \"" + projectid + "\" }";
		int RESULT = 200;

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT, true);
			RESULT = 409;
			testInput(SAMPLE_INPUT_STRING, RESULT, true);

			// Delete the newly created project
			dao.deleteProject(projectid);
		} catch (Exception ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
	}

	@Test
	public void archiveProjectTestFails() throws Exception {

		String projectid = "GARBAGE"; // testProject.id.toString();
		String SAMPLE_INPUT_STRING = "{\"projectid\": \"" + projectid + "\" }";
		int RESULT = 422;

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT, false);
		} catch (Exception ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
	}

}
