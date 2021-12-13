package projectmanager.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import projectmanager.db.TaskDAO;
import projectmanager.http.MarkTaskRequest;
import projectmanager.http.MarkTaskResponse;

public class MarkTaskHandler implements RequestHandler<MarkTaskRequest, MarkTaskResponse>{

	LambdaLogger logger;
	
	public boolean markTask(String taskid, boolean completed) {
		boolean result = false;
		
		if (logger != null) {
		      logger.log("in markTask");
		    }
		TaskDAO dao = new TaskDAO();

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
		
		try {
			if (markTask(req.getTaskid(), req.getCompleted())) {
				response = new MarkTaskResponse(200);
			} else {
				response = new MarkTaskResponse(422,
						"Task Mark Failed!");
			}

		} catch (Exception e) {
			response = new MarkTaskResponse(400,
					"Unable to create Task: " + "(" + e.getMessage() + ")");
		}

		return response;
	}
}
