package models.dao;

import models.Dependent;
import models.User;

public interface DependentDao extends Dao<Long, Dependent> {
	Dependent findById(Long id,User user);
}