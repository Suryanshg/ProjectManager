package projectmanager.http;

/**
 * To work with AWS must not have final attributes, must have no-arg
 * constructor, and all get/set methods.
 */
public class CreateProjectRequest {
	String projectName;

	// No-args constructor
	public CreateProjectRequest() {
	}

	public CreateProjectRequest(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String toString() {
		return "CreateProjectRequest(" + projectName + ")";
	}
}
