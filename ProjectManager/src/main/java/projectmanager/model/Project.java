package projectmanager.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Project {
	public final UUID id;
	public final String name;
	public List<Task> tasks;
	public List<Teammate> teammates;
	public boolean isArchived;
	
	public Project(String name) {
		this.id = UUID.randomUUID();
		this.name = name;
		tasks = new ArrayList<Task>();
		teammates = new ArrayList<Teammate>();
		isArchived = false;		
	}
	
	public Project(UUID id, String name) {
		this.id = id;
		this.name = name;
		tasks = new ArrayList<Task>();
		teammates = new ArrayList<Teammate>();
		isArchived = false;		
	}
	
	public Project(UUID id, String name, boolean isArchived) {
		this.id = id;
		this.name = name;
		this.tasks = new ArrayList<Task>();
		this.teammates = new ArrayList<Teammate>();
		this.isArchived = isArchived;
	}
	
	public void addTeammate(Teammate teammate) {
		this.teammates.add(teammate);
	}
	
	public void removeTeammate(Teammate teammate) {
		this.teammates.remove(teammate);
	}
	
	public void addTask(Task task) {
		this.tasks.add(task);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		
		if (o instanceof Project) {
			Project otherProject = (Project) o;
			return id.equals(otherProject.id);
		}
		
		return false;
	}

}
