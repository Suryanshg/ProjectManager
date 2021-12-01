package projectmanager.handler;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import projectmanager.http.CreateTaskRequest;
import projectmanager.http.CreateTaskResponse;

public class CreateTaskHandlerTest extends LambdaTest {

	void testInput(String incoming, int outgoing) throws IOException {
		CreateTaskHandler handler = new CreateTaskHandler();
		CreateTaskRequest req = new Gson().fromJson(incoming, CreateTaskRequest.class);
		CreateTaskResponse response = handler.handleRequest(req, createContext("create task"));
		assertEquals(outgoing, response.statusCode);
		// TODO: Add code delete the newly created task
	}

	@Test
	public void createTaskTestPasses() {
		String SAMPLE_INPUT_STRING = "{\"title\": \"testPass\",\"projectId\": \"107d139a-9a1d-42e3-9f59-b61a93e6c7a3\"}";
		int RESULT = 200;

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT);
		} catch (IOException ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
	}

	@Test
	public void createTaskTestFails() {
		String SAMPLE_INPUT_STRING = "{\"title\": \"testFail\",\"projectId\": \"107d139a-9a1d-42e3-9f59-b61a93e6c7a3\"}";
		int RESULT = 422;

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT);
		} catch (IOException ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
	}

	@Test
	public void createTaskBadProjectTestFails() {
		String SAMPLE_INPUT_STRING = "{\"title\": \"testFail\",\"projectId\": \"1\"}";
		int RESULT = 400;

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT);
		} catch (IOException ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
	}

}