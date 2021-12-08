package projectmanager.http;

public class DeleteTeammateResponse {
    public boolean deleted;
    public int statusCode;
    public String error;

    public DeleteTeammateResponse(boolean deleted, int statusCode) {
        this.deleted = deleted;
        this.statusCode = statusCode;
        this.error = "";
    }

    public DeleteTeammateResponse(int statusCode, String error) {
        this.statusCode = statusCode;
        this.error = error;
        this.deleted = false;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return this.error;
    }
}
