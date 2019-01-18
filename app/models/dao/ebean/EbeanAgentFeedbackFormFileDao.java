package models.dao.ebean;

import java.util.List;

import models.AgentFeedbackFormFile;
import models.dao.AgentFeedbackFormFileDao;
import play.db.ebean.Model.Finder;

public class EbeanAgentFeedbackFormFileDao extends
		AbstractEbeanDao<Long, AgentFeedbackFormFile> implements
		AgentFeedbackFormFileDao {

	public EbeanAgentFeedbackFormFileDao() {
		super(new Finder<Long, AgentFeedbackFormFile>(Long.class, AgentFeedbackFormFile.class));
	}

	@Override
	public AgentFeedbackFormFile findLatest() {
		List<AgentFeedbackFormFile> files = finder.order().desc("uploadTime").setMaxRows(1).findList();
		return files.isEmpty() ? null : files.get(0);
	}

}
