package models.dao.ebean;

import models.Property;
import models.User;
import models.dao.PropertyDao;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;

public class EbeanPropertyDao extends AbstractEbeanDao<Long, Property> implements PropertyDao {
    public EbeanPropertyDao() {
        super(new Finder<Long, Property>(Long.class, Property.class));
    }

    @Override
    public Property findById(Long id, User user) {
        String query = "find peroperty fetch form fetch form.user where id = :id and form.user.id = :user_id";
        
        return Ebean.createQuery(Property.class, query)
                                .setParameter("id", id)
                                .setParameter("user_id", user.id).findUnique();    }
}
