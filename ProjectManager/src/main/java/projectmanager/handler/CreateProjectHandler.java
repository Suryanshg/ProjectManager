package projectmanager.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import projectmanager.db.ProjectDAO;
import projectmanager.http.CreateProjectRequest;
import projectmanager.http.CreateProjectResponse;
import projectmanager.model.Project;

public class CreateProjectHandler implements RequestHandler<CreateProjectRequest, CreateProjectResponse> {

	LambdaLogger logger;
	
	String projectId;
	
	public boolean createProject(String projectName) throws Exception{
		if (logger != null) { logger.log("in createProject"); }
		ProjectDAO dao = new ProjectDAO();
		
		if (logger != null) { logger.log("in createProject, retrieved the DAO"); }
		Project project = new Project (projectName);
		
		boolean result = dao.addProject(project);
		
		if (logger != null) { logger.log("in createProject, fetched the result"); }
		
		if(result) {
			projectId = project.id.toString();
		}
		
		return result;
	}



	@Override
	public CreateProjectResponse handleRequest(CreateProjectRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler of CreateProjectHandler");
		logger.log(req.toString());

		CreateProjectResponse response;
		try {
			if (createProject(req.getProjectName())) {
				String url = " https://softengproject3733.s3.us-east-2.amazonaws.com/project.html/?project=" + projectId; 
				response = new CreateProjectResponse(projectId, url, 200);
			} else {
				response = new CreateProjectResponse(422, "Project Creation Failed! Project with the same name already exists!");
			}

		} catch (Exception e) {
			response = new CreateProjectResponse(400,"Unable to create Project: " + req.getProjectName() + "(" + e.getMessage() + ")");
		}

		return response;
	}

}
