package projectmanager.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import projectmanager.db.ProjectDAO;
import projectmanager.http.ArchiveProjectRequest;
import projectmanager.http.ArchiveProjectResponse;
import projectmanager.http.GenericResponse;
import projectmanager.middleware.ProjectArchived;

public class ArchiveProjectHandler implements RequestHandler<ArchiveProjectRequest, ArchiveProjectResponse> {

	public LambdaLogger logger = null;
	ProjectArchived archivedMiddleware = new ProjectArchived();

	@Override
	public ArchiveProjectResponse handleRequest(ArchiveProjectRequest req, Context context) {

		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to archive a project");

		ArchiveProjectResponse response = null;
		logger.log(req.toString());

		ProjectDAO dao = new ProjectDAO();

		try {
			GenericResponse archived = archivedMiddleware.getArchived(req.projectid, context);
			if (archived.statusCode == 200) {
				if (dao.archiveProject(req.projectid))
					response = new ArchiveProjectResponse(true, 200);
				else
					response = new ArchiveProjectResponse(422,
							"Unable to archive project, no project with the specified id exists");
			} else
				response = new ArchiveProjectResponse(archived.statusCode, archived.error);
		} catch (Exception e) {
			response = new ArchiveProjectResponse(400,
					"Unable to archive project:" + req.projectid + "(" + e.getMessage() + ")");
		}

		return response;
	}
}
