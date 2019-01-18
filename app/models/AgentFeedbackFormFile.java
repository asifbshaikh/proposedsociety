package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

@Entity
public class AgentFeedbackFormFile extends Model {
	private static final long serialVersionUID = 1L;
	@Id public Long id;
	
	public Date uploadTime;

	@OneToOne 
	public FileAttachment feedbackFormFile;
}
