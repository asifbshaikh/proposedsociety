package models.dao;

import models.RecurringDeposit;
import models.User;

public interface RDDetailsDao extends Dao <Long, RecurringDeposit> {
	RecurringDeposit findById(Long id,User user);
}
