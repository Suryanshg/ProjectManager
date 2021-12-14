package projectmanager.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Teammate {
	public final UUID id;
	public final String name;
	public List<String> tasks;
	public String projectid;

	public Teammate(String name) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.tasks = new ArrayList<String>();
		this.projectid = null;
	}

	public Teammate(UUID id, String name, String projectid) {
		this.id = id;
		this.name = name;
		this.tasks = new ArrayList<String>();
		this.projectid = projectid;
	}

	public void addTask(String taskId) {
		this.tasks.add(taskId);
	}

	public void removeTask(String taskId) {
		this.tasks.remove(taskId);
	}

	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}

		if (o instanceof Teammate) {
			Teammate other = (Teammate) o;
			return id.equals(other.id);
		}

		return false;
	}

}
