package projectmanager.handler;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import projectmanager.db.TeammateTaskDAO;
import projectmanager.http.AssignTeammateRequest;
import projectmanager.http.AssignTeammateResponse;
import projectmanager.http.GenericResponse;
import projectmanager.middleware.ProjectArchived;

public class AssignTeammateHandler implements RequestHandler<AssignTeammateRequest, AssignTeammateResponse> {
  LambdaLogger logger;
  ProjectArchived archivedMiddleware = new ProjectArchived();

  public boolean assignTeammate(String projectid, String taskid, String teammateid) throws Exception {
    if (logger != null) {
      logger.log("assignTeammate");
    }
    TeammateTaskDAO dao = new TeammateTaskDAO();

    if (logger != null) {
      logger.log("in assignTeammate, retrieved the DAO");
    }

    boolean result = dao.assignTeammate(projectid, taskid, teammateid);

    if (logger != null) {
      logger.log("in assignTeammate, fetched the result");
    }
    return result;
  }

  @Override
  public AssignTeammateResponse handleRequest(AssignTeammateRequest req, Context context) {
    logger = context.getLogger();
    logger.log("Loading Java Lambda handler of AssignTeammateHandler");
    logger.log(req.toString());

    AssignTeammateResponse response;
    try {
      GenericResponse archived = archivedMiddleware.getArchived(req.getProjectid(), context);
      if (archived.statusCode == 200) {
        if (assignTeammate(req.getProjectid(), req.getTaskid(), req.getTeammateid()))
          response = new AssignTeammateResponse(200);
        else
          response = new AssignTeammateResponse(422,
              "Assigning a teammate failed! Teammate already assigned to task or incorrect parameters!");
      } else
        response = new AssignTeammateResponse(archived.statusCode, archived.error);

    } catch (Exception e) {
      response = new AssignTeammateResponse(400,
          "Unable to assign Teammate to Task: (" + e.getMessage() + ")");
    }

    return response;
  }

}
