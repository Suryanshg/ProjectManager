package projectmanager.http;

import java.util.Map;

import projectmanager.model.Task;

public class CreateTaskResponse {
	public Map<String, Object> task;
	public int statusCode;
	public String error;

	// public CreateTaskResponse(String taskId, String url) {
	// this.taskId = taskId;
	// this.url = url;
	// }

	// Success
	public CreateTaskResponse(Task task, int statusCode) {
		this.task = task.getResponseMap();
		this.statusCode = statusCode;
		this.error = "";
	}

	// Failure
	public CreateTaskResponse(int statusCode, String error) {
		this.task = null;
		this.statusCode = statusCode;
		this.error = error;
	}
}