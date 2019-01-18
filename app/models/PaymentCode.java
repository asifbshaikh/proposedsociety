package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class PaymentCode extends Model {
    private static final long serialVersionUID = 1L;

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    public Long id; 
    
    public String code;

    @Required
    public String name;
    
    @Required
    @Email
    public String email;

    @Required
    public String mobile;

    @Required
    public int amount;

    public Date generationTime;
    
    public Date invalidateTime;
    
    public boolean used;
    
    @OneToOne
    public ApplicationForm form;

    @Override
    public String toString() {
        return "PaymentCode [id=" + id + ", code=" + code + ", name=" + name + ", email=" + email + ", mobile="
                + mobile + ", amount=" + amount + ", generationTime=" + generationTime + ", invalidateTime="
                + invalidateTime + ", used=" + used + ", form=" + form + "]";
    }
}
