package projectmanager.model;

import java.util.ArrayList;
import java.util.UUID;

public class Task {
	public final UUID id;
	public String title;
	public Integer outlineNumber;
	public Boolean completed;
	public ArrayList<Teammate> assignees;
	public ArrayList<Task> subTasks;
	public Task parentTask;

	public Task(String title) {
		this.id = UUID.randomUUID();
		this.title = title;
		this.outlineNumber = 0; // TODO is 0 correct?
		this.completed = false;
		this.assignees = new ArrayList<Teammate>();
		this.subTasks = new ArrayList<Task>();
		this.parentTask = parentTask;
	}

	public Task(UUID id, String title, Integer outlineNumber, Boolean completed, Task parentTask) {
		this.id = id;
		this.title = title;
		this.outlineNumber = outlineNumber;
		this.completed = completed;
		this.assignees = new ArrayList<Teammate>();
		this.subTasks = new ArrayList<Task>();
		this.parentTask = parentTask;
	}

	public void addTeammate(Teammate teammate) {
		this.assignees.add(teammate);
	}

	public void removeTeammate(Teammate teammate) {
		this.assignees.remove(teammate);
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public void setSubTasks(ArrayList<Task> subTasks) {
		this.subTasks = subTasks;
	}

	public void addSubTask(Task subTask) {
		this.subTasks.add(subTask);
	}

	public void renameTask(String title) {
		this.title = title;
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
