package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;

import play.data.validation.Constraints;
import play.data.validation.Constraints.Email;
import play.data.validation.ValidationError;
import play.db.ebean.Model;
import validation.Condition;
import validation.OptionalCascade;
import validation.RequiredAndNotEquals;

@Entity
@Table(name = "agent_form")
public class AgentForm extends Model{
	
	private static final long serialVersionUID = 1L;
    private static final String ADDRESS_TYPE_OTHER = "other";
    public static final Finder<Long, AgentForm> FIND = new Finder<Long, AgentForm>(Long.class, AgentForm.class);

    // Agent Details
    
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    public String status ;
   
   /* @Column(name = "form_number") 
    public String formNumber;*/
    
    @Constraints.Required public String title;
    @Constraints.Required(message = "Required Field!") public String fname;
    @Constraints.Required(message = "Required Field!") public String mname;
    @Constraints.Required(message = "Required Field!") public String lname;
    @Constraints.Required(message = "Required Field!") public Date dob;
    @Constraints.Required(message = "Required Field!") @Email public String email;
    @Constraints.Required(message = "Required Field!") public String sex;
    
        // Identity 1
    @Constraints.Required(message = "Required Field!") 
    public String identity_number1;
    @Constraints.Required(message = "Required Field!") 
    public String identity_number1_type;
    @OneToOne(cascade = CascadeType.DETACH)
    public FileAttachment identity1;
    @Constraints.Required
    @Transient
    public String identity1_id;

    // Identity 2
    public String identity_number2;
    public String identity_number2_type;
    @OneToOne(cascade = CascadeType.DETACH)
    public FileAttachment identity2;
    @Transient
    public String identity2_id;

    // Identity 3
    public String identity_number3;
    public String identity_number3_type;
    @OneToOne(cascade = CascadeType.DETACH)
    public FileAttachment identity3;
    @Transient
    public String identity3_id;

    @Constraints.Required(message = "Required Field!") public Character marital_status;
    
