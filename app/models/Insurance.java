package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class Insurance extends Model {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    public String nameOfHolder;
    public String insurerName;
    public Date startDate;
    public Date maturityDate;
    public Integer sumAssured;
    public String premiumFreqency;
    public Integer premium;
    public Integer premiumsPaid;
    public Integer premiumsBalance;
    public Date lastPaymentDate;
    public Integer maturityAmount;
    public boolean monthlyAmntDedctedFrmSal;
    public boolean receivingAnyMaturedAmount;
    public Integer allocation;
    
    @ManyToOne
    public ApplicationForm form;
    
/*    @ManyToOne
    @Required
    public OwnContribution ownContribution;


    public Insurance(OwnContribution ownContribution, String nameOfHolder, String insurerName, Date startDate, Date maturityDate, String sumAssured, String premiumFreqency, String premium, String premiumsPaid, String premiumsBalance, Date lastPaymentDate, String maturityAmount, String monthlyAmntDedctedFrmSal, String receivingAnyMaturedAmount, String allocation)
    {
        this.ownContribution = ownContribution;
        this.sumAssured = Integer.parseInt(sumAssured);
        this.premium = Integer.parseInt(premium);
        this.premiumsBalance = Integer.parseInt(premiumsBalance);
        this.premiumsPaid = Integer.parseInt(premiumsPaid);
        this.maturityAmount = Integer.parseInt(maturityAmount);
        this.monthlyAmntDedctedFrmSal = Boolean.valueOf(monthlyAmntDedctedFrmSal);
        this.receivingAnyMaturedAmount = Boolean.valueOf(receivingAnyMaturedAmount);
        this.allocation = Integer.parseInt(allocation);

        this.nameOfHolder = nameOfHolder;
        this.insurerName = insurerName;
        this.startDate = startDate;
        this.maturityDate = maturityDate;
        this.premiumFreqency = premiumFreqency;
        this.lastPaymentDate = lastPaymentDate;
    } */
}
