package projectmanager.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import projectmanager.db.TaskDAO;
import projectmanager.http.CreateTaskRequest;
import projectmanager.http.CreateTaskResponse;
import projectmanager.model.Task;

public class CreateTaskHandler implements RequestHandler<CreateTaskRequest, CreateTaskResponse> {

	LambdaLogger logger;

	String taskId;

	public boolean createTask(String taskName, String projectId) throws Exception {
		if (logger != null) {
			logger.log("in createTask");
		}
		TaskDAO dao = new TaskDAO();

		if (logger != null) {
			logger.log("in createTask, retrieved the DAO");
		}
		Task task = new Task(taskName);

		boolean result = dao.addTask(task, projectId);

		if (logger != null) {
			logger.log("in createTask, fetched the result");
		}

		if (result) {
			this.taskId = task.id.toString();
		}

		return result;
	}

	@Override
	public CreateTaskResponse handleRequest(CreateTaskRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler of CreateTaskHandler");
		logger.log(req.toString());

		CreateTaskResponse response;
		try {
			if (createTask(req.getTitle(), req.getProjectId())) {
				response = new CreateTaskResponse(taskId, 200);
			} else {
				response = new CreateTaskResponse(422,
						"Task Creation Failed! Task with the same name/project combination already exists!");
			}

		} catch (Exception e) {
			response = new CreateTaskResponse(400,
					"Unable to create Task: " + req.getTitle() + "(" + e.getMessage() + ")");
		}

		return response;
	}

}