    // Father / Husband Details
    @Constraints.Required(message = "Required Field!") public String fh_title;
    @Constraints.Required(message = "Required Field!") public String fh_fname;
    @Constraints.Required(message = "Required Field!") public String fh_mname;
    @Constraints.Required(message = "Required Field!") public String fh_lname;
    @Constraints.Required(message = "Required Field!") public String fh_relation;
    
    
    //AgentAddress Details
    @Constraints.Required(message = "Required Field!") public String permanent_address_same_as;
    @Constraints.Required(message = "Required Field!") public String residential_address_same_as;
    @Constraints.Required(message = "Required Field!") public String communication_address_same_as;
    @Constraints.Required(message = "Required Field!") public String office_address_same_as;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) 
    @Valid
    public AgentAddress birth_place;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    @OptionalCascade(condition = PermAddressIsTypeOther.class)
    public AgentAddress permanent_address;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    @OptionalCascade(condition = ResiAddressIsTypeOther.class)
    public AgentAddress residential_address;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) 
    @Valid
    @OptionalCascade(condition = CommAddressIsTypeOther.class)
    public AgentAddress communication_address;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) 
    @Valid
    @OptionalCascade(condition = NriCheck.class)
    public AgentAddress nri_address ;
    
    @Constraints.Required(message = "Required Field!") public String phone1_type;
    @Constraints.Required(message = "Required Field!") 
    @Constraints.Pattern(value = "[\\+]?[0-9]+[\\-]?[\\-0-9]+", message = "please, insert valid number.")
    public String phone1;
   
    
    public String phone2_type;
    @Constraints.Pattern(value = "[\\+]?[0-9]+[\\-]?[\\-0-9]+", message = "please, insert valid number.")
    public String phone2;
    public String phone3_type;
    @Constraints.Pattern(value = "[\\+]?[0-9]+[\\-]?[\\-0-9]+", message = "please, insert valid number.")
    public String phone3;
    

    //Agent Office AgentAddress Details
    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE }) 
    @Valid
    @OptionalCascade(condition = OfficeAddressIsTypeOther.class)
    public AgentAddress office_address;
    
    @Email
    public String off_mail;
    public String website_addres;
    public String off_fax;
    @Constraints.Pattern(value = "[\\+]?[0-9]+[\\-]?[\\-0-9]+", message = "please, insert valid number.") 
    public String off_phone1;
    public String off_phone1_type;
    @Constraints.Pattern(value = "[\\+]?[0-9]+[\\-]?[\\-0-9]+", message = "please, insert valid number.") 
    public String off_phone2;
    public String off_phone2_type;
    @Constraints.Pattern(value = "[\\+]?[0-9]+[\\-]?[\\-0-9]+", message = "please, insert valid number.") 
    public String off_phone3;
    public String off_phone3_type;
    

    //Residential Status
    @Constraints.Required(message = "Required Field!") 
    public String resi_status;
    @Constraints.Required(message = "Required Field!")  
    public Integer residence_yrs_residing;
    @Constraints.Required(message = "Required Field!") 
    public Integer city_yrs_residing;
 


    public String occupation;
    @Constraints.Required(message = "Required Field!") 
    public String nationality;
    
    //Language
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    public LanguageProficiency marathi;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    public LanguageProficiency hindi;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    public LanguageProficiency english;
    
    //Highest Qualification
    @RequiredAndNotEquals(notEquals = {"select_"}, message ="Required Field!")
    public String qualification;
    public String otherQualification;
    
    //   Status of Agent
    @RequiredAndNotEquals(notEquals = {"select_"}, message ="Required Field!")
    public String agentType;
    @Constraints.Required(message = "Required Field!")  
    public String companyName;
    @Constraints.Required(message = "Required Field!")  
    public String   isIncomeTaxPayee;
    @Constraints.Required(message = "Required Field!")
    public String panCardNumber;
    @Constraints.Required(message = "Required Field!")  
    public String onlineMonatoryTransactionFacility;
    
    //Bank Account Particulars   
    @Constraints.Required(message = "Required Field!")  
	public String bankName;
    @Constraints.Required(message = "Required Field!") 
	public String branchName;
    @Constraints.Required(message = "Required Field!") 
    public Long bankAccount;
    
    //Agent form place and date 
    public String formFilledPlace;
    // @Constraints.Required(message = "Required Field!")
    public String formFilledDay;
    
    public Long formFilledYear;
    
    // Photograph Attachment
    @OneToOne(cascade = CascadeType.DETACH)
    public FileAttachment photograph;
    @Constraints.Required
    @Transient
    public String photograph_id;

    // AgentAddress Proof Attachment
    @OneToOne(cascade = CascadeType.DETACH)
    public FileAttachment addressproof;
    @Constraints.Required
    @Transient
    public String addressproof_id;
    
    @OneToOne(cascade = CascadeType.DETACH)
    public FileAttachment resi_light_bill;
    @Constraints.Required
    @Transient
    public String resi_light_bill_id;
    
    @OneToOne(cascade = CascadeType.DETACH)
    public FileAttachment resi_tax_receipt;
    @Constraints.Required
    @Transient
    public String resi_tax_receipt_id;
    
	// BirthCertificate Proof Attachment
    @OneToOne(cascade = CascadeType.DETACH)
    public FileAttachment birthCertificate;
    @Constraints.Required
    @Transient
    public String birthCertificate_id;
    
    // Agent Office Proof Attachment
    @OneToOne(cascade = CascadeType.DETACH)
    public FileAttachment office_addressproof;
    @Transient
    public String office_addressproof_id;
    
    @OneToOne(cascade = CascadeType.DETACH)
    public FileAttachment office_light_bill;
    @Transient
    public String office_light_bill_id;
    
    //Verification
   // @Constraints.Required(message = "Required Field!") 
    public String verificationCode;
    
    @OneToOne
    public User user;
    
    
    public static class PermAddressIsTypeOther implements Condition {
        @Override
        public boolean shouldCascade(Object bean) {
            AgentForm a = AgentForm.class.cast(bean);
            return a.permanent_address_same_as != null && ADDRESS_TYPE_OTHER.equals(a.permanent_address_same_as);
        }
    }

    public static class ResiAddressIsTypeOther implements Condition {
        @Override
        public boolean shouldCascade(Object bean) {
            AgentForm a = AgentForm.class.cast(bean);
            return a.residential_address_same_as != null && ADDRESS_TYPE_OTHER.equals(a.residential_address_same_as);
        }
    }

    public static class CommAddressIsTypeOther implements Condition {
        @Override
        public boolean shouldCascade(Object bean) {
            AgentForm a = AgentForm.class.cast(bean);
            return a.communication_address_same_as != null && ADDRESS_TYPE_OTHER.equals(a.communication_address_same_as);
        }
    }
    public static class NriCheck implements Condition{

		@Override
		public boolean shouldCascade(Object bean) {
		    AgentForm a = AgentForm.class.cast(bean);
			return (a.nationality != null && a.nationality.equals("N"));
		}
    	
    }
    
    public static class OfficeAddressIsTypeOther implements Condition {
        @Override
        public boolean shouldCascade(Object bean) {
            AgentForm a = AgentForm.class.cast(bean);
            return a.office_address_same_as != null && ADDRESS_TYPE_OTHER.equals(a.office_address_same_as);
        }
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AgentForm [id=");
		builder.append(id);
		builder.append(", status=");
		builder.append(status);
		builder.append(", title=");
		builder.append(title);
		builder.append(", fname=");
		builder.append(fname);
		builder.append(", mname=");
		builder.append(mname);
		builder.append(", lname=");
		builder.append(lname);
		builder.append(", dob=");
		builder.append(dob);
		builder.append(", email=");
		builder.append(email);
		builder.append(", sex=");
		builder.append(sex);
		builder.append(", identity_number1=");
		builder.append(identity_number1);
		builder.append(", identity_number1_type=");
		builder.append(identity_number1_type);
		builder.append(", identity1=");
		builder.append(identity1);
		builder.append(", identity1_id=");
		builder.append(identity1_id);
		builder.append(", identity_number2=");
		builder.append(identity_number2);
		builder.append(", identity_number2_type=");
		builder.append(identity_number2_type);
		builder.append(", identity2=");
		builder.append(identity2);
		builder.append(", identity2_id=");
		builder.append(identity2_id);
		builder.append(", identity_number3=");
		builder.append(identity_number3);
		builder.append(", identity_number3_type=");
		builder.append(identity_number3_type);
		builder.append(", identity3=");
		builder.append(identity3);
		builder.append(", identity3_id=");
		builder.append(identity3_id);
		builder.append(", marital_status=");
		builder.append(marital_status);
		builder.append(", fh_title=");
		builder.append(fh_title);
		builder.append(", fh_fname=");
		builder.append(fh_fname);
		builder.append(", fh_mname=");
		builder.append(fh_mname);
		builder.append(", fh_lname=");
		builder.append(fh_lname);
		builder.append(", fh_relation=");
		builder.append(fh_relation);
		builder.append(", permanent_address_same_as=");
		builder.append(permanent_address_same_as);
		builder.append(", residential_address_same_as=");
		builder.append(residential_address_same_as);
		builder.append(", communication_address_same_as=");
		builder.append(communication_address_same_as);
		builder.append(", office_address_same_as=");
		builder.append(office_address_same_as);
		builder.append(", birth_place=");
		builder.append(birth_place);
		builder.append(", permanent_address=");
		builder.append(permanent_address);
		builder.append(", residential_address=");
		builder.append(residential_address);
		builder.append(", communication_address=");
		builder.append(communication_address);
		builder.append(", nri_address=");
		builder.append(nri_address);
		builder.append(", phone1_type=");
		builder.append(phone1_type);
		builder.append(", phone1=");
		builder.append(phone1);
		builder.append(", phone2_type=");
		builder.append(phone2_type);
		builder.append(", phone2=");
		builder.append(phone2);
		builder.append(", phone3_type=");
		builder.append(phone3_type);
		builder.append(", phone3=");
		builder.append(phone3);
		builder.append(", office_address=");
		builder.append(office_address);
		builder.append(", off_mail=");
		builder.append(off_mail);
		builder.append(", website_addres=");
		builder.append(website_addres);
		builder.append(", off_fax=");
		builder.append(off_fax);
		builder.append(", off_phone1=");
		builder.append(off_phone1);
		builder.append(", off_phone1_type=");
		builder.append(off_phone1_type);
		builder.append(", off_phone2=");
		builder.append(off_phone2);
		builder.append(", off_phone2_type=");
		builder.append(off_phone2_type);
		builder.append(", off_phone3=");
		builder.append(off_phone3);
		builder.append(", off_phone3_type=");
		builder.append(off_phone3_type);
		builder.append(", resi_status=");
		builder.append(resi_status);
		builder.append(", residence_yrs_residing=");
		builder.append(residence_yrs_residing);
		builder.append(", city_yrs_residing=");
		builder.append(city_yrs_residing);
		builder.append(", occupation=");
		builder.append(occupation);
		builder.append(", nationality=");
		builder.append(nationality);
		builder.append(", marathi=");
		builder.append(marathi);
		builder.append(", hindi=");
		builder.append(hindi);
		builder.append(", english=");
		builder.append(english);
		builder.append(", qualification=");
		builder.append(qualification);
		builder.append(", otherQualification=");
		builder.append(otherQualification);
		builder.append(", agentType=");
		builder.append(agentType);
		builder.append(", companyName=");
		builder.append(companyName);
		builder.append(", isIncomeTaxPayee=");
		builder.append(isIncomeTaxPayee);
		builder.append(", panCardNumber=");
		builder.append(panCardNumber);
		builder.append(", onlineMonatoryTransactionFacility=");
		builder.append(onlineMonatoryTransactionFacility);
		builder.append(", bankName=");
		builder.append(bankName);
		builder.append(", branchName=");
		builder.append(branchName);
		builder.append(", bankAccount=");
		builder.append(bankAccount);
		builder.append(", formFilledPlace=");
		builder.append(formFilledPlace);
		builder.append(", formFilledDay=");
		builder.append(formFilledDay);
		builder.append(", photograph=");
		builder.append(photograph);
		builder.append(", photograph_id=");
		builder.append(photograph_id);
		builder.append(", addressproof=");
		builder.append(addressproof);
		builder.append(", addressproof_id=");
		builder.append(addressproof_id);
		builder.append(", resi_light_bill=");
		builder.append(resi_light_bill);
		builder.append(", resi_light_bill_id=");
		builder.append(resi_light_bill_id);
		builder.append(", resi_tax_receipt=");
		builder.append(resi_tax_receipt);
		builder.append(", resi_tax_receipt_id=");
		builder.append(resi_tax_receipt_id);
		builder.append(", birthCertificate=");
		builder.append(birthCertificate);
		builder.append(", birthCertificate_id=");
		builder.append(birthCertificate_id);
		builder.append(", office_addressproof=");
		builder.append(office_addressproof);
		builder.append(", office_addressproof_id=");
		builder.append(office_addressproof_id);
		builder.append(", office_light_bill=");
		builder.append(office_light_bill);
		builder.append(", office_light_bill_id=");
		builder.append(office_light_bill_id);
		builder.append(", verificationCode=");
		builder.append(verificationCode);
		builder.append(", user=");
		builder.append(user);
		builder.append("]");
		return builder.toString();
	}
    
	public List<ValidationError> validate(){
		List<ValidationError> errors = new  ArrayList<ValidationError>();
		if(!office_address_same_as.equals("notHave")){
			if(office_addressproof_id == null ){
				errors.add(new ValidationError("office_addressproof_id", "Required Field"));
			}
			if(office_addressproof_id.equals("")){
				errors.add(new ValidationError("office_addressproof_id", "Required Field"));
			}
			if(office_light_bill_id == null ){
				errors.add(new ValidationError("office_light_bill_id", "Required Field"));
			}
			if(office_light_bill_id.equals("")){
				errors.add(new ValidationError("office_light_bill_id", "Required Field"));
			}
		}
		
		return errors.isEmpty()? null : errors;
		
	}
}
