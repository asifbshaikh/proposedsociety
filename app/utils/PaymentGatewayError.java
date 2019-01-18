package utils;

import models.Payment;

public final class PaymentGatewayError extends Exception {
    private static final long serialVersionUID = 1L;
    private Payment payment;

    public PaymentGatewayError() {
    }

    public PaymentGatewayError(String message) {
        super(message);
    }

    public PaymentGatewayError(String message, Payment payment) {
        super(message);
        this.payment = payment;
    }

    public PaymentGatewayError(Throwable cause) {
        super(cause);
    }

    public PaymentGatewayError(String message, Throwable cause) {
        super(message, cause);
    }

    public Payment getPayment() {
        return payment;
    }

    public boolean hasPayment() {
        return payment != null;
    }
}
