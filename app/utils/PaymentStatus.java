package utils;

import models.Payment;

public class PaymentStatus {
    public static enum Status {
        SUCCESS, FAILURE;
    }

    private final Status status;
    private final Payment payment;

    public PaymentStatus(Status status, Payment payment) {
        super();
        this.status = status;
        this.payment = payment;
    }

    public Status getStatus() {
        return status;
    }

    public Payment getPayment() {
        return payment;
    }

    @Override
    public String toString() {
        return "PaymentStatus [status=" + status + ", payment=" + payment + "]";
    }
}
