package projectmanager.http;

public class ArchiveProjectResponse {
	public boolean archived;
	public String error;
	public int statusCode;
	
	// Success
	public ArchiveProjectResponse(boolean archived, int statusCode) {
		this.archived = archived;
		this.statusCode = statusCode;
		this.error = "";
	}
	
	// Failure
	public ArchiveProjectResponse(int statusCode, String error) {
		this.statusCode = statusCode;
		this.error = error;
		this.archived = false;
	}
}
