package projectmanager.http;

public class DeleteProjectResponse {
	public boolean deleted;
	public String error;
	public int statusCode;
	
	// Success
	public DeleteProjectResponse(boolean deleted, int statusCode) {
		this.deleted = deleted;
		this.statusCode = statusCode;
		this.error = "";
	}
	
	// Failure
	public DeleteProjectResponse(int statusCode, String error) {
		this.statusCode = statusCode;
		this.error = error;
		this.deleted = false;
	}
}
