package projectmanager.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import projectmanager.db.TeammateDAO;
import projectmanager.http.CreateTeammateRequest;
import projectmanager.http.CreateTeammateResponse;
import projectmanager.http.GenericResponse;
import projectmanager.middleware.ProjectArchived;
import projectmanager.model.Teammate;

public class CreateTeammateHandler implements RequestHandler<CreateTeammateRequest, CreateTeammateResponse> {

    LambdaLogger logger;
    ProjectArchived archivedMiddleware = new ProjectArchived();

    String teammateId;

    public boolean createTeammate(String name, String projectid) throws Exception {
        if (logger != null) {
            logger.log("in createTeammate");
        }
        TeammateDAO dao = new TeammateDAO();

        if (logger != null) {
            logger.log("in createTeammate, retrieved the DAO");
        }
        Teammate teammate = new Teammate(name);

        boolean result = dao.addTeammate(teammate, projectid);

        if (logger != null) {
            logger.log("in createTeammate, fetched the result");
        }

        if (result) {
            teammateId = teammate.id.toString();
        }

        return result;
    }

    @Override
    public CreateTeammateResponse handleRequest(CreateTeammateRequest req, Context context) {
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler of CreateProjectHandler");
        logger.log(req.toString());

        CreateTeammateResponse response;
        try {
            GenericResponse archived = archivedMiddleware.getArchived(req.projectid, context);
            if (archived.statusCode == 200) {
                if (createTeammate(req.getName(), req.getprojectid()))
                    response = new CreateTeammateResponse("Teammate created successfully.", 200, teammateId);
                else
                    response = new CreateTeammateResponse(
                            "Teammate Creation Failed! Teammate with the same name already exists in this project!",
                            422);
            } else
                response = new CreateTeammateResponse(archived.error, archived.statusCode);

        } catch (Exception e) {
            response = new CreateTeammateResponse(
                    "Unable to create Teammate: " + req.getName() + "(" + e.getMessage() + ")", 400);
        }

        return response;
    }

}