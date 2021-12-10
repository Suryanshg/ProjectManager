package projectmanager.http;

public class MarkTaskRequest {
	String taskid;
	boolean completed;
	
	//No-args constructor
	public MarkTaskRequest() {}
	
	public MarkTaskRequest(String taskid, boolean completed) {
		this.taskid = taskid;
		this.completed = completed;
	}

	public String getTaskid() {
		return this.taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public boolean getCompleted() {
		return this.completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
}
