package projectmanager.http;

import projectmanager.model.Project;

public class ProjectResponse {
	public Project project;
	public int statusCode;
	public String error;
	
	public ProjectResponse(Project project, int statusCode) {
		this.project = project;
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public ProjectResponse(int statusCode, String error) {
		this.project = null;
		this.statusCode = statusCode;
		this.error = error;
	}
}
