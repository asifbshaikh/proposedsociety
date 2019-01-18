package models.dao.ebean;

import models.Insurance;
import models.User;
import models.dao.InsuranceDao;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;

public class EbeanInsuranceDao extends AbstractEbeanDao<Long, Insurance> implements InsuranceDao {

    public EbeanInsuranceDao() {
        super(new Finder<Long, Insurance>(Long.class, Insurance.class));
    }

    @Override
    public Insurance findById(Long id, User user) {
        String query = "find insurance fetch form fetch form.user where id = :id and form.user.id = :user_id";
        return Ebean.createQuery(Insurance.class, query)
                                .setParameter("id", id)
                                .setParameter("user_id", user.id).findUnique();
    }
    
}
