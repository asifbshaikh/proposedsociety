package models.dao;

import models.AgentFeedbackFormFile;

public interface AgentFeedbackFormFileDao extends Dao<Long, AgentFeedbackFormFile> {
	AgentFeedbackFormFile findLatest();
}
