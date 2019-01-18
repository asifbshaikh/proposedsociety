package utils;

import java.util.Locale;
import java.util.Map;

import models.Invoice;
import models.Payment;
import play.mvc.Result;

public interface PaymentGateway {
	public enum PaymentMode {
		CREDIT_OR_DEBIT_CARD, NET_BANKING;
	}

    PaymentStatus processPayment(Map<String, String> data) throws PaymentGatewayError;
    Result initiateFromClient(Invoice invoice, String redirectUrl) throws PaymentGatewayError;
    Result redirectToBank(String secret, String redirectionUrl, String remoteAdress, String userAgent, Locale locale) throws PaymentGatewayError;
    Result redirectToClient(PaymentStatus status) throws PaymentGatewayError;
    Payment clientProcessPayment(String encryptedData) throws PaymentGatewayError;
}
