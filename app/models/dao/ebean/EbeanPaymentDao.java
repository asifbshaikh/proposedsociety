package models.dao.ebean;

import models.Payment;
import models.dao.PaymentDao;
import play.db.ebean.Model.Finder;

public class EbeanPaymentDao extends AbstractEbeanDao<Long, Payment> implements PaymentDao {

    public EbeanPaymentDao() {
        super(new Finder<Long, Payment>(Long.class, Payment.class));
    }

    @Override
    public Payment findByMerchantTxnId(String id) {
        return finder.where().eq("transactionId", id).findUnique();
    }

	@Override
	public Payment findByInvoiceId(Long id) {
		return finder.where().eq("invoice_id", id).findUnique();
	}
}
