package projectmanager.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import projectmanager.db.ProjectDAO;
import projectmanager.http.ArchiveProjectRequest;
import projectmanager.http.ArchiveProjectResponse;

public class ArchiveProjectHandler implements RequestHandler<ArchiveProjectRequest, ArchiveProjectResponse> {

	public LambdaLogger logger = null;
	
	@Override
	public ArchiveProjectResponse handleRequest(ArchiveProjectRequest req, Context context) {
		
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to archive a project");
		
		ArchiveProjectResponse response = null;
		logger.log(req.toString());
		
		ProjectDAO dao = new ProjectDAO();
		
		try {
			if (dao.archiveProject(req.projectid)) {
				response = new ArchiveProjectResponse(true, 200);
			} else {
				response = new ArchiveProjectResponse(422, "Unable to archive project, no project with the specified id exists");
			}
		} catch (Exception e) {
			response = new ArchiveProjectResponse(400, "Unable to archive project:" + req.projectid + "(" + e.getMessage() + ")");
		}
		
		return response;
	}
}
