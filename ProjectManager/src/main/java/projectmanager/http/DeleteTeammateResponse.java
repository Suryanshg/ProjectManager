package projectmanager.http;

public class DeleteTeammateResponse {
    public boolean deleted;
    public int statusCode;
    public String errorMessage;

    public DeleteTeammateResponse(boolean deleted, int statusCode) {
        this.deleted = deleted;
        this.statusCode = statusCode;
        this.errorMessage = "";
    }

    public DeleteTeammateResponse(int statusCode, String errorMessage) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.deleted = false;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
