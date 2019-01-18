package models.dao;


import models.FixedDeposit;
import models.User;

public interface FDDetailsDao extends Dao <Long, FixedDeposit> {
	
	FixedDeposit findById(Long id,User user);
}
