package models.dao;

import models.PaymentCode;

public interface PaymentCodeDao extends Dao<Long, PaymentCode> {
    PaymentCode findByCode(String code);
}
