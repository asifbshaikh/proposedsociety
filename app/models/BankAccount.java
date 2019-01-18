package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class BankAccount extends Model {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    public String bankName;
    public String accountNumber;
    public Integer balance;
    public boolean anyDedFromBankAccount;
    public boolean loan;
    public Integer loanEmi;
    public Integer balanceInstallments;
    public Integer balanceLoanAmount;
    public Integer otherMonthlyDeduction;
    public Integer totalMonthlyDeduction;
    public Integer allocateToBuy;
    
    @ManyToOne
    public ApplicationForm form;
}