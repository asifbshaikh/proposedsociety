package models.dao.ebean;

import java.util.List;

import play.db.ebean.Model.Finder;
import models.Page;
import models.dao.PageDao;

public class EbeanPageDao extends AbstractEbeanDao<Long, Page> implements PageDao {
	public EbeanPageDao() {
		super(new Finder<Long, Page>(Long.class, Page.class));
	}

	@Override
	public List<Page> findAll() {
		return finder.all();
	}
}
