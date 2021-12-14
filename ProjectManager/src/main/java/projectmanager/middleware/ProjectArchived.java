package projectmanager.middleware;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import projectmanager.db.ProjectDAO;
import projectmanager.http.GenericResponse;
import projectmanager.model.Project;

public class ProjectArchived {
  public LambdaLogger logger = null;
  ProjectDAO dao = new ProjectDAO();

  public GenericResponse getArchived(String projectid, Context context) {

    logger = context.getLogger();
    logger.log("Checking if project " + projectid + " is archived");

    try {
      Project project = this.dao.getProjectByID(projectid);
      if (project == null)
        return new GenericResponse(422, "Project " + projectid + " doesn't exist!");
      else if (!project.isArchived)
        return new GenericResponse(200);
      else
        return new GenericResponse(409, "Project " + projectid + " is archived!");

    } catch (Exception e) {
      return new GenericResponse(400,
          "Unable to get project " + projectid + " (" + e.getMessage() + ")");
    }
  }
}
