package projectmanager.model;

import java.util.ArrayList;
import java.util.UUID;

public class Project {
	public final UUID id;
	public final String name;
	public ArrayList<Task> tasks;
	public ArrayList<Teammate> teammates;
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
