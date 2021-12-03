package projectmanager.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import projectmanager.db.TeammateDAO;
import projectmanager.http.CreateTeammateRequest;
import projectmanager.http.CreateTeammateResponse;
import projectmanager.http.DeleteTeammateRequest;
import projectmanager.http.DeleteTeammateResponse;
import projectmanager.model.Teammate;

public class DeleteTeammateHandler implements RequestHandler<DeleteTeammateRequest, DeleteTeammateResponse> {

    LambdaLogger logger;

    String teammateId;

    public boolean deleteTeammate(String teammateid) throws Exception{
        if (logger != null) { logger.log("in createTeammate"); }
        TeammateDAO dao = new TeammateDAO();

        if (logger != null) { logger.log("in createTeammate, retrieved the DAO"); }

        boolean result = dao.deleteTeammate(teammateid);

        if (logger != null) { logger.log("in createTeammate, fetched the result"); }

        return result;
    }

    @Override
    public DeleteTeammateResponse handleRequest(DeleteTeammateRequest req, Context context) {
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler of DeleteTeammateRequest");
        logger.log(req.toString());

        DeleteTeammateResponse response;
        try {
            if (deleteTeammate(req.getTeammateid())) {
                response = new DeleteTeammateResponse(true, 200);
            } else {
                response = new DeleteTeammateResponse(422, "Teammate deletion failed! There are no teammates with this ID.");
            }

        } catch (Exception e) {
            response = new DeleteTeammateResponse(400, "Unable to delete Teammate: " + req.getTeammateid() + "(" + e.getMessage() + ")");
        }

        return response;
    }

}
