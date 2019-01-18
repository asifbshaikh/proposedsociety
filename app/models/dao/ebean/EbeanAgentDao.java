package models.dao.ebean;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.db.ebean.Model.Finder;
import views.html.agentList;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;

import common.Roles;
import controllers.FormStatus;
import models.Address;
import models.Agent;
import models.AgentAddress;
import models.AgentForm;
import models.AgentStatistics;
import models.Buero;
import models.User;
import models.dao.AgentDao;


public class EbeanAgentDao extends AbstractEbeanDao<Long, Agent> implements AgentDao {
	private static final Logger LOG = LoggerFactory.getLogger(Agent.class);
    public EbeanAgentDao() {
        super(new Finder<Long, Agent>(Long.class, Agent.class));
    }

   
    public Agent findByUser(User user) {
        String query = "find agent fetch user where user.id = :user_id";
        return Ebean.createQuery(Agent.class, query)
                .setParameter("user_id", user.id).findUnique();
    }

	public List<Agent> findAll() {
		
		return finder.all();
	}

	public AgentStatistics getAgentStatistics() {
		AgentStatistics agentStatistics = new AgentStatistics();
		List<Agent> list = finder.all();
		
		agentStatistics.totalApplication = list.size();
		agentStatistics.totalActiveAgent = getTotalActiveAgent(list);
		agentStatistics.totalApprovedApplication = getTotalApprovedAgent(list);
		agentStatistics.totalRejectedApplication = getTotalRejectedApplication(list);
		agentStatistics.totalPendingApplication = getTotalPendingApplication(list);
		
		return agentStatistics;
	}

	private int getTotalPendingApplication(List<Agent> list) {
		int count = 0;
		for(Agent agent : list){
			if(agent.form.status.equals( FormStatus.PENDING)){
				count++;
			}
		}
		return count;
	}

	private int getTotalRejectedApplication(List<Agent> list) {
		int count = 0;
		for(Agent agent : list){
			if(agent.form.status.equals(FormStatus.REJECTED)){
				count++;
			}
		}
		return count;
	}

	private int getTotalApprovedAgent(List<Agent> list) {
		int count = 0;
		for(Agent agent : list){
			if(agent.form.status.equals(FormStatus.APPROVED)){
				LOG.info(" " + agent.form.status);
				count++;
			}
		}
		return count;
	}

	private int getTotalActiveAgent(List<Agent> list) {
		int count = 0;
		for(Agent agent : list){
			if(agent.user.hasRole(Roles.AGENT)){
				count++;
			}
		}
		return count;
	}
	
	public List<Agent> getIbsAgents(Buero ib) {
        return finder.where().eq("ib_id", ib.id.toString()).findList();
	}

	@Override
	public Page<Agent> findPage(int pageSize, int pageNumber) {
		Page<Agent> agentList = finder.findPagingList(pageSize).getPage(pageNumber);
		if(agentList != null){
			return  agentList;
		}
		return null;
	}

	@Override
	public Page<Agent> findPageForIb(int pageSize, int pageNumber,Buero ib) {
		if(ib != null){
			Page<Agent> agentList =  finder.where().eq("ib_id", ib.id.toString()).findPagingList(pageSize).getPage(pageNumber);
			if(agentList != null){
				return agentList;
			}
		}
		return null;
	}
	
	@Override
	public Page<Agent> findApprovedAgentPage(int pageSize, int pageNumber) {
			Page<Agent> agentList =  finder.where().ne("form.status","Pending").findPagingList(pageSize).getPage(pageNumber);
			if(agentList != null){
				return agentList;
			}
		return null;
	}
	


	@Override
	public Agent findByCode(String code) {
		return finder.where().eq("agent_code", code).findUnique();
	}


	@Override
	public List<AgentForm> findAgentByOfficeAddress(String state, String district, String taluka) {
		List<AgentForm> agentlist  = new ArrayList<AgentForm>();
		List<AgentAddress> agentids = Ebean.find(AgentAddress.class).where().eq("taluka",taluka).eq("district",district).eq("state",state).findList();
		for(AgentAddress addr: agentids){
			if (Ebean.find(AgentForm.class).where().eq("office_address_id",addr.id).findUnique() != null)
			agentlist.add(Ebean.find(AgentForm.class).where().eq("office_address_id",addr.id).findUnique());
		}
		return agentlist;
	}
	
	@Override
	public List<AgentForm> findAgentByCity(String city) {
		List<AgentForm> agentlist  = new ArrayList<AgentForm>();
		List<AgentAddress> agentids = Ebean.find(AgentAddress.class).where().eq("city",city).findList();
		for(AgentAddress addr: agentids){
			if (Ebean.find(AgentForm.class).where().eq("office_address_id",addr.id).findUnique() != null)
			agentlist.add(Ebean.find(AgentForm.class).where().eq("office_address_id",addr.id).findUnique());
		}
		return agentlist;
	}
}
