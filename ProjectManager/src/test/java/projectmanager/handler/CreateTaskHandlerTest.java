package projectmanager.handler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import projectmanager.http.CreateTaskRequest;
import projectmanager.http.CreateTaskResponse;

public class CreateTaskHandlerTest extends LambdaTest {

	Map<String, Object> testInput(String incoming, int outgoing) throws IOException {
		CreateTaskHandler handler = new CreateTaskHandler();
		CreateTaskRequest req = new Gson().fromJson(incoming, CreateTaskRequest.class);
		CreateTaskResponse response = handler.handleRequest(req, createContext("create task"));
		assertEquals(outgoing, response.statusCode);
		return response.task;
	}

	@Test
	public void createTaskTestPasses() {
		String title = "test13";
		String projectid = "107d139a-9a1d-42e3-9f59-b61a93e6c7a3";
		String parentTask = "043d4745-8056-4ba0-a436-0fe954883341";
		String SAMPLE_INPUT_STRING = "{\"title\": \"" + title + "\",\"projectid\": \"" + projectid
				+ "\", \"parentTask\": \"" + parentTask + "\"}";
		int RESULT = 200;

		try {
			Map<String, Object> task = testInput(SAMPLE_INPUT_STRING, RESULT);
			assertEquals(task.get("title"), title);
			assertEquals(task.get("projectid"), projectid);
			assertEquals(task.get("parentTask"), parentTask);
		} catch (IOException ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
	}

	@Test
	public void createTaskTestFails() {
		String SAMPLE_INPUT_STRING = "{\"title\": \"testFail\",\"projectid\": \"107d139a-9a1d-42e3-9f59-b61a93e6c7a3\"}";
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

	@Test
	public void createTaskBadTaskTestFails() {
		String SAMPLE_INPUT_STRING = "{\"title\": \"testFail\",\"parentTask\": \"1\"}";
		int RESULT = 400;

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT);
		} catch (IOException ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
	}

}