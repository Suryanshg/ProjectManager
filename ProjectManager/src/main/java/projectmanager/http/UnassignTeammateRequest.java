package projectmanager.http;

public class UnassignTeammateRequest {
  String projectid;
  String taskid;
  String teammateid;

  public UnassignTeammateRequest() {
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
    return "UnassignTeammateRequest(Project:" + projectid + ", Task:" + taskid + ", Teammate:" + teammateid + ")";
  }
}
