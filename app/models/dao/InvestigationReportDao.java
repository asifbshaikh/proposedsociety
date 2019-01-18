package models.dao;

import models.Agent;
import models.InvestigationReport;
import models.FileAttachment;
import models.Buero;

public interface InvestigationReportDao extends Dao<Long, InvestigationReport> {
	InvestigationReport getFeedbackForm(Buero ib_id , Agent agent_id);
}
