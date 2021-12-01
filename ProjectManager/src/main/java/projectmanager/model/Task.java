package projectmanager.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Task {
	public final UUID id;
	public String title;
	public String outlineNumber;
	public Boolean completed;
	public List<Teammate> assignees;
	public List<Task> subTasks;
	public Task parentTask;

	public Task(String title) {
		this.id = UUID.randomUUID();
		this.title = title;
		this.outlineNumber = "1";
		this.completed = false;
		this.assignees = new ArrayList<Teammate>();
		this.subTasks = new ArrayList<Task>();
		this.parentTask = null;
	}

	public Task(UUID id, String title, String outlineNumber, Boolean completed) {
		this.id = id;
		this.title = title;
		this.outlineNumber = outlineNumber;
		this.completed = completed;
		this.assignees = new ArrayList<Teammate>();
		this.subTasks = new ArrayList<Task>();
		this.parentTask = null;
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