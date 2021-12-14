package projectmanager.http;

import projectmanager.model.Task;

import java.util.List;

public class CreateTaskResponse {
	public List<Task> task;
	public int statusCode;
	public String error;

	// Success
	public CreateTaskResponse(List<Task> task, int statusCode) {
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