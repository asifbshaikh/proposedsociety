package mail;

import play.libs.Akka;
import ps.akka.actor.MailActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.RoundRobinRouter;

public class PsMailer implements Mailer{

	//Mail router for routing incoming MailActor among 25 SmsSender
	private final static ActorRef MAIL_ROUTER =Akka.system().actorOf(new Props(MailActor.class).withRouter(new RoundRobinRouter(25)),"MAIL_ROUTER");
	
	@Override
	public void sendText(TextMail mail) {
		MAIL_ROUTER.tell(mail, MAIL_ROUTER);
	}

	@Override
	public void sentHtml(HtmlMail mail) {
		MAIL_ROUTER.tell(mail, MAIL_ROUTER);
	}

}
