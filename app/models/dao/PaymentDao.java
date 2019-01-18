package models.dao;

import models.Payment;

public interface PaymentDao extends Dao<Long, Payment> {
    Payment findByMerchantTxnId(String id);
    Payment findByInvoiceId(Long id);
}
