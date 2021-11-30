package projectmanager.http;

public class DeleteTeammateRequest {
    String teammateid;

    public DeleteTeammateRequest() {

    }

    public DeleteTeammateRequest(String teammateid) {
        this.teammateid = teammateid;
    }

    public String getTeammateId() {
        return this.teammateid;
    }

    public void setTeammateId(String teammateid) {
        this.teammateid = teammateid;
    }
}
