package projectmanager.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import projectmanager.db.ProjectDAO;
import projectmanager.http.DeleteProjectRequest;
import projectmanager.http.DeleteProjectResponse;

public class DeleteProjectHandler implements RequestHandler<DeleteProjectRequest, DeleteProjectResponse> {
	
	public LambdaLogger logger = null;

	@Override
	public DeleteProjectResponse handleRequest(DeleteProjectRequest req, Context context) {
		
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to delete project");
		
		DeleteProjectResponse response = null;
		logger.log(req.toString());
		
		ProjectDAO dao = new ProjectDAO();
		
		try {
			if (dao.deleteProject(req.projectid)) {
				response = new DeleteProjectResponse(true, 200);
			} else {
				response = new DeleteProjectResponse(422, "Unable to delete project, no project with the specified id exists");
			}
		} catch (Exception e) {
			response = new DeleteProjectResponse(400, "Unable to delete project:" + req.projectid + "(" + e.getMessage() + ")");
		}
		
		
		return response;
	}

}
