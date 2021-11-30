package projectmanager.http;

/**
 * To work with AWS must not have final attributes, must have no-arg
 * constructor, and all get/set methods.
 */
public class CreateTaskRequest {
	String title;
	String projectId;
	String parentId;

	// No-args constructor
	public CreateTaskRequest() {
	}

	public CreateTaskRequest(String title, String projectId, String parentId) {
		this.title = title;
		this.projectId = projectId;
		this.parentId = parentId;
	}

	public CreateTaskRequest(String title, String projectId) {
		this.title = title;
		this.projectId = projectId;
		this.parentId = null;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProjectId() {
		return this.projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String toString() {
		return "CreateTaskRequest(" + title + ")";
	}
}
