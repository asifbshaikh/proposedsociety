package models;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.data.format.Formats;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;


@Entity
public class FixedDeposit extends Model {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id; 
    @Required(message = "Required Field!") public String bankName;
    @Required(message = "Required Field!") public String nameOfHolder;
    
    @Formats.DateTime(pattern="dd/MM/yyyy")
    @Temporal(TemporalType.TIMESTAMP)
    @Required(message = "Required Field!") public Date startDate;
    
    @Formats.DateTime(pattern="dd/MM/yyyy")
    @Temporal(TemporalType.TIMESTAMP)
    @Required(message = "Required Field!") public Date maturityDate;
    
    @Required(message = "Required Field!") public Integer pricipal;
    @Required(message = "Required Field!") public BigDecimal interestRate;
    @Required(message = "Required Field!") public Integer maturityAmount;
    @Required(message = "Required Field!") public Boolean haveTakenLoanOnFD;
    @Required(message = "Required Field!") public Boolean monthlyAmntDedctedFrmSal;
    //@OneToOne (cascade = CascadeType.ALL)public LoanDetails loanDetails;
    @Required(message = "Required Field!") public Boolean willSurrender;
    @Required(message = "Required Field!") public Integer allocation;
    @Required(message = "Required Field!") public Integer amountCanAvail;
    
    public String financer;
    public Integer loanAmount;
    public Integer montlyEmi;
    public Integer installmentsPaid;
    public Integer balanceInstallments;
    public Integer balanceLoanAmount;
    
    @ManyToOne
    public ApplicationForm form;
}
