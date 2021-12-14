package projectmanager.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import projectmanager.db.TaskDAO;
import projectmanager.http.GenericResponse;
import projectmanager.http.MarkTaskRequest;
import projectmanager.http.MarkTaskResponse;
import projectmanager.middleware.ProjectArchived;
import projectmanager.model.Task;

public class MarkTaskHandler implements RequestHandler<MarkTaskRequest, MarkTaskResponse> {

	LambdaLogger logger;
	ProjectArchived archivedMiddleware = new ProjectArchived();
	TaskDAO dao = new TaskDAO();

	public boolean markTask(String taskid, boolean completed) {
		boolean result = false;

		if (logger != null) {
			logger.log("in markTask");
		}

		if (logger != null) {
			logger.log("in markTask, retrieved the DAO");
		}

		try {
			if (completed) {
				result = dao.markTask(taskid, 1);
			} else {
				result = dao.markTask(taskid, 0);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (logger != null) {
			logger.log("in renameTask, fetched the result");
		}

		return result;
	}

	@Override
	public MarkTaskResponse handleRequest(MarkTaskRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler of UpdateTaskHandler");
		logger.log(req.toString());

		MarkTaskResponse response;
		MarkTaskResponse typicalErrorResponse = new MarkTaskResponse(422,
				"Task Mark Failed!");
		try {
			Task t = this.dao.getTaskById(req.getTaskid());
			if (t == null)
				return typicalErrorResponse;
			GenericResponse archived = archivedMiddleware.getArchived(t.projectid, context);
			if (archived.statusCode == 200) {
				if (markTask(req.getTaskid(), req.getCompleted()))
					response = new MarkTaskResponse(200);
				else
					response = typicalErrorResponse;
			} else
				response = new MarkTaskResponse(archived.statusCode, archived.error);

		} catch (Exception e) {
			response = new MarkTaskResponse(400,
					"Unable to create Task: " + "(" + e.getMessage() + ")");
		}

		return response;
	}
}
