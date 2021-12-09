package projectmanager.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Teammate {
	public final UUID id;
	public final String name;
	public List<String> tasks;
	
	public Teammate(String name) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.tasks = new ArrayList<String>();
	}
	
	public Teammate(UUID id, String name) {
		this.id = id;
		this.name = name;
		this.tasks = new ArrayList<String>();
	}
	
	public void addTask(String taskId) {
		this.tasks.add(taskId);
	}
	
	public void removeTask(String taskId) {
		this.tasks.remove(taskId);
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
