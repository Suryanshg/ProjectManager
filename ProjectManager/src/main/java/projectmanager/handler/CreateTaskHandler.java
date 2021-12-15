package projectmanager.handler;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import projectmanager.db.TaskDAO;
import projectmanager.db.TeammateTaskDAO;
import projectmanager.http.CreateTaskRequest;
import projectmanager.http.CreateTaskResponse;
import projectmanager.http.GenericResponse;
import projectmanager.middleware.ProjectArchived;
import projectmanager.model.Task;
import projectmanager.model.TeammateTask;

public class CreateTaskHandler implements RequestHandler<CreateTaskRequest, CreateTaskResponse> {

	LambdaLogger logger;
	ProjectArchived archivedMiddleware = new ProjectArchived();

	Task task;

	public Boolean createTask(String taskName, String parentTask, String projectid) throws Exception {
		if (logger != null) {
			logger.log("in createTask");
		}
		TaskDAO dao = new TaskDAO();

		if (logger != null) {
			logger.log("in createTask, retrieved the DAO");
		}
		Task task = new Task(taskName);
		Boolean result = dao.addTask(task, parentTask, projectid);

		if (logger != null) {
			logger.log("in createTask, fetched the result");
		}

		if (result) {
			this.task = task;
		}

		return result;
	}

	@Override
	public CreateTaskResponse handleRequest(CreateTaskRequest req, Context context) {

		try {
			GenericResponse archived = archivedMiddleware.getArchived(req.getProjectid(), context);
			if (archived.statusCode != 200)
				return new CreateTaskResponse(archived.statusCode, archived.error);
		} catch (Exception e) {
			return new CreateTaskResponse(400,
					"Unable to create Task: " + req.getTitle() + "(" + e.getMessage() + ")");

		}
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler of CreateTaskHandler");
		logger.log(req.toString());

		CreateTaskResponse response = null;

		// TODO: Process request to get the list of titles to create
		List<String> titles = new ArrayList<String>();
		// Really not sure if the tests can actually carry a true \n in them.
		String[] array = req.getTitle().split("\n|U+000A");

		for (String title : array) {
			titles.add(title);
		}

		// TODO: if there is parentTask, then get the teammateids for the parent
		List<TeammateTask> teammateTasks = new ArrayList<TeammateTask>();
		TeammateTaskDAO ttDao = new TeammateTaskDAO();

		if (req.getParentTask() != null) {

			// Extract the parentTask's assignees

			try {
				teammateTasks = ttDao.getAllTeammateTaskForTaskId(req.getParentTask());

				// Unassign all the teammates from the parentTask
				for (TeammateTask tt : teammateTasks) {
					ttDao.unassignTeammate(tt.projectid, tt.taskid, tt.teammateid);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// TODO: Create tasks and store the task ids
		List<String> createdTaskIds = new ArrayList<String>();

		for (String title : titles) {
			try {
				if (createTask(title, req.getParentTask(), req.getProjectid())) {

					createdTaskIds.add(task.id.toString());
				} else {
					response = new CreateTaskResponse(422,
							"Task(s) Creation Failed! Task(s) with the same name/project combination already exists!");
					return response;
				}

			} catch (Exception e) {
				response = new CreateTaskResponse(400,
						"Unable to create Task: " + req.getTitle() + "(" + e.getMessage() + ")");

				return response;
			}
		}

		// If the tasks created were subtasks
		if (req.getParentTask() != null) {
			// Double looped array with MANUAL incrementation.

			// Here's how this works.
			// For a list of teammates that exceeds the size of the list of tasks, this will
			// just continue to add
			// teammates to tasks, doubling them up.

			// Whenever a task is assigned to a teammate, teammate is MANUALLY incremented
			// upward.
			// This allows for repeating when teammates > tasks.

			// Checking added to make sure that this won't fail if tasks > teammates

			//

			for (int teammate = 0; teammate < teammateTasks.size();) {
				for (int task = 0; task < createdTaskIds.size(); task++) {
					if (teammate >= teammateTasks.size()) {
						break;
					}
					try {
						ttDao.assignTeammate(req.getProjectid(), createdTaskIds.get(task), teammateTasks.get(teammate).teammateid);
					} catch (Exception e) {
						response = new CreateTaskResponse(401, "Failed assignment of teammates.");
						return response;
					}
					teammate++;
				}
			}
		}

		List<Task> tasks = new ArrayList<Task>();
		TaskDAO tdao = new TaskDAO();
		for (String taskid : createdTaskIds) {
			Task t = null;
			try {
				t = tdao.getTaskById(taskid);
			} catch (Exception e) {
				e.printStackTrace();
			}
			tasks.add(t);
		}
		// If we get to this point, we can set the response to be successful
		response = new CreateTaskResponse(tasks, 200);

		return response;
	}

}
