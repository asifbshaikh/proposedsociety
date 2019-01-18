package models;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class RecurringDeposit extends Model {

    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    public String bankName;
    public String nameOfHolder;
    public Date startDate;
    public Date maturityDate;
    public Integer installment;
    public Integer installmentsPaid;
    public Integer installmentsBalance;
    public Integer principal;
    public BigDecimal interestRate;
    public Integer maturityAmount;
    public Integer monthlyInstallments;
    public Integer balanceInstallments;
    public Integer balanceDedAmount;
    public Boolean monthlyAmntDedctedFrmSal;
    public Boolean willSurrender;
    public Integer amountCanAvail;
    public Integer allocation;
    
    @ManyToOne
    public ApplicationForm form;
}
