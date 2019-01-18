package models.dao;

import models.BankAccount;
import models.User;

public interface BankAccountDao extends Dao<Long, BankAccount> {
	BankAccount findById(Long id,User user);
}
