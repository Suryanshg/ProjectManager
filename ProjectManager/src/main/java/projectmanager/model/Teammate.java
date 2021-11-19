package projectmanager.model;

import java.util.ArrayList;
import java.util.UUID;

public class Teammate {
	public final UUID id;
	public final String name;
	public ArrayList<Task> tasks;
	
	public Teammate(String name) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.tasks = new ArrayList<Task>();
	}
	
	public Teammate(UUID id, String name) {
		this.id = id;
		this.name = name;
		this.tasks = new ArrayList<Task>();
	}
	
	public void addTask(Task task) {
		this.tasks.add(task);
	}
	
	public void removeTask(Task task) {
		this.tasks.remove(task);
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
