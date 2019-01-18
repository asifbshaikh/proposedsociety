package models.dao;

import java.util.List;
import com.avaje.ebean.Page;

import models.AgentAddress;
import models.Address;
import models.Agent;
import models.AgentAddress;
import models.AgentForm;
import models.AgentStatistics;
import models.Buero;
import models.User;

public interface AgentDao extends Dao<Long, Agent> {
	Agent findByUser(User user);
	List<Agent> findAll();
	AgentStatistics getAgentStatistics();
	Page<Agent> findPage(int pageSize,int pageNumber);
	Page<Agent> findPageForIb(int pageSize,int pageNumber,Buero ib);
	List<Agent> getIbsAgents(Buero ib);
	Page<Agent> findApprovedAgentPage(int pageSize,int pageNumber);
	Agent findByCode(String code);
	List<AgentForm> findAgentByOfficeAddress(String state, String district, String taluka);
	List<AgentForm> findAgentByCity(String city);
}

