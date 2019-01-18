package models.dao.ebean;
import java.util.List;


import play.db.ebean.Model.Finder;
import models.Buero;
import models.User;
import models.dao.BueroDao;

public class EbeanBueroDao extends AbstractEbeanDao<Long, Buero> implements BueroDao {

	protected EbeanBueroDao() {
		super(new Finder<Long, Buero>(Long.class, Buero.class));
	}

	@Override
	public List<Buero> getInvestigationBueros() {
		List<Buero> ibDetails =finder.all();
	        return ibDetails;
	}
	
	@Override
	public Buero findUniqueByUserId(Long userId){
		return finder.where().eq("user_id",userId).findUnique();
	}

	@Override
	public boolean findIfNotExist(User user) {
		Buero buero = finder.where().eq("user_id",user.id).findUnique();
		if(buero == null){return true;}
		return false;
	}
}

