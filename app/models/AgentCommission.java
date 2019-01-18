package models;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;

@Entity
public class AgentCommission {
	
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
	public BigDecimal commissionAmount;
	public String description;
	public Date commissionDate ;
	public Long commissionAgainstId ;
	
	@ManyToOne(cascade = {CascadeType.DETACH})
    @Required
    public Agent agent;
		
}
