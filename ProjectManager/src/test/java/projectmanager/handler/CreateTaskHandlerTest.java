package projectmanager.handler;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import projectmanager.db.TaskDAO;
import projectmanager.http.CreateTaskRequest;
import projectmanager.http.CreateTaskResponse;
import projectmanager.model.Task;

public class CreateTaskHandlerTest extends LambdaTest {

	Task testInput(String incoming, int outgoing) throws IOException {
		CreateTaskHandler handler = new CreateTaskHandler();
		CreateTaskRequest req = new Gson().fromJson(incoming, CreateTaskRequest.class);
		CreateTaskResponse response = handler.handleRequest(req, createContext("create task"));
		assertEquals(outgoing, response.statusCode);
		// System.out.println(response.error);
		return response.task;
	}

	void deleteTask(String id) throws IOException {
		TaskDAO dao = new TaskDAO();
		Boolean result;
		try {
			result = dao.deleteTask(id);
			assertEquals(result, true);
		} catch (Exception e) {
			assertEquals(false, true); // always fails
		}
	}

	@Test
	public void createTaskTestPasses() {
		String title = "test134";
		String projectid = "107d139a-9a1d-42e3-9f59-b61a93e6c7a3";
		String parentTask = null;
		String SAMPLE_INPUT_STRING = "{\"title\": \"" + title + "\",\"projectid\": \"" + projectid
				+ "\", \"parentTask\": " + parentTask + "}";
		int RESULT = 200;

		try {
			Task task = testInput(SAMPLE_INPUT_STRING, RESULT);
			assertEquals(task.title, title);
			assertEquals(task.outlineNumber, "1");
			deleteTask(task.id.toString());
		} catch (IOException ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
	}

	@Test
	public void createTaskTestFails() throws Exception {
		// Let's create a Task with the same title and projectid in the DB
		TaskDAO dao = new TaskDAO();
		Task task = new Task("testFail");
		dao.addTask(task, null, "107d139a-9a1d-42e3-9f59-b61a93e6c7a3");

		String SAMPLE_INPUT_STRING = "{\"title\": \"testFail\",\"projectid\": \"107d139a-9a1d-42e3-9f59-b61a93e6c7a3\"}";
		int RESULT = 422;

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT);
			dao.deleteTask(task.id.toString());
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