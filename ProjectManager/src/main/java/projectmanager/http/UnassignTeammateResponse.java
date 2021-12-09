package projectmanager.http;

public class UnassignTeammateResponse {
  public int statusCode;
  public String error;

  public UnassignTeammateResponse(int statusCode) {
    this.statusCode = statusCode;
    this.error = "";
  }

  public UnassignTeammateResponse(int statusCode, String error) {
    this.statusCode = statusCode;
    this.error = error;
  }
}
