package projectmanager.handler;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.UUID;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import projectmanager.http.CreateProjectRequest;
import projectmanager.http.CreateProjectResponse;
import projectmanager.http.ProjectRequest;
import projectmanager.http.ProjectResponse;
import projectmanager.model.Project;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class GetProjectHandlerTest extends LambdaTest {

	void testInput(String incoming, int outgoing, Project expected) throws IOException {
		GetProjectHandler handler = new GetProjectHandler();
		ProjectRequest req = new Gson().fromJson(incoming, ProjectRequest.class);
		ProjectResponse response = handler.handleRequest(req, createContext("get project"));

		assertEquals(outgoing, response.statusCode);
		assertEquals(expected, response.project);
		
//		System.out.println(response.toString());
	}


    @Test
    public void testGetProjectHandler200() {
    	
    	String testId = "0bc22c80-a9d6-43a1-b1f2-7fba045eae0b";
    	String testName = "erre";
    	String SAMPLE_INPUT_STRING = "{\"project\": \"" + testId + "\" }";
    	
    	Project testProject = new Project(UUID.fromString(testId), testName);
		int RESULT = 200;

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT, testProject);
			
		} catch(IOException ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
    }
    
    
    @Test
    public void testGetProjectHandler422() {
    	
    	String testId = "GARBAGE";
    	String SAMPLE_INPUT_STRING = "{\"project\": \"" + testId + "\" }";
		int RESULT = 422;

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT, null);
		} catch(IOException ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
    }
}
