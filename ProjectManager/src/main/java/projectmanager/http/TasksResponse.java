package projectmanager.http;


import java.util.ArrayList;
import java.util.List;

import projectmanager.model.Task;

public class TasksResponse {
	
	public List<Task> tasks;
	public int statusCode;
	public String error;
	
	// Success
	public TasksResponse(List<Task> tasks, int statusCode) {
		this.tasks = tasks;
		this.error = "";
		this.statusCode = 200;
	}
	
	// Failure
	public TasksResponse(int statusCode, String error) {
		this.error = error;
		this.statusCode = statusCode;
		this.tasks = new ArrayList<Task>();
	}

}
