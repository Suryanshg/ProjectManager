package projectmanager.model;

import java.util.ArrayList;
import java.util.UUID;

public class Task {
	public final UUID id;
	public String title;
	public String outlineNumber;
	public Boolean completed; //  Using Boolean instead of boolean because it can store null values as well
	public ArrayList<Teammate> assignees;
	public ArrayList<Task> subTasks;
	public Task parentTask; // will be null for the top-level task
	
	// Might need more overloading constructors in the future
	public Task(UUID id, String title, String outlineNumber, Boolean completed, Task parentTask) {
		this.id = id;
		this.title = title;
		this.outlineNumber = outlineNumber;
		this.completed = completed;
		this.assignees = new ArrayList<Teammate>();
		this.subTasks = new ArrayList<Task>();
		this.parentTask = parentTask;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		
		if (o instanceof Task) {
			Task other = (Task) o;
			return id.equals(other.id);
		}
		
		return false;
	}
	

}
