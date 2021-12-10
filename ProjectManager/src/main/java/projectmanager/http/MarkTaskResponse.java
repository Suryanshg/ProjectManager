package projectmanager.http;

public class MarkTaskResponse {
	
	public int statusCode;
	public String error;
	
	// Success	TODO: finishing success and failure
	public MarkTaskResponse(int statusCode) {
		this.statusCode = statusCode;
		this.error = "";
	}

	// Failure
	public MarkTaskResponse(int statusCode, String error) {
		
		this.statusCode = statusCode;
		this.error = error;
	}
}
