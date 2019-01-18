package mail;

public interface Mailer {
	void sendText(TextMail mail);
	void sentHtml(HtmlMail mail);
}
