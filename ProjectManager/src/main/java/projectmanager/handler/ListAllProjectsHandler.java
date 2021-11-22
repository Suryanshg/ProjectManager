package projectmanager.handler;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import projectmanager.db.ProjectDAO;
import projectmanager.http.ListAllProjectsResponse;
import projectmanager.model.Project;

public class ListAllProjectsHandler implements RequestHandler<Object, ListAllProjectsResponse> {
	
	public LambdaLogger logger;
	
	
	public List<Project> getProjects() throws Exception{
		if (logger != null) { logger.log("in getProjects"); }
		ProjectDAO dao = new ProjectDAO();
		
		if (logger != null) { logger.log("in getProjects, retrieved the DAO"); }
		
		List<Project> projects = dao.getAllProjects();
		if (logger != null) { logger.log("in getProjects, fetched the result"); }
		
		return projects;
	}

    @Override
    public ListAllProjectsResponse handleRequest(Object input, Context context) {
    	logger = context.getLogger();
		logger.log("Loading Java Lambda handler to list all constants");

		ListAllProjectsResponse response;
		try {
			List<Project> list = getProjects();
			
			response = new ListAllProjectsResponse(list, 200);
		} catch (Exception e) {
			response = new ListAllProjectsResponse(403, e.getMessage());
		}
		
		return response;

    }

}
