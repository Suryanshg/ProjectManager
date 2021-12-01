package projectmanager.http;

/**
 * To work with AWS must not have final attributes, must have no-arg
 * constructor, and all get/set methods.
 */
public class CreateTaskRequest {
	String title;
	String projectid;
	String parentTask;

	// No-args constructor
	public CreateTaskRequest() {
	}

	public CreateTaskRequest(String title, String projectid, String parentTask) {
		this.title = title;
		this.projectid = projectid;
		this.parentTask = parentTask;
	}

	public CreateTaskRequest(String title, String projectid) {
		this.title = title;
		this.projectid = projectid;
		this.parentTask = null;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProjectid() {
		return this.projectid;
	}

	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}

	public String getParentTask() {
		return this.parentTask;
	}

	public void setParentTask(String parentTask) {
		this.parentTask = parentTask;
	}

	public String toString() {
		return "CreateTaskRequest(" + title + " " + projectid + " " + parentTask + ")";
	}
}
