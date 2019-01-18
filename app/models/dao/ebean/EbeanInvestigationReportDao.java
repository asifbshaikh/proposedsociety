package models.dao.ebean;

import play.db.ebean.Model.Finder;
import models.Agent;
import models.InvestigationReport;
import models.Buero;
import models.dao.InvestigationReportDao;

public class EbeanInvestigationReportDao extends AbstractEbeanDao<Long, InvestigationReport> implements InvestigationReportDao{

	protected EbeanInvestigationReportDao() {
		super(new Finder<Long, InvestigationReport>(Long.class, InvestigationReport.class));
	}

	@Override
	public InvestigationReport getFeedbackForm(Buero buero,Agent agent) {
		return finder.where().eq("ib_id",buero.id ).eq("agent_id", agent.id).findUnique();
	}	
}
