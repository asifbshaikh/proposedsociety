package ps.akka.actor;

import mail.Attachment;
import mail.HtmlMail;
import mail.TextMail;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

import akka.actor.UntypedActor;

public class MailActor extends UntypedActor {
	
	//All following configuration can be read from config file
	private final static String HOST_NAME = "cp-26.webhostbox.net";
	private final static int SMS_PORT = 587;
	private final static DefaultAuthenticator DEFAULT_AUTHENTICATOR = new DefaultAuthenticator("noreply@proposedsociety.com", "B@viskarseva");
	private final static boolean IS_SSl_ON_CONNECT = false;
	
	
	@Override
	public void onReceive(Object obj) throws Exception {
		
		if(obj instanceof TextMail) {
			TextMail mail = (TextMail) obj;
			
			if(mail.getAttachment() == null) {
				
				Email email = new SimpleEmail();
				email.setHostName(HOST_NAME);
				email.setSmtpPort(SMS_PORT);
				email.setAuthenticator(DEFAULT_AUTHENTICATOR);
				email.setSSL(IS_SSl_ON_CONNECT);
				email.setFrom(mail.getFrom());
				email.setSubject(mail.getSubject());
				email.setMsg(mail.getBody());
				email.addTo(mail.getTo());
				email.send();
				
			}else{
				
				EmailAttachment attachment = new EmailAttachment();
				Attachment at = mail.getAttachment();
				
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				attachment.setDescription(at.getDescription());
				attachment.setName(at.getName());
				
				attachment.setPath(at.getAttachment().getAbsolutePath());
				
				MultiPartEmail email = new MultiPartEmail();
				email.setHostName(HOST_NAME);
				email.setSmtpPort(SMS_PORT);
				email.setAuthenticator(DEFAULT_AUTHENTICATOR);
				email.setSSL(IS_SSl_ON_CONNECT);
				email.setFrom(mail.getFrom());
				email.setSubject(mail.getSubject());
				email.setMsg(mail.getBody());
				email.addTo(mail.getTo());

				// add the attachment
				email.attach(attachment);
				// send the email
				email.send();
			}
		}
		
		if(obj instanceof HtmlMail) {
			HtmlMail mail =  (HtmlMail) obj;
			
			if(mail.getAttachment() == null) {
				
				HtmlEmail email = new HtmlEmail();
				email.setHostName(HOST_NAME);
				email.setSmtpPort(SMS_PORT);
				email.setAuthenticator(DEFAULT_AUTHENTICATOR);
				email.setSSL(IS_SSl_ON_CONNECT);
				email.setFrom(mail.getFrom());
				email.setSubject(mail.getSubject());
				email.setHtmlMsg(mail.getHtmlBody());
				email.addTo(mail.getTo());
				email.send();
				
			}else{
				
				EmailAttachment attachment = new EmailAttachment();
				Attachment at = mail.getAttachment();
				
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				attachment.setDescription(at.getDescription());
				attachment.setName(at.getName());
				attachment.setPath(at.getAttachment().getAbsolutePath());
				MultiPartEmail email = new HtmlEmail();
				email.setHostName(HOST_NAME);
				email.setSmtpPort(SMS_PORT);
				email.setAuthenticator(DEFAULT_AUTHENTICATOR);
				email.setSSL(IS_SSl_ON_CONNECT);
				email.setFrom(mail.getFrom());
				email.setSubject(mail.getSubject());
				email.setMsg(mail.getHtmlBody());
				email.addTo(mail.getTo());

				// add the attachment
				email.attach(attachment);
				// send the email
				email.send();
			}
		}
	}

}
