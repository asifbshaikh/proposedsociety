package controllers;

import java.util.Locale;

import models.dao.DaoProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import utils.PaymentGateway;
import utils.PaymentGatewayError;
import utils.PaymentStatus;

import com.google.inject.Inject;

public class PaymentController extends AuthenticatedUserController {
    private final Logger LOG = LoggerFactory.getLogger(PaymentController.class);

    // Keeping it public for others to be able to use it.
    public static final String PARAM_SECRET = "secret";
    private final PaymentGateway paymentGateway;

    @Inject
    public PaymentController(DaoProvider provider, PaymentGateway paymentGateway) {
        super(provider.userDao());
        this.paymentGateway = paymentGateway;
    }

    public Result processPayment() {
        DynamicForm form = Form.form().bindFromRequest();
        try {
            PaymentStatus status = paymentGateway.processPayment(form.data());
            LOG.info("Payment status: " + status);
            return redirectToClient(status);
        } catch (PaymentGatewayError e) {
            LOG.error("Failed to process payment! Error: " + e.getMessage(), e);
        }

        return badRequest("Payment Failed!");
    }

    public Result redirectToClient(PaymentStatus status) {
        try {
            return paymentGateway.redirectToClient(status);
        } catch (PaymentGatewayError e) {
            LOG.error("Failed to redirect back to client Payment Id: " + status.getPayment().id);
        }
        
        return badRequest("Error while processing payment");
    }

    public Result redirectToBank() {
        DynamicForm form = Form.form().bindFromRequest();
        String cipher = form.get(PARAM_SECRET);
        if (cipher == null || cipher.trim().length() == 0) {
            LOG.error("Attempt to initiate payment without secret populated");
            return badRequest("Bad Request");
        }

        try {
            LOG.debug("Redirect back URL: " + routes.PaymentController.processPayment().url());
            return paymentGateway.redirectToBank(cipher, routes.PaymentController.processPayment().url(), request()
                    .remoteAddress(), request().getHeader("User-Agent"), Locale.getDefault());
        } catch (PaymentGatewayError pge) {
            LOG.error("Could not redirect to payment gateway due to: " + pge.getMessage(), pge);
            return badRequest("Failed to redirect to payment gateway");
        }
    }

    protected Result invalidParameters(String message) {
        LOG.warn(message);
        return badRequest("Invalid Parameters");
    }
}
