package models.dao.ebean;

import models.LoanDetails;
import models.User;
import models.dao.LoanDetailsDao;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;

public class EbeanLoanDetailsDao extends AbstractEbeanDao<Long, LoanDetails> implements LoanDetailsDao {
    
    public EbeanLoanDetailsDao() {
        super(new Finder<Long, LoanDetails>(Long.class, LoanDetails.class));
    }

    @Override
    public LoanDetails findById(Long id, User user) {
        String query = "find loan fetch form fetch form.user where id = :id and form.user.id = :user_id";
        
        return Ebean.createQuery(LoanDetails.class, query)
                                .setParameter("id", id)
                                .setParameter("user_id", user.id).findUnique();
    }
}
