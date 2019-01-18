package utils;

public interface SmsGateway {
	void send(String mobile, SmsTemplate template, String ... params) throws SmsGatewayException;
	void sendTextSms(String mobile, String message) throws SmsGatewayException;
}
