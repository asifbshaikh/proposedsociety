package models;

import java.util.Date;
import models.ApplicationForm.PostPayment;
import models.ApplicationForm.PrePayment;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.Valid;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Email;
import play.db.ebean.Model;
import validation.Condition;
import validation.OptionalCascade;
import validation.RequiredAndNotEquals;

@Entity
public class Applicant extends Model {
	
	

    private static final long serialVersionUID = 1L;
    private static final String ADDRESS_TYPE_OTHER = "other";
    private static final String OCCUPATION_TYPE_EMPLOYED = "E";
    private static final String OCCUPATION_TYPE_SELF_EMPLOYED = "S";
    
    public static final Finder<Long, Applicant> FIND = new Finder<Long, Applicant>(Long.class, Applicant.class);
    
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;

    // Applicant Details
    @Constraints.Required(groups = { PrePayment.class}) public String title;
    @Constraints.Required(groups = { PrePayment.class},message = "Required Field!") public String fname;
    @Constraints.Required(groups = { PrePayment.class},message = "Required Field!") public String mname;
    @Constraints.Required(groups = { PrePayment.class},message = "Required Field!") public String lname;
    @Constraints.Required(groups = { PrePayment.class},message = "Required Field!") public Date dob;
    @Constraints.Required(groups = { PrePayment.class,PostPayment.class },message = "Required Field!") @Email public String email;
    @Constraints.Required(groups = { PrePayment.class,PostPayment.class },message = "Required Field!") public String sex;
    
    // Identity 1
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!") 
    public String identity_number1;
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!") 
    public String identity_number1_type;
    @OneToOne(cascade = CascadeType.DETACH)
    public FileAttachment identity1;
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!") 
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

    // Father / Husband Details
    @Constraints.Required(groups = { PrePayment.class,PostPayment.class } ,message = "Required Field!") public String fh_title;
    @Constraints.Required(groups = { PrePayment.class,PostPayment.class },message = "Required Field!") public String fh_fname;
    @Constraints.Required(groups = { PrePayment.class,PostPayment.class },message = "Required Field!") public String fh_mname;
    @Constraints.Required(groups = { PrePayment.class,PostPayment.class },message = "Required Field!") public String fh_lname;
    @Constraints.Required(groups = { PrePayment.class,PostPayment.class },message = "Required Field!") public String fh_relation;

    @Constraints.Required(groups = { PrePayment.class,PostPayment.class },message = "Required Field!") public Character marital_status;

