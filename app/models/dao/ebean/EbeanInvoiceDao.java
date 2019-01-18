package models.dao.ebean;

import models.Invoice;
import models.dao.InvoiceDao;
import play.db.ebean.Model.Finder;

public class EbeanInvoiceDao extends AbstractEbeanDao<Long, Invoice> implements InvoiceDao {

    public EbeanInvoiceDao() {
        super(new Finder<Long, Invoice>(Long.class, Invoice.class));
    }
}
