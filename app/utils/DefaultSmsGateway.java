package utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.Akka;
import ps.akka.actor.SmsSender;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.RoundRobinRouter;

import common.ConfigUtils;

public final class DefaultSmsGateway implements SmsGateway {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultSmsGateway.class);
	
	//SMS router for routing incoming msg among 25 SmsSender
	private final static ActorRef SMSrouter =Akka.system().actorOf(new Props(SmsSender.class).withRouter(new RoundRobinRouter(25)),"SMSrouter");
											     

	private static final String CONFIG_SMS_GATEWAY_PREFIX = "smsgateway";
	private static final String CONFIG_SMS_GATEWAY_URL = CONFIG_SMS_GATEWAY_PREFIX + ".url";
	private static final String CONFIG_SMS_GATEWAY_USER = CONFIG_SMS_GATEWAY_PREFIX + ".user";
	private static final String CONFIG_SMS_GATEWAY_PASSWORD = CONFIG_SMS_GATEWAY_PREFIX + ".password";
	private static final String CONFIG_SMS_GATEWAY_SENDER_ID = CONFIG_SMS_GATEWAY_PREFIX + ".senderid";
	private static final String PARAM_USER_ID = "username";
	private static final String PARAM_PASSWORD = "pass";
	private static final String PARAM_SENDER_ID = "senderid";
	private static final String PARAM_MESSAGE = "message";
	private static final String PARAM_MOBILE_NO = "dest_mobileno";
	private static final String PARAM_MSGTYPE = "msgtype";
    private static final String PARAM_TEMPLATE_ID = "tempid";
	private static final String PARAM_MSGTYPE_TEXT = "TXT";
	private static final String PARAM_RESPONSE = "response";
	private static final String PARAM_RESPONSE_YES = "Y";

	private static final Pattern MOBILE_NO_PATTERN = Pattern.compile("[0-9]+");
	private static final Pattern URL_PATTERN = Pattern.compile("(http|https)://([A-z0-9\\.]+)[\\:]?([0-9]+)?(/.*)?");
	private static final Pattern SMS_GATEWAY_SUCCESS_RESPONSE_PATTERN = Pattern.compile("[0-9]+\\-[0-9]+_[0-9]+_[0-9]+");
	
	private static final String DEFAULT_COUNTRY_CODE = "91";
	
	private final String user;
	private final String password;
	private final String senderId;
	private final String scheme;
	private final String host;
	private final String port;
	private final String path; 

	public static class TemplateSms{
		public final HttpGet request;
		public final String mobile;
		public final SmsTemplate template;
		
		TemplateSms(HttpGet request,String mobile, SmsTemplate template ){
			this.request = request;
			this.mobile = mobile;
			this.template = template;
		}
	}
	
	public static class TextSms{
		public final HttpGet request;
		public final String mobile;
		
		TextSms(HttpGet request,String mobile){
			this.request = request;
			this.mobile = mobile;
		}
	}
	
	public DefaultSmsGateway() {
		LOG.info("Initializing DefaultSmsGateway.");
		
		user = ConfigUtils.getConfigString(CONFIG_SMS_GATEWAY_USER, true);
		password = ConfigUtils.getConfigString(CONFIG_SMS_GATEWAY_PASSWORD, true);
		senderId = ConfigUtils.getConfigString(CONFIG_SMS_GATEWAY_SENDER_ID, true);
		
		String gatewayUrl = ConfigUtils.getConfigString(CONFIG_SMS_GATEWAY_URL, true);
		LOG.info("Gateway URL: " + gatewayUrl);
		LOG.info("Username: " + user);
		LOG.info("Password: " + "******");
		LOG.info("SenderID: " + senderId);
		 
		Matcher matcher = URL_PATTERN.matcher(gatewayUrl);
		
		if (!matcher.matches()) {
			throw new RuntimeException("Invalid gateway URL: " + gatewayUrl);
		}
		
		scheme = matcher.group(1);
		host = matcher.group(2);
		port = matcher.group(3);
		path = matcher.group(4);
		
		LOG.info("Calculated scheme: " + scheme);
		LOG.info("Calculated host: " + host);
		LOG.info("Calculated port: " + port);
		LOG.info("Calculated path: " + path);
	}
	
	@Override
    public void send(String mobile,SmsTemplate template, String... params) throws SmsGatewayException {
	    if (LOG.isDebugEnabled()) {
	        LOG.debug("Trying to send SMS to number: " + mobile + " template: " + template.name() + " params: " + Arrays.asList(params));
	    }
        if (!MOBILE_NO_PATTERN.matcher(mobile.trim()).matches() && mobile.trim().length() != 10) {
            throw new SmsGatewayException("Invalid Mobile Number");
        }
        
        try {
            
            URIBuilder builder = new URIBuilder().setScheme(scheme).setHost(host);
            // Set optional parameter port
            if (port != null) {
                builder.setPort(Integer.valueOf(port));
            }
            
            // Set optional parameter path
            if (path != null) {
                builder.setPath(path);
            }
            
            builder.setScheme(scheme)
                    .setHost(host)
                    .setParameter(PARAM_USER_ID, user)
                    .setParameter(PARAM_PASSWORD, password)
                    .setParameter(PARAM_SENDER_ID, senderId)
                    .setParameter(PARAM_TEMPLATE_ID, template.getTemplateId())
                    .setParameter(PARAM_MOBILE_NO, appendCountryCode(mobile))
                    .setParameter(PARAM_RESPONSE, PARAM_RESPONSE_YES).build();
        
            if (params != null) {
                int size = params.length;
                for (int i = 0; i < size; i++) {
                    builder.setParameter("F" + (i + 1), params[i]);
                }
            }

            URI uri = builder.build(); 
  

            HttpGet request = new HttpGet(uri);
            
            LOG.debug("SMS Gateway request URL: " + request.toString());
            SMSrouter.tell(new TemplateSms(request, mobile, template),SMSrouter);

        }  catch (URISyntaxException urie) {
            throw new SmsGatewayException(urie);
        }
    }
	
	@Override
	public void sendTextSms(String mobile, String message)  throws SmsGatewayException {
		LOG.debug("Trying to send SMS to number: " + mobile + " message: " + message);
		if (!MOBILE_NO_PATTERN.matcher(mobile.trim()).matches() && mobile.trim().length() != 10) {
			throw new SmsGatewayException("Invalid Mobile Number");
		}
		
		try {
			
			URIBuilder builder = new URIBuilder().setScheme(scheme).setHost(host);
			// Set optional parameter port
			if (port != null) {
				builder.setPort(Integer.valueOf(port));
			}
			
			// Set optional parameter path
			if (path != null) {
				builder.setPath(path);
			}
			
			URI uri = builder.setScheme(scheme)
							.setHost(host)
							.setParameter(PARAM_USER_ID, user)
							.setParameter(PARAM_PASSWORD, password)
							.setParameter(PARAM_SENDER_ID, senderId)
							.setParameter(PARAM_MSGTYPE, PARAM_MSGTYPE_TEXT)
							.setParameter(PARAM_MESSAGE, escapeSpecialChars(message))
							.setParameter(PARAM_MOBILE_NO, appendCountryCode(mobile))
							.setParameter(PARAM_RESPONSE, PARAM_RESPONSE_YES).build();
		
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(uri);

			LOG.debug("SMS Gateway request URL: " + request.toString());
			
			SMSrouter.tell(new TextSms(request, mobile),SMSrouter);
			 
		} catch (URISyntaxException urie) {
			throw new SmsGatewayException(urie);
		}
	}
	
	private boolean isErrorResponse(String response) {
			return !SMS_GATEWAY_SUCCESS_RESPONSE_PATTERN.matcher(response).matches();
	}
	
	private String escapeSpecialChars(String message) {
		String cleaned = message;
		cleaned = cleaned.replace("&", "amp;");
		cleaned = cleaned.replace("#", ";hash");
		cleaned = cleaned.replace("+", "plus;");
		cleaned = cleaned.replace(",", "comma;");
		return cleaned;
	}

	public String appendCountryCode(String mobile)  throws SmsGatewayException {
		return DEFAULT_COUNTRY_CODE + mobile;
	}
}