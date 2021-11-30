package projectmanager.http;

public class DeleteTeammateResponse {
    int statusCode;
    String errormessage;

    public DeleteTeammateResponse() {}

    public DeleteTeammateResponse(int statusCode, String errormessage) {
        this.statusCode = statusCode;
        this.errormessage = errormessage;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setErrorMessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public String getErrorMessage() {
        return this.errormessage;
    }
}
