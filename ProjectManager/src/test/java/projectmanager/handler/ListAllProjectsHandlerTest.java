package projectmanager.handler;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import projectmanager.http.ListAllProjectsResponse;
import projectmanager.http.ProjectRequest;
import projectmanager.http.ProjectResponse;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ListAllProjectsHandlerTest extends LambdaTest {
	
	void testInput(int outgoing) throws IOException {
		ListAllProjectsHandler handler = new ListAllProjectsHandler();
		ListAllProjectsResponse response = handler.handleRequest(new Object(), createContext("list all projects"));

		assertEquals(outgoing, response.statusCode);
	}



    @Test
    public void testListAllProjectsHandler() {
    	
		int RESULT = 200;

		try {
			testInput(RESULT);
		} catch(IOException ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
    }
}
