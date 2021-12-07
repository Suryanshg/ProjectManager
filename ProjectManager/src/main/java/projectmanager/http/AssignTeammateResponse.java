package projectmanager.http;

public class AssignTeammateResponse {
  public int statusCode;
  public String error;

  public AssignTeammateResponse(int statusCode) {
    this.statusCode = statusCode;
    this.error = "";
  }

  public AssignTeammateResponse(int statusCode, String error) {
    this.statusCode = statusCode;
    this.error = error;
  }
}
