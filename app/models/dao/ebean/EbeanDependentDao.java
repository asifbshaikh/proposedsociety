package models.dao.ebean;

import models.Dependent;
import models.User;
import models.dao.DependentDao;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;

public class EbeanDependentDao extends AbstractEbeanDao<Long, Dependent> implements DependentDao {
    public EbeanDependentDao() {
        super(new Finder<Long, Dependent>(Long.class, Dependent.class));
    }

    @Override
    public Dependent findById(Long id, User user) {
        String query = "find dependent fetch form fetch form.user where id = :id and form.user.id = :user_id";
        
        return Ebean.createQuery(Dependent.class, query)
                                .setParameter("id", id)
                                .setParameter("user_id", user.id).findUnique();
    }
}
