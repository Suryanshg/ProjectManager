package projectmanager.http;

public class CreateTaskResponse {
	public String taskId;
	public int statusCode;
	public String error;

	// public CreateTaskResponse(String taskId, String url) {
	// this.taskId = taskId;
	// this.url = url;
	// }

	// Success
	public CreateTaskResponse(String taskId, int statusCode) {
		this.taskId = taskId;
		this.statusCode = statusCode;
		this.error = "";
	}

	// Failure
	public CreateTaskResponse(int statusCode, String error) {
		this.taskId = "";
		this.statusCode = statusCode;
		this.error = error;
	}
}