package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

@Entity
public class Agent extends Model {

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
	
	@OneToOne(cascade = CascadeType.ALL) public AgentForm form;
	
	@OneToOne
	public User user;
	
	@ManyToOne
    public Buero ib;
	
	public int totalAmountPaid ;
	public int formCredits;
	public int totalFormFilledCount;
	
	@Column(unique = true)
	public String agentCode;
	
	@OneToMany(cascade = CascadeType.ALL) public List<AgentCommission> commissions;
	
	
	public static Agent create(User user) {
		Agent agent = new Agent();
		agent.user = user;
		return agent;
	}
	
	public void manageAgentAVCcreditAndCommission(User user){
		user.agent.formCredits -= 1;
		user.agent.totalFormFilledCount += 1;
		
		//Adding agent commission
		user.agent.commissions.add(recordCommission(100,user.id));//Assuming 100 commission on each applicant form submission
	}
	
	public AgentCommission recordCommission(int value ,long commissionAgainstId){
		AgentCommission agentCommission = new AgentCommission();
		agentCommission.agent = this;
		agentCommission.commissionAmount = new BigDecimal(value); 
		agentCommission.description = "Towards Application Form Fees";
		agentCommission.commissionDate = new Date();
		agentCommission.commissionAgainstId = commissionAgainstId;
		return agentCommission;
	}
}