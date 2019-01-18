package utils;

import static utils.StringUtils.isTrivial;

import java.util.Locale;
import java.util.Map;

import models.Payment;
import models.dao.DaoProvider;
import play.mvc.Controller;
import play.mvc.Result;
import utils.PaymentStatus.Status;

import com.google.inject.Inject;

import controllers.routes;

public class MockCCAvenuePaymentGateway extends CCAvenuePaymentGateway {
	private static final String MOCK_CCAV_PARAM_STATUS = "status";
	@Inject
	public MockCCAvenuePaymentGateway(DaoProvider provider,
			TransactionIdGenerator idgen) {
		super(provider, idgen);
	}

	@Override
	public Result redirectToBank(String secret, String redirectionUrl,
			String remoteAddress, String userAgent, Locale locale)
			throws PaymentGatewayError {
		return Controller.ok(views.html.payments.mockPaymentGateway.render(
				routes.PaymentController.processPayment().url(), 
				identifyPaymentAndObtainParameters(secret, redirectionUrl)));
	}

	@Override
	public PaymentStatus processPayment(Map<String, String> data)
			throws PaymentGatewayError {
        String orderId = data.get(CCAV_PARAM_ORDER_ID);
        String status = data.get(MOCK_CCAV_PARAM_STATUS);

        if (isTrivial(orderId)) {
            throw new PaymentGatewayError("Missing mandatory parameters.");
        }
        
        Payment payment = paymentDao.findByMerchantTxnId(orderId);
        if (payment == null) {
            throw new PaymentGatewayError("Payment with payment id: " + orderId + " does not exist.");
        }
        
        if (value(payment.clientProcessed) || value(payment.gatewayProcessed) || value(payment.invoice.paid)) {
            throw new PaymentGatewayError("Payment with payment id: " + orderId
                    + " found in invalid state. Client Processed: " + payment.clientProcessed
                    + " Gateway Processed: " + payment.gatewayProcessed + " Paid: " + payment.invoice.paid, payment);
        }

        LOG.info("Found mock status: " + status);
        payment.authDesc = CCAV_VAL_AUTHDESC_SUCCESS;
        payment.gatewayProcessed = true;

    
        PaymentStatus.Status paymentStatus;
        
        
        if (!StringUtils.isTrivial(status) && "N".equals(status)) {
        	
        	paymentStatus = Status.FAILURE;
        	payment.invoice.paid = false;
        	payment.success = false;
        	
        }else{
        	
        	paymentStatus = Status.SUCCESS;
        	payment.bankName = "Test Bank";
            payment.nbBid = "00000000-0000-0000-1411-071507993000";
            payment.paymentMode = "Test card";
            payment.nbOrderNo = "CCABK1CNM257";
            payment.invoice.paid = true;
            payment.success = true;
        }
        
        LOG.info("Payment with payment id: " + payment.id + " towards invoice bearing invoice number: "
                + payment.invoice.invoiceNumber + " was: " + paymentStatus);

        paymentDao.save(payment);
        // Invoice needs to be separately saved as we marked invoice as paid.
        invoiceDao.save(payment.invoice);
        
        // TODO: Read the address parameters, make provision in payment to store it, and save them in database.
            
        return new PaymentStatus(paymentStatus, payment);
	}
}
