package projectmanager.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import projectmanager.http.TasksRequest;
import projectmanager.http.TasksResponse;

public class GetTasksHandler implements RequestHandler<TasksRequest, TasksResponse> {

	@Override
	public TasksResponse handleRequest(TasksRequest input, Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
