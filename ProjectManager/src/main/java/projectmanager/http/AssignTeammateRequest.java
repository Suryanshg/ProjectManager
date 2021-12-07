package projectmanager.http;

public class AssignTeammateRequest {
  String projectid;
  String taskid;
  String teammateid;

  public AssignTeammateRequest() {
  }

  public void setProjectid(String projectid) {
    this.projectid = projectid;
  }

  public String getProjectid() {
    return this.projectid;
  }

  public void setTaskid(String taskid) {
    this.taskid = taskid;
  }

  public String getTaskid() {
    return this.taskid;
  }

  public void setTeammateid(String teammateid) {
    this.teammateid = teammateid;
  }

  public String getTeammateid() {
    return this.teammateid;
  }

  public String toString() {
    return "AssignTeammateRequest(Project:" + projectid + ", Task:" + taskid + ", Teammate:" + teammateid + ")";
  }
}
