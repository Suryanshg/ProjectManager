package projectmanager.http;

public class CreateProjectResponse {
	public String projectId;
	public String url;
	public int statusCode;
	public String error;
	
//	public CreateProjectResponse(String projectId, String url) {
//		this.projectId = projectId;
//		this.url = url;
//	}
	
	// Success
	public CreateProjectResponse(String projectId, String url, int statusCode) {
		this.projectId = projectId;
		this.url = url;
		this.statusCode = statusCode;
		this.error = "";
	}
	
	// Failure
	public CreateProjectResponse(int statusCode, String error) {
		this.projectId = "";
		this.url = "";
		this.statusCode = statusCode;
		this.error = error;
	}
	
//	public String toString() {
//		if (statusCode == 200) {
//			return "CreateProjectResponse(" + projectId + "," + url + ")";
//		} else {
//			return "Error(" + statusCode + ", err=" + error + ")";
//		}
//	}
}
