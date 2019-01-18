package models;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class MiscBorrowing extends Model {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    public String type;
    public Integer amount;
    public Integer numberOfYears;
    public BigDecimal interestRate;
    public Integer emi;
    public Integer totalDeduction;
    
    @ManyToOne
    public ApplicationForm form;
}
