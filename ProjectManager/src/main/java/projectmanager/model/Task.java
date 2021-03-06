package projectmanager.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Task {
	public final UUID id;
	public String title;
	public String outlineNumber;
	public Boolean completed;
	public List<String> assignees;
	public List<Task> subTasks;
	public Task parentTask;
	public String projectid;

	public Task(String title) {
		this.id = UUID.randomUUID();
		this.title = title;
		this.outlineNumber = "1";
		this.completed = false;
		this.assignees = new ArrayList<String>();
		this.subTasks = new ArrayList<Task>();
		this.parentTask = null;
		this.projectid = null;
	}

	public Task(UUID id, String title, String outlineNumber, Boolean completed, String projectid) {
		this.id = id;
		this.title = title;
		this.outlineNumber = outlineNumber;
		this.completed = completed;
		this.assignees = new ArrayList<String>();
		this.subTasks = new ArrayList<Task>();
		this.parentTask = null;
		this.projectid = projectid;
	}

	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}

	public void addTeammate(String teammateId) {
		this.assignees.add(teammateId);
	}

	public void removeTeammate(String teammateId) {
		this.assignees.remove(teammateId);
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