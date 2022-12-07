package myutil;

@SuppressWarnings("serial")
public class CustomException extends Exception{

	public CustomException() {
			
	}
	
	public CustomException(String message){
		super(message);
	}
	
	public CustomException(String message, Exception e) {
		super(message,e);
	}
	
}
