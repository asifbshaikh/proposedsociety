package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;


@Entity
public class LoanDetails extends Model {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    public boolean amountPerMonthFromSal;
    @Required(message = "Required Field!") public String financer;
    @Required(message = "Required Field!") public Integer loanAmount;
    @Required(message = "Required Field!") public Integer monthlyEmi;
    @Required(message = "Required Field!") public Integer installmentsPaid;
    @Required(message = "Required Field!") public Integer installmentsBalance;
    public Integer balanceLoanAmount;
    public Boolean isApplicant;

    @ManyToOne
    public ApplicationForm form;
}
