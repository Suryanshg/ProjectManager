package projectmanager.http;

/** To work with AWS must not have final attributes, must have no-arg constructor, and all get/set methods. */
public class DeleteProjectRequest {
	
	public String projectid;
	
	public DeleteProjectRequest() {
		
	}
	
	public DeleteProjectRequest(String projectid) {
		this.projectid = projectid;
	}
	
	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}
	
	public String getProjectid() {
		return this.projectid;
	}
	
	public String toString() {
		return "DeleteProjectRequest(" + projectid + ")";
	}

}
