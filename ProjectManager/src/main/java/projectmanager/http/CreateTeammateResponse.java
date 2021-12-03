package projectmanager.http;

public class CreateTeammateResponse {
    public int statusCode;
    String teammateid;
    String error;

    public CreateTeammateResponse(String error, int statusCode) {
        this.error = error;
        this.statusCode = statusCode;
        this.teammateid = null;
    }

    public CreateTeammateResponse(String error, int statusCode, String teammateid) {
        this.error = error;
        this.statusCode = statusCode;
        this.teammateid = teammateid;
    }

    public String getTeammateId() {
        return this.teammateid;
    }

    public void setTeammateId(String teammateid) {
        this.teammateid = teammateid;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
