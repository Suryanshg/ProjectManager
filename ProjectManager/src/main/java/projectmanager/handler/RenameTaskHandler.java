package projectmanager.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import projectmanager.db.TaskDAO;
import projectmanager.http.GenericResponse;
import projectmanager.http.RenameTaskRequest;
import projectmanager.http.RenameTaskResponse;
import projectmanager.middleware.ProjectArchived;
import projectmanager.model.Task;

public class RenameTaskHandler implements RequestHandler<RenameTaskRequest, RenameTaskResponse> {

	LambdaLogger logger;
	ProjectArchived archivedMiddleware = new ProjectArchived();
	TaskDAO dao = new TaskDAO();

	public boolean renameTask(String taskid, String name) throws Exception {
		if (logger != null) {
			logger.log("in renameTask");
		}

		if (logger != null) {
			logger.log("in renameTask, retrieved the DAO");
		}

		boolean result = dao.renameTask(taskid, name);

		if (logger != null) {
			logger.log("in renameTask, fetched the result");
		}

		return result;
	}

	@Override
	public RenameTaskResponse handleRequest(RenameTaskRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler of UpdateTaskHandler");
		logger.log(req.toString());

		RenameTaskResponse response;
		RenameTaskResponse typicalErrorResponse = new RenameTaskResponse(422,
				"Rename Failed!");
		try {
			Task t = this.dao.getTaskById(req.getTaskid());
			if (t == null) {
				return typicalErrorResponse;
			}
			GenericResponse archived = archivedMiddleware.getArchived(t.projectid, context);
			if (archived.statusCode == 200) {
				if (renameTask(req.getTaskid(), req.getName()))
					response = new RenameTaskResponse(200);
				else
					response = typicalErrorResponse;
			} else
				response = new RenameTaskResponse(archived.statusCode, archived.error);

		} catch (Exception e) {
			response = new RenameTaskResponse(400,
					"Unable to rename Task: " + "(" + e.getMessage() + ")");
		}

		return response;
	}
}
