package projectmanager.http;

import projectmanager.model.Task;

public class CreateTaskResponse {
	public Task task;
	public int statusCode;
	public String error;

	// public CreateTaskResponse(String taskId, String url) {
	// this.taskId = taskId;
	// this.url = url;
	// }

	// Success
	public CreateTaskResponse(Task task, int statusCode) {
		this.task = task;
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