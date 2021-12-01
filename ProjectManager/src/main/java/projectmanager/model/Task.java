package projectmanager.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;

public class Task {
	public final UUID id;
	public String title;
	public String outlineNumber;
	public Boolean completed;
	public ArrayList<Teammate> assignees;
	public ArrayList<Task> subTasks;
	public String parentTask;
	public String projectid;

	public Task(String title, String parentTask, String projectid) {
		this.id = UUID.randomUUID();
		this.title = title;
		this.outlineNumber = "1";
		this.completed = false;
		this.assignees = new ArrayList<Teammate>();
		this.subTasks = new ArrayList<Task>();
		this.parentTask = parentTask;
		this.projectid = projectid;
	}

	public Task(UUID id, String title, String outlineNumber, Boolean completed, String parentTask, String projectid) {
		this.id = id;
		this.title = title;
		this.outlineNumber = outlineNumber;
		this.completed = completed;
		this.assignees = new ArrayList<Teammate>();
		this.subTasks = new ArrayList<Task>();
		this.parentTask = parentTask;
		this.projectid = projectid;
	}

	public Map<String, Object> getResponseMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("outlineNumber", outlineNumber);
		map.put("completed", completed);
		map.put("parentTask", parentTask);
		map.put("projectid", projectid);
		return map;
	}

	public void setProject(String projectid) {
		this.projectid = projectid;
	}

	public void setParentTask(String parentTask) {
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
