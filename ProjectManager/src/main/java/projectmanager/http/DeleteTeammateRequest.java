package projectmanager.http;

public class DeleteTeammateRequest {
    String teammateid;

    public DeleteTeammateRequest() {
    }

    public DeleteTeammateRequest(String teammateid) {
        this.teammateid = teammateid;
    }

    public String getTeammateid() {
        return this.teammateid;
    }

    public void setTeammateid(String teammateid) {
        this.teammateid = teammateid;
    }
}
