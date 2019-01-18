package ps.akka.actor;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.DefaultSmsGateway.TemplateSms;
import utils.DefaultSmsGateway.TextSms;
import utils.SmsTemplate;
import akka.actor.UntypedActor;

public class SmsSender extends UntypedActor {
	private static final Logger LOG = LoggerFactory.getLogger(SmsSender.class);
	private static final Pattern SMS_GATEWAY_SUCCESS_RESPONSE_PATTERN = Pattern.compile("[0-9]+\\-[0-9]+_[0-9]+_[0-9]+");
	//Defining request configuration 
	private static final  RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
	
	public void onReceive(Object msg){

        if(msg instanceof TemplateSms){
        	 TemplateSms obj = (TemplateSms)msg;
        	 HttpGet request = obj.request;
        	 final String mobile = obj.mobile;
        	 final SmsTemplate template = obj.template;
        	 
             //Building Async Http client with above configuration
             CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig).build();
             
             LOG.debug("SMS Gateway request URL: " + request.toString());
             httpClient.start();
             httpClient.execute(request, new FutureCallback<HttpResponse>(){
            	 
            	 //FIXME : how do i write method in inner class that throws SmsGatewayException 
                 public void completed(final HttpResponse response){
                	   final long t2 = System.currentTimeMillis();
                 	try {
     						String returnCode = IOUtils.toString(response.getEntity().getContent());
     						LOG.debug("SMS Gateway return code: " + returnCode);
     	                    
     	                    if (isErrorResponse(returnCode)) {
     	                        LOG.warn("Failed sending SMS to number: " + mobile + " template: " + template.getTemplateId() + " due to: " + returnCode);
     	                    } else {
     	                        LOG.debug("SMS sent successfully to number: " + mobile + " template: " + template.getTemplateId());
     	                    }
     				}  catch (IOException ioe) {
     						LOG.warn("IOException while proccessing sms send rquest :" ,ioe);
     				}
                 }
                 
     			public void failed(final Exception ex) {
     				LOG.warn("Failed while proceesing send sms request  :" ,ex);
                 }

                 public void cancelled() {
                 	 LOG.warn("Sending sms request to number " + mobile + "cancelled");
                 }
             });
        }
        
        if(msg instanceof TextSms){
        	TextSms obj = (TextSms)msg;
       	 	HttpGet request = obj.request;
       	 	final String mobile = obj.mobile;
            //Building Async Http client with above configuration
            CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig).build();
            
            LOG.debug("SMS Gateway request URL: " + request.toString());
            httpClient.start();
            httpClient.execute(request, new FutureCallback<HttpResponse>(){
           	 
           	 //FIXME : how do i write method in inner class that throws SmsGatewayException 
             public void completed(final HttpResponse response){
               	   final long t2 = System.currentTimeMillis();
                	try {
    						String returnCode = IOUtils.toString(response.getEntity().getContent());
    						LOG.debug("SMS Gateway return code: " + returnCode);
    	                    
    	                    if (isErrorResponse(returnCode)) {
    	                        LOG.warn("Failed sending SMS to number: " + mobile + " template: " + " due to: " + returnCode);
    	                    } else {
    	                        LOG.debug("SMS sent successfully to number: " + mobile + " template: ");
    	                    }
    				}  catch (IOException ioe) {
    						LOG.warn("IOException while proccessing sms send rquest :" ,ioe);
    				}
                }
                
    			public void failed(final Exception ex) {
    				LOG.warn("Failed while proceesing send sms request  :" ,ex);
                }

                public void cancelled() {
                	 LOG.warn("Sending sms request to number " + mobile + "cancelled");
                }
            });
        }
	}
	
	private boolean isErrorResponse(String response) {
		return !SMS_GATEWAY_SUCCESS_RESPONSE_PATTERN.matcher(response).matches();
	}

}
