package projectmanager.http;

public class RenameTaskResponse {
	
	public int statusCode;
	public String error;
	
	// Success  TODO: finishing success and failure :)
	public RenameTaskResponse(int statusCode) {
		this.statusCode = statusCode;
		this.error = "";
	}

	// Failure
	public RenameTaskResponse(int statusCode, String error) {
		this.statusCode = statusCode;
		this.error = error;
	}
}
