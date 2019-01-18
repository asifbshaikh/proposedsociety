package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import models.ApplicationForm.PostPayment;
import models.ApplicationForm.PrePayment;
@Entity
public class EmployedIncome extends Model {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!") public String employer;
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!") public String designation;
    
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!") 
    //@Constraints.Pattern(value = "[0-9]*", message = "please, insert numeric value.")
    public Integer gross_sal;
    
    //@Constraints.Pattern(value = "[0-9]*", message = "please, insert numeric value.")
    public Integer deductions;
    
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!") public Integer net_sal;
    //@Constraints.Pattern(value = "[0-9]*", message = "please, insert numeric value.")
    public Integer yrly_bonus;
    
   // @Constraints.Required(message = "Required Field!")
    public Date yrs_with_employer_d;
    
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!") 
    //@Constraints.Pattern(value = "[0-9]*", message = "please, insert numeric value.")
    public Integer experience_year;
    
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!") 
    //@Constraints.Pattern(value = "[0-9]*", message = "please, insert numeric value.")
    public Integer experience_month;
    
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!") 
    //@Constraints.Pattern(value = "[0-9]*", message = "please, insert numeric value.")
    public Integer yrs_service_remn;
}
