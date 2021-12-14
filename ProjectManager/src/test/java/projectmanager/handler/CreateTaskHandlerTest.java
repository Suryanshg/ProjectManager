package projectmanager.handler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import projectmanager.db.ProjectDAO;
import projectmanager.db.TaskDAO;
import projectmanager.http.CreateTaskRequest;
import projectmanager.http.CreateTaskResponse;
import projectmanager.model.Task;

public class CreateTaskHandlerTest extends LambdaTest {

	List<Task> testInput(String incoming, int outgoing) throws IOException {
		CreateTaskHandler handler = new CreateTaskHandler();
		CreateTaskRequest req = new Gson().fromJson(incoming, CreateTaskRequest.class);
		CreateTaskResponse response = handler.handleRequest(req, createContext("create task"));
		System.out.println(response.error);
		assertEquals(outgoing, response.statusCode);
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
		String title = "test139";
		String projectid = "0bc22c80-a9d6-43a1-b1f2-7fba045eae0b";
		String parentTask = null;
		String SAMPLE_INPUT_STRING = "{\"title\": \"" + title + "\",\"projectid\": \"" + projectid
				+ "\", \"parentTask\": " + parentTask + "}";
		int RESULT = 200;

		try {
			List<Task> task = testInput(SAMPLE_INPUT_STRING, RESULT);
			assertEquals(task.get(0).title, title);
			deleteTask(task.get(0).id.toString());
		} catch (IOException ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
	}

	@Test
	public void createTaskTestFails() throws Exception {
		// Let's create a Task with the same title and projectid in the DB
		TaskDAO dao = new TaskDAO();
		Task task = new Task("testFail");
		dao.addTask(task, null, "0bc22c80-a9d6-43a1-b1f2-7fba045eae0b");

		String SAMPLE_INPUT_STRING = "{\"title\": \"testFail\",\"projectid\": \"0bc22c80-a9d6-43a1-b1f2-7fba045eae0b\"}";
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
		String SAMPLE_INPUT_STRING = "{\"title\": \"testFail\",\"projectId\": \"aaaaaaaaaaaaaaaa\"}";
		int RESULT = 400;

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT);
		} catch (IOException ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
	}

	@Test
	public void createTaskBadTaskTestFails() {
		String SAMPLE_INPUT_STRING = "{\"title\": \"testFail\",\"parentTask\": \"1\",\"projectid\": \"0bc22c80-a9d6-43a1-b1f2-7fba045eae0b\"}";
		int RESULT = 400;

		try {
			testInput(SAMPLE_INPUT_STRING, RESULT);
		} catch (IOException ioe) {
			Assert.fail("invalid: " + ioe.getMessage());
		}
	}

	@Test
	public void createTaskWithDecomp() {
		try {
			// Setup - Delete all tasks, create decomp task, then assign alice, bob, and charlie to the task.
			// Alice is 8607f0b6-bd9a-4786-8731-14222d7efe23
			// Bob is dea87398-62ca-4cfc-bfb6-ee5cffae2b03
			// Charlie is a56705f8-69a1-48ae-aba2-f295e87ce1ae
			TaskDAO tdao = new TaskDAO();
			List<Task> previous_tasks = tdao.getTasksByProject("134beb90-b707-41e5-9f9f-88a29ea7655c");

			for (Task task : previous_tasks) {
				tdao.deleteTask(String.valueOf(task.id));
			}
			Task tltask = new Task("Top level task");
			tdao.addTask(tltask, null, "134beb90-b707-41e5-9f9f-88a29ea7655c");
			// Then test the input.
			String SAMPLE_INPUT_STRING = "{\"title\": \"Decompose1\\nDecompose2\\nDecompose3\",\"parentTask\": \"" + String.valueOf(tltask.id) + "\", \"projectid\": \"134beb90-b707-41e5-9f9f-88a29ea7655c\"}";
			int RESULT = 200;
			List<Task> tasks = testInput(SAMPLE_INPUT_STRING, RESULT);

			assertEquals("Decompose1", tasks.get(0).title);
			assertEquals("8607f0b6-bd9a-4786-8731-14222d7efe23", tasks.get(0).assignees.get(0));

			assertEquals("Decompose2", tasks.get(0).title);
			assertEquals("a56705f8-69a1-48ae-aba2-f295e87ce1ae", tasks.get(0).assignees.get(0));

			assertEquals("Decompose3", tasks.get(0).title);
			assertEquals("dea87398-62ca-4cfc-bfb6-ee5cffae2b03", tasks.get(0).assignees.get(0));
		} catch (Exception e) {
			Assert.fail("Failed: " + e.getMessage());
		}
	}

}