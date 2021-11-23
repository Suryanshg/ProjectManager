package projectmanager.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import projectmanager.db.ProjectDAO;
import projectmanager.http.ProjectRequest;
import projectmanager.http.ProjectResponse;
import projectmanager.model.Project;

public class GetProjectHandler implements RequestHandler<ProjectRequest, ProjectResponse> {
	
	public LambdaLogger logger = null;
	
	Project project;
	
	
	public boolean getProject(String projectID) throws Exception {
		if (logger != null) { logger.log("in getProject"); }
		ProjectDAO dao = new ProjectDAO();
		
		if (logger != null) { logger.log("in getProject, retrieved the DAO"); }
		project = dao.getProjectByID(projectID);
		
		if (project == null) {
			return false;
		}
		
		return true;
	}

    @Override
    public ProjectResponse handleRequest(ProjectRequest req, Context context) {
    	logger = context.getLogger();
		logger.log("Loading Java Lambda handler to get a project");

		ProjectResponse response = null;
		logger.log("Project Request: " + req.toString());
	
		try {
			if (getProject(req.getProject())) {
				response = new ProjectResponse(project, 200);
			} else {
				response = new ProjectResponse(422, "No project with the specified id " + req.getProject() + " exists");
			}
		} catch (Exception e) {
			response = new ProjectResponse(400, "Unable to fetch the project with id " + req.getProject() + "(" + e.getMessage() + ")");
		}

		return response;

    }

}
