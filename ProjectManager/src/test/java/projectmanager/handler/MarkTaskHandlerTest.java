package projectmanager.handler;

import static org.junit.Assert.*;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import projectmanager.db.TaskDAO;
import projectmanager.http.MarkTaskRequest;
import projectmanager.http.MarkTaskResponse;
import projectmanager.model.Task;

import java.io.IOException;

public class MarkTaskHandlerTest extends LambdaTest {

	void testInput(String incoming, int outgoing) {
		MarkTaskHandler handler = new MarkTaskHandler();
		MarkTaskRequest req = new Gson().fromJson(incoming, MarkTaskRequest.class);
		MarkTaskResponse response = handler.handleRequest(req, createContext("mark task"));
		assertEquals(outgoing, response.statusCode);
	}

	String createTask(String name, String projectid, String parentTask) {
		TaskDAO dao = new TaskDAO();
		Task task = new Task(name);
		Boolean result;
		try {
			result = dao.addTask(task, parentTask, projectid);
		} catch (Exception e) {
			assertEquals(false, true);
		}

		return String.valueOf(task.id);
	}

	Boolean getTaskState(String id) throws Exception {
		TaskDAO dao = new TaskDAO();
		Task task = dao.getTaskById(id);
		return task.completed;
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
	public void MarkTaskComplete() {
		// Create a test task
		String id = createTask("testTask", "0bc22c80-a9d6-43a1-b1f2-7fba045eae0b", null);
		// Mark that task as complete & verify input
		String sample = "{\"taskid\": \"" + id + "\", \"completed\": \"true\"}";
		int result = 200;
		try {
			testInput(sample, result);
			assertEquals(true, getTaskState(id));
			deleteTask(id);
		} catch (Exception e) {
			Assert.fail("Test failed with exception: " + e.getMessage());
		}
	}

	@Test
	public void MarkTaskIncomplete() {
		// Create a test task
		String id = createTask("testTask", "0bc22c80-a9d6-43a1-b1f2-7fba045eae0b", null);
		// Mark that task as complete, then incomplete & verify input
		String sample_true = "{\"taskid\": \"" + id + "\", \"completed\": \"true\"}";
		String sample_false = "{\"taskid\": \"" + id + "\", \"completed\": \"false\"}";
		int result = 200;
		try {
			testInput(sample_true, result);
			assertEquals(true, getTaskState(id));
			testInput(sample_false, result);
			assertEquals(false, getTaskState(id));
			deleteTask(id);
		} catch (Exception e) {
			Assert.fail("Test failed with exception: " + e.getMessage());
		}
	}

	@Test
	public void MarkTaskCycle() {
		// Create a test task
		String id = createTask("testTask", "0bc22c80-a9d6-43a1-b1f2-7fba045eae0b", null);
		// Mark that task as complete, then incomplete & verify input
		String sample_true = "{\"taskid\": \"" + id + "\", \"completed\": \"true\"}";
		String sample_false = "{\"taskid\": \"" + id + "\", \"completed\": \"false\"}";
		int result = 200;
		try {
			testInput(sample_true, result);
			assertEquals(true, getTaskState(id));
			testInput(sample_false, result);
			assertEquals(false, getTaskState(id));
			testInput(sample_true, result);
			assertEquals(true, getTaskState(id));
			deleteTask(id);
		} catch (Exception e) {
			Assert.fail("Test failed with exception: " + e.getMessage());
		}
	}
	
	@Test
	public void MarkTaskFail() {
		String sample = "{\"taskid\": \"iddoesntexist\", \"completed\": \"true\"}";
		int result = 422;

		try {
			testInput(sample, result);
		} catch (Exception e) {
			Assert.fail("Test failed with exception: " + e.getMessage());
		}
	}

}
