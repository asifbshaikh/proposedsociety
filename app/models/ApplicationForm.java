package models;

import static utils.CryptUtils.decrypt;
import static utils.CryptUtils.desalt;
import static utils.CryptUtils.encrypt;
import static utils.CryptUtils.salt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.JoinColumn;
import javax.validation.Valid;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import validation.Condition;
import validation.OptionalCascade;

@Entity @Table(name = "form")
public class ApplicationForm extends Model {
    
    //Two groups PrePaymnet and PostPayment
    public interface PrePayment	{ }
    public interface PostPayment{ }
	
    private static final long serialVersionUID = 1L;
    
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    public String status;
    
    @Column(name = "form_number") 
    public String formNumber;
    
    public Date filled_date;
    public Date completed_date;
    
    @OneToOne(cascade = CascadeType.ALL)
    @Valid
    public Applicant applicant;
    
    @OneToOne(cascade = CascadeType.ALL)
    @Valid
    @OptionalCascade(condition = DoesCoApplicantExist.class)
    public Applicant co_applicant;
    
    @OneToMany(mappedBy= "form", cascade = CascadeType.ALL)
    @JoinColumn(name = "form_id")
    public List<Dependent> dependents;
    
    @OneToMany(mappedBy= "form", cascade = CascadeType.ALL)
    @JoinColumn(name = "form_id")
    public List<LoanDetails> loanDetailsList;
    
    @Transient public Budget budget;
    
    @OneToMany(cascade = CascadeType.ALL) 
    @JoinColumn(name = "form_id")
    public List<Requirement> requirements;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL)
    @JoinColumn(name = "form_id")
    public List<MiscBorrowing> miscBorrowings;
    
    @OneToMany(cascade = CascadeType.ALL) 
    @JoinColumn(name = "form_id")
    public List<Property> property;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL)
    @JoinColumn(name = "form_id") 
    public List<BankAccount> bankAccounts;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL)
    @JoinColumn(name = "form_id") 
    public List<FixedDeposit> fixedDeposits;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL)
    @JoinColumn(name = "form_id") 
    public List<RecurringDeposit> recurringDeposits;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL)
    @JoinColumn(name = "form_id")
    public List<Insurance> insurances;
    
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!") public Boolean coApplicantExists;
    
    @OneToOne(cascade = CascadeType.ALL)
    public Summary summary;
    
   // @Constraints.Required(message = "Required Field!") 
    public Integer personalContribution;
    public Integer calculatedBudget;
    public Integer cashWithYou;
    
    public String shortFormApplicantIncomePer;
    public String shortFormCoApplicantIncomePer;
    public BigDecimal applicantIncomeShortForm;
    public BigDecimal coApplicantIncomeShortForm;
    public BigDecimal requiredLoan;
    public BigDecimal totalBudget;
    public String registrationBy;
    //public String agentId;
    
    @Transient
    public String agentCode;
    
    @OneToOne(cascade = CascadeType.ALL)
    public Agent agent;
    
    @OneToOne(mappedBy = "form")
    public User user;
    
    @OneToOne(mappedBy = "form")
    public PaymentInfo offlinePayment;
    
    @OneToOne(cascade = CascadeType.ALL)
    public Invoice invoice;
    
    
    public List<Dependent> getApplicantDependents() {
    	if(dependents != null){
	    	List<Dependent> dps = new ArrayList<Dependent>();
	    	for(Dependent depe : dependents) {
	    		if(depe.isApplicant == true){
	    			dps.add(depe);
	    		}
	    	}
	    	return dps; 
    	}
    	return null;
	}
    
    public List<Dependent> getCoApplicantDependents() {
    	if(dependents != null){
	    	List<Dependent> dps = new ArrayList<Dependent>();
	    	for(Dependent coappDepe : dependents) {
	    		if(coappDepe.isApplicant != true){
	    			dps.add(coappDepe);
	    		}
	    	}
	    	return dps;
    	}
    	return null;
	}
    
    public List<LoanDetails> getApplicantLoanDetails() {
    	if(loanDetailsList != null){
	    	List<LoanDetails> lds = new ArrayList<LoanDetails>();
	    	for(LoanDetails ld : loanDetailsList) {
	    		if(ld.isApplicant == true){
	    			lds.add(ld);
	    		}
	    	}
		    return lds;
    	}
    	return null;
	}
    
    public List<LoanDetails> getCoApplicantLoanDetails() {
    	if(loanDetailsList != null){
	    	List<LoanDetails> lds = new ArrayList<LoanDetails>();
	    	for(LoanDetails coappLd : loanDetailsList) {
	    		if(coappLd.isApplicant != true){
	    			lds.add(coappLd);
	    		}
	    	}
		    return lds;
    	}
    	return null;
	}
    
    public static class DoesCoApplicantExist implements Condition {
        @Override
        public boolean shouldCascade(Object bean) {
            ApplicationForm af = ApplicationForm.class.cast(bean);
            return af.coApplicantExists != null && af.coApplicantExists == true;
        }
    }
    
    public static String generateRandomText() {
        return String.valueOf(System.currentTimeMillis());
    }
    
    public Applicant generateName(String val) {
        if(val.equals("applicant")){
        	return applicant;
        }else{
        	return co_applicant;
        }
    }
    public Integer getPersonalContribution(){
    	if(requiredLoan != BigDecimal.ZERO && totalBudget != BigDecimal.ZERO ){
    		personalContribution =requiredLoan.subtract(totalBudget).intValueExact();
    	}
    	return personalContribution;
    }
    
    public String encryptId() {
        String salted = salt(String.valueOf(this.id));
        String encrypted = encrypt(salted);
        return encrypted;
    }

    public static long decryptId(String encryptedId)  {
        String id = desalt(decrypt(encryptedId));
            return Long.parseLong(id);
    }
    
}
