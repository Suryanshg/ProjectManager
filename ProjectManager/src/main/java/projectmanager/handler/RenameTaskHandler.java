package projectmanager.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import projectmanager.db.TaskDAO;
import projectmanager.http.RenameTaskRequest;
import projectmanager.http.RenameTaskResponse;

public class RenameTaskHandler implements RequestHandler<RenameTaskRequest, RenameTaskResponse> {

	LambdaLogger logger;
	
	public boolean renameTask(String taskid, String name) throws Exception {
		if (logger != null) {
		      logger.log("in renameTask");
		    }
		TaskDAO dao = new TaskDAO();

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
		try {
			if (renameTask(req.getTaskid(), req.getName())) {
				response = new RenameTaskResponse(200);
			} else {
				response = new RenameTaskResponse(422,
						"Rename Failed!");
			}

		} catch (Exception e) {
			response = new RenameTaskResponse(400,
					"Unable to rename Task: " + "(" + e.getMessage() + ")");
		}

		return response;
	}
}
