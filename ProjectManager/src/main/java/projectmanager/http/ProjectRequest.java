package projectmanager.http;

/** To work with AWS must not have final attributes, must have no-arg constructor, and all get/set methods. */
public class ProjectRequest {
	String project; // This is the project id
	
	public ProjectRequest(){
		
	}
	
	public ProjectRequest(String project) {
		this.project = project;
	}
	
	public void setProject(String project) {
		this.project = project;
	}
	
	public String getProject() {
		return project;
	}
	
	public String toString() {
		return "ProjectRequest(" + project + ")";
	}
}
