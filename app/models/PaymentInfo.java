package models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.Constraint;

import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class PaymentInfo extends Model{
	private static final long serialVersionUID = 1L;

	//Common All Mode
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id; 
	@Constraints.Required(message = "Required Field!")    public String paymentMode;
	@Constraints.Required(message = "Required Field!")    public String bankName;
	@Constraints.Required(message = "Required Field!")    public String branchName;
	@Constraints.Required(message = "Required Field!")    public Date paymentDate;

	@OneToOne(cascade = CascadeType.ALL) public ApplicationForm form;
	
	//By Cheque
	@Constraints.Required(message = "Required Field!") 	public Integer chequeNumber;

	//By NetBanking 
	@Constraints.Required(message = "Required Field!")	public String transactionId;
	@Constraints.Required(message = "Required Field!") 	public String accountNumber;
	
	//By Deposite
	@Constraints.Required(message = "Required Field!") 	public Integer receiptNumber;

	@Override
	public String toString() {
		return "PaymentInfo [paymentMode=" + paymentMode + ", bankName="
				+ bankName + ", branchName=" + branchName + ", date=" + paymentDate
				+ ", chequeNumber=" + chequeNumber + ", transactionId="
				+ transactionId + ", accountNumber=" + accountNumber
				+ ", receiptNumber=" + receiptNumber + "]";
	}

	

}
