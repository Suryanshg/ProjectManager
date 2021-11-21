package projectmanager.http;

public class GenericErrorResponse {
	public String errorMessage;
	public int statusCode;
	
	public GenericErrorResponse(String errorMessage, int statusCode) {
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
	}

}
