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
import projectmanager.model.Task;
import projectmanager.model.TeammateTask;

public class CreateTaskHandler implements RequestHandler<CreateTaskRequest, CreateTaskResponse> {

	LambdaLogger logger;

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
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler of CreateTaskHandler");
		logger.log(req.toString());
		
		CreateTaskResponse response = null;
		
		// TODO: Process request to get the list of titles to create
		List<String> titles = new ArrayList<String>();
		String[] array = req.getTitle().split("\n");
		
		for(String title: array) {
			titles.add(title);
		}
		
		
		// TODO: if there is parentTask, then get the teammateids for the parent
		List<TeammateTask> teammateTasks = new ArrayList<TeammateTask>();
		TeammateTaskDAO ttDao = new TeammateTaskDAO();
		
		if(req.getParentTask() != null) {
			
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
		
		for(String title: titles) {
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
		
		// TODO: For each subtask id created, assign the teammates in the round-robin fashion
		
		// If the tasks created were subtasks
		if(req.getParentTask() != null) {
			
			int currentTaskIdIndex = 0;
			
			// For all the teammate tasks, assign the teammates in round-robin fashion
			for(TeammateTask tt: teammateTasks) {
				
				// Assign teammate on the basis of current Task
				try {
					ttDao.assignTeammate(req.getProjectid(), createdTaskIds.get(currentTaskIdIndex), tt.teammateid);
				} catch (Exception e) {
					response = new CreateTaskResponse(401, "Unable to do assign Teammates in round-robin fashion");
					return response;
				}
				
				currentTaskIdIndex++;
				// if the currentTask exceeds the last created Task, we reset the counter
				if (currentTaskIdIndex == createdTaskIds.size()) {
					currentTaskIdIndex = 0;
				}
			}
		}
		
		// If we get to this point, we can set the response to be successful
		response = new CreateTaskResponse(null, 200);
		
		return response;
	}

}
