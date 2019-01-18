package models.dao.ebean;

import models.MiscBorrowing;
import models.User;
import models.dao.MiscBorrowingDao;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;

public class EbeanMiscBorrowingDao extends AbstractEbeanDao<Long, MiscBorrowing> implements MiscBorrowingDao {
    public EbeanMiscBorrowingDao() {
        super(new Finder<Long, MiscBorrowing>(Long.class, MiscBorrowing.class));
    }

    @Override
    public MiscBorrowing findById(Long id, User user) {
        String query = "find borrowing fetch form fetch form.user where id = :id and form.user.id = :user_id";
        
        return Ebean.createQuery(MiscBorrowing.class, query)
                                .setParameter("id", id)
                                .setParameter("user_id", user.id).findUnique();
    }

    
} 