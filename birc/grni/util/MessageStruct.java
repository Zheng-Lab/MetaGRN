package birc.grni.util;

//TODO: exception, error and other messages report mechanism
/**
 * message structure
 * @author liu xingliang
 */
public class MessageStruct {
	
	private String identifier;
	private String message;
	
	public MessageStruct() {
		this.identifier = "";
		this.message = "";
	}
	
	public MessageStruct(String identifier, String message) {
		this.identifier = identifier;
		this.message = message;
	}
	
	public String getIdentifier() {
		return this.identifier;
	}
	
	public String getMessage() {
		return this.message;
	}
}
