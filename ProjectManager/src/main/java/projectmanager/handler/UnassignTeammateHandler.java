package projectmanager.handler;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import projectmanager.db.TeammateTaskDAO;
import projectmanager.http.UnassignTeammateRequest;
import projectmanager.http.UnassignTeammateResponse;

public class UnassignTeammateHandler implements RequestHandler<UnassignTeammateRequest, UnassignTeammateResponse> {
  LambdaLogger logger;

  public boolean UnassignTeammate(String projectid, String taskid, String teammateid) throws Exception {
    if (logger != null) {
      logger.log("UnassignTeammate");
    }
    TeammateTaskDAO dao = new TeammateTaskDAO();

    if (logger != null) {
      logger.log("in UnassignTeammate, retrieved the DAO");
    }

    boolean result = dao.unassignTeammate(projectid, taskid, teammateid);

    if (logger != null) {
      logger.log("in UnassignTeammate, fetched the result");
    }
    return result;
  }

  @Override
  public UnassignTeammateResponse handleRequest(UnassignTeammateRequest req, Context context) {
    logger = context.getLogger();
    logger.log("Loading Java Lambda handler of UnassignTeammateHandler");
    logger.log(req.toString());

    UnassignTeammateResponse response;
    try {
      if (UnassignTeammate(req.getProjectid(), req.getTaskid(), req.getTeammateid())) {
        response = new UnassignTeammateResponse(200);
      } else {
        response = new UnassignTeammateResponse(422,
            "Unassigning a teammate failed! Teammate already Unassigned to task or incorrect parameters!");
      }

    } catch (Exception e) {
      response = new UnassignTeammateResponse(400,
          "Unable to Unassign Teammate to Task: (" + e.getMessage() + ")");
    }

    return response;
  }

}
