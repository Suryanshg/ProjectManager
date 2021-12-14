package projectmanager.http;

public class GenericResponse {
  public int statusCode;
  public String error;

  public GenericResponse(int statusCode, String error) {
    this.statusCode = statusCode;
    this.error = error;
  }

  public GenericResponse(int statusCode) {
    this.statusCode = statusCode;
    this.error = "";
  }
}
