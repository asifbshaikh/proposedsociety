package models.dao;

import java.util.List;

import models.Page;

public interface PageDao extends Dao<Long, Page> {
	List<Page> findAll();
}
