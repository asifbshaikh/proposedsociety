package models.dao.ebean;

import models.PaymentCode;
import models.dao.PaymentCodeDao;
import play.db.ebean.Model.Finder;

public class EbeanPaymentCodeDao extends AbstractEbeanDao<Long, PaymentCode> implements PaymentCodeDao {
    public EbeanPaymentCodeDao() {
        super(new Finder<Long, PaymentCode>(Long.class, PaymentCode.class));
    }

    @Override
    public PaymentCode findByCode(String code) {
        return finder.where().eq("code", code).findUnique();
    }
}
