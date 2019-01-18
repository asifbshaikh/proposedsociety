package models.dao;

import java.util.List;
import models.Buero;
import models.User;

public interface BueroDao extends Dao<Long, Buero>{
	List<Buero> getInvestigationBueros();
	Buero findUniqueByUserId(Long userId);
	boolean findIfNotExist(User user);
}
