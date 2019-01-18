package utils;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

public final class Notifier {
    private static final Logger LOG = LoggerFactory.getLogger(Notifier.class);
    private Notifier() {

    }

   public static void sendHtmlEmail(Mail content) {
       MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
       mail.addFrom(content.getSender());
       mail.addRecipient(content.getRecipient());
       mail.setSubject(content.getSubject());
       mail.sendHtml(content.getBody());
       LOG.info("Sending Mail to " +content.getRecipient());
   }
    
   public static void sendTextEmail(Mail content) {
       MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
       mail.addFrom(content.getSender());
       mail.addRecipient(content.getRecipient());
       mail.setSubject(content.getSubject());
       mail.send(content.getBody());
       LOG.info("Sending Mail to " +content.getRecipient());
   }
}