    // Spouse Details
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "spouse_details_id") 
    //@Valid
    public SpouseDetails spouse_details;

    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!") public String permanent_address_same_as;
    @Constraints.Required(groups = { PrePayment.class },message = "Required Field!") public String residential_address_same_as;
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!") public String communication_address_same_as;
    
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) 
    @Valid
    @OptionalCascade(condition = AlwaysTrue.class, groups = {PostPayment.class } )
    public Address birth_place;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    @OptionalCascade(condition = PermAddressIsTypeOther.class,groups = {PostPayment.class })
    public Address permanent_address;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    @OptionalCascade(condition = ResiAddressIsTypeOther.class,groups ={ PostPayment.class})
    public Address residential_address;
    
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) 
    @Valid
    @OptionalCascade(condition = CommAddressIsTypeOther.class,groups = {PostPayment.class })
    public Address communication_address;

    // Phone 1
    @Constraints.Required(groups = { PrePayment.class,PostPayment.class },message = "Required Field!") 
    public String phone1_type;
    @Constraints.Required(groups = { PrePayment.class,PostPayment.class },message = "Required Field!") 
    @Constraints.Pattern(value = "[\\+]?[0-9]+[\\-]?[\\-0-9]+", message = "please, insert valid number.")
    @Constraints.MinLength(value = 10, message = "Mobile Number should be atleast 10 digit.") 
    public String phone1;

    // Phone 2
    public String phone2_type;
    @Constraints.Pattern(value = "[\\+]?[0-9]+[\\-]?[\\-0-9]+", message = "please, insert valid number.")
    public String phone2;

    // Phone 3
    public String phone3_type;
    @Constraints.Pattern(value = "[\\+]?[0-9]+[\\-]?[\\-0-9]+", message = "please, insert valid number.")
    public String phone3;
    
    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE }) 
    @Valid
    @OptionalCascade(condition = AlwaysTrue.class, groups = {PostPayment.class } )
    public Address office_address;
    
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!")
    @Email
    public String off_mail;
    
    public String employer_web;

    // Office Phone 1 
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!")
    @Constraints.Pattern(value = "[\\+]?[0-9]+[\\-]?[\\-0-9]+", message = "please, insert valid number.") 
    public String off_phone1;
    public String off_phone1_type;

    // Office Phone 2
    @Constraints.Pattern(value = "[\\+]?[0-9]+[\\-]?[\\-0-9]+", message = "please, insert valid number.") 
    public String off_phone2;
    public String off_phone2_type;
    
    // Office Phone 3
    @Constraints.Pattern(value = "[\\+]?[0-9]+[\\-]?[\\-0-9]+", message = "please, insert valid number.") 
    public String off_phone3;
    public String off_phone3_type;
    
    public String resi_status;
    
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!")  
    public Integer residence_yrs_residing;
    
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!") 
    public Integer city_yrs_residing;
    
    public Integer approx_eligibility;
    
    @RequiredAndNotEquals(notEquals = {"select_"}, groups = { PostPayment.class },message ="Required Field!")
    public String occupation;
    
    // Loan details.
    public boolean loan;
    
    // Employed Income Details
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    @OptionalCascade(condition = OccupationIsEmployed.class)
    @Column(name = "employed_income_id")
    public EmployedIncome employedIncome;
    
    // Self-Employed Income Details
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    @OptionalCascade(condition = OccupationIsSelfEmployed.class)
    @Column(name = "self_employed_income_id")
    public SelfEmployedIncome selfEmployedIncome;
    
    //Relationship With Applicant
    public String relationship_with_applicant;

    // Photograph Attachment
    @OneToOne(cascade = CascadeType.DETACH)
    public FileAttachment photograph;
    @Constraints.Required(groups = {PostPayment.class})
    @Transient
    public String photograph_id;

    // Address Proof Attachment
    @OneToOne(cascade = CascadeType.DETACH)
    public FileAttachment addressproof;
    @Constraints.Required(groups = {PostPayment.class})
    @Transient
    public String addressproof_id;
    
    
    @Override
    public String toString() {
        return "Applicant [id=" + id + ", title=" + title + ", fname=" + fname + ", mname=" + mname + ", lname=" + lname + ", dob=" + dob
                + ", email=" + email + ", sex=" + sex + ", identity_number1=" + identity_number1 + ", identity_number1_type=" + identity_number1_type
                + ", identity_number2=" + identity_number2 + ", identity_number2_type=" + identity_number2_type + ", identity_number3="
                + identity_number3 + ", identity_number3_type=" + identity_number3_type + ", fh_title=" + fh_title + ", fh_fname=" + fh_fname
                + ", fh_mname=" + fh_mname + ", fh_lname=" + fh_lname + ", fh_relation=" + fh_relation + ", marital_status=" + marital_status
                + ", spouse_details=" + spouse_details + ", permanent_address_same_as=" + permanent_address_same_as
                + ", residential_address_same_as=" + residential_address_same_as + ", communication_address_same_as=" + communication_address_same_as
                + ", birth_place=" + birth_place + ", permanent_address=" + permanent_address + ", residential_address=" + residential_address
                + ", communication_address=" + communication_address + ", phone1_type=" + phone1_type + ", phone1="
                + phone1 + ", phone2_type=" + phone2_type + ", phone2=" + phone2 + ", phone3_type=" + phone3_type + ", phone3=" + phone3
                + ", office_address=" + office_address + ", off_mail=" + off_mail + ", employer_web=" + employer_web + ", off_phone1=" + off_phone1
                + ", off_phone1_type=" + off_phone1_type + ", off_phone2=" + off_phone2 + ", off_phone2_type=" + off_phone2_type + ", off_phone3="
                + off_phone3 + ", off_phone3_type=" + off_phone3_type + ", resi_status=" + resi_status + ", residence_yrs_residing="
                + residence_yrs_residing + ", city_yrs_residing=" + city_yrs_residing + ", occupation=" + occupation + ", employedIncome="
                + employedIncome + ", selfEmployedIncome=" + selfEmployedIncome + "]";
    }


    public static class PermAddressIsTypeOther implements Condition {
        @Override
        public boolean shouldCascade(Object bean) {
            Applicant a = Applicant.class.cast(bean);
            return a.permanent_address_same_as != null && ADDRESS_TYPE_OTHER.equals(a.permanent_address_same_as);
        }
    }

    public static class ResiAddressIsTypeOther implements Condition {
        @Override
        public boolean shouldCascade(Object bean) {
            Applicant a = Applicant.class.cast(bean);
            return a.residential_address_same_as != null && ADDRESS_TYPE_OTHER.equals(a.residential_address_same_as);
        }
    }

    public static class CommAddressIsTypeOther implements Condition {
        @Override
        public boolean shouldCascade(Object bean) {
            Applicant a = Applicant.class.cast(bean);
            return a.communication_address_same_as != null && ADDRESS_TYPE_OTHER.equals(a.communication_address_same_as);
        }
    }
    
    public static class OccupationIsEmployed implements Condition {
        @Override
        public boolean shouldCascade(Object bean) {
            Applicant a = Applicant.class.cast(bean);
            return a.occupation != null && OCCUPATION_TYPE_EMPLOYED.equals(a.occupation);
        }
    }
    
    public static class OccupationIsSelfEmployed implements Condition {
        @Override
        public boolean shouldCascade(Object bean) {
            Applicant a = Applicant.class.cast(bean);
            return a.occupation != null && OCCUPATION_TYPE_SELF_EMPLOYED.equals(a.occupation);
        }
    }
    public static class AlwaysTrue implements Condition {
        @Override
        public boolean shouldCascade(Object bean) {
            Applicant a = Applicant.class.cast(bean);
            return true;
        }
    }
    
    public String getFullName(){
    	return fname+" "+mname+" "+lname;
    }
    
}
