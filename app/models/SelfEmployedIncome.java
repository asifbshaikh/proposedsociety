package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import models.ApplicationForm.PostPayment;
import models.ApplicationForm.PrePayment;

@Entity
public class SelfEmployedIncome extends Model {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!") public String business_type;
    
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!") 
    //@Constraints.Pattern(value = "[0-9]*", message = "please, insert numeric value.")
    public Integer yr1_pat;
    
    @Constraints.Required(groups = { PostPayment.class },message = "Required Field!") 
    //@Constraints.Pattern(value = "[0-9]*", message = "please, insert numeric value.")
    public Integer yr1_depr;
    
    //@Constraints.Pattern(value = "[0-9]*", message = "please, insert numeric value.")
    public Integer yr2_pat;
    
    //@Constraints.Pattern(value = "[0-9]*", message = "please, insert numeric value.")
    public Integer yr2_depr;
    
    //@Constraints.Pattern(value = "[0-9]*", message = "please, insert numeric value.")
    public Integer yr3_pat;
    
    //@Constraints.Pattern(value = "[0-9]*", message = "please, insert numeric value.")
    public Integer yr3_depr;
}
