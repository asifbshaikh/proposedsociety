package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Summary  extends Model {
	private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    
    @Required(message = "Required Field!") public Character iAccptAbvCal;
	public Boolean approxLoanEli;
	public Boolean allBankBalSummAmnt;
	public Boolean cashBalance;
	public Boolean allFdSummAmnt;
	public Boolean allRdSummAmnt;
	public Boolean allInsuranceSummAmnt;
	public Boolean sellingSummAmnt;
	public Boolean borrowedSummary;
	public Boolean donationSummary;
	public Boolean returnedAmount;
}
