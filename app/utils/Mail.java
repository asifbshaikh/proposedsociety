package utils;

public class Mail{
	
    public static final String PS_FROM_ADDR = "Proposed Society <noreply@proposedsociety.com>";
    public static final String BANK_DETAILS_TO_PS = "ProposedSociety@proposedsociety.com";
    public static final String VISITOR_TO_PS = "contact@proposedsociety.com";
    
	private String sender;
	private String recipient;
	private String subject;
	private String body;
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	
}
