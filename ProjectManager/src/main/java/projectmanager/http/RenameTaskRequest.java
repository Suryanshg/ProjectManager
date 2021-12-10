package projectmanager.http;

public class RenameTaskRequest {
	
	String taskid;
	String name;
	
	//No-args constructor
	public RenameTaskRequest() {}
	
	public RenameTaskRequest(String taskid, String name) {
		this.taskid = taskid;
		this.name = name;
	}
	
	public String getTaskid() {
		return this.taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
	    return "RenameTaskRequest(Task:" + taskid + ", Name:" + name + ")";
	  }
}
