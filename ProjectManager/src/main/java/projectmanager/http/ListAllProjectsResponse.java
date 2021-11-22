package projectmanager.http;

import java.util.ArrayList;
import java.util.List;

import projectmanager.model.Project;

public class ListAllProjectsResponse {
	public String error;
	public int statusCode;
	public List<Project> projects;
	
	public ListAllProjectsResponse(List<Project> projects, int statusCode) {
		this.projects  = projects;
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public ListAllProjectsResponse(int statusCode, String error) {
		this.projects = new ArrayList<Project>();
		this.statusCode = statusCode;
		this.error = error;
	}
	
}
