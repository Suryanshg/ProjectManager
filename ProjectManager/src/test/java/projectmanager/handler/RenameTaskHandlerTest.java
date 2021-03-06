package projectmanager.handler;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import projectmanager.db.TaskDAO;
import projectmanager.http.RenameTaskRequest;
import projectmanager.http.RenameTaskResponse;
import projectmanager.model.Task;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class RenameTaskHandlerTest extends LambdaTest {

	void testInput(String incoming, int outgoing) throws IOException {
		RenameTaskHandler handler = new RenameTaskHandler();
		RenameTaskRequest req = new Gson().fromJson(incoming, RenameTaskRequest.class);
		RenameTaskResponse response = handler.handleRequest(req, createContext("rename task"));
		System.out.println(response.error);
		assertEquals(outgoing, response.statusCode);
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

	String getTaskName(String id) throws Exception {
		TaskDAO dao = new TaskDAO();
		Task task = null;
		try {
			task = dao.getTaskById(id);
		} catch (Exception e) {
			assertEquals(false, true);
		}
		return task.title;
	}
	
	
	@Test
	public void RenameTaskSucceeds() throws Exception{
		// Create test task
		String id = createTask("testRename2", "24ba3f65-a766-4cd4-a8b8-d9e9c55a1939", null);
		// Rename task...
		String sample = "{\"taskid\": \"" + id + "\", \"name\": \"testRename3\"}";
		int result = 200;

		try {
			testInput(sample, result);
			assertEquals("testRename3", getTaskName(id));
			deleteTask(id);
		} catch (Exception e) {
			Assert.fail("Test failed with exception: " + e.getMessage());
		}
	}
	
	@Test
	public void RenameTaskFails() throws Exception{
		String sample = "{\"taskid\": \"anidthatreallydoesntexist\", \"name\": \"testRename2\"}";
		int result = 422;

		try {
			testInput(sample, result);
		} catch (Exception e) {
			Assert.fail("Test failed with exception: " + e.getMessage());
		}
	}
}

