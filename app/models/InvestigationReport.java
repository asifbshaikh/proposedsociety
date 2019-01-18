package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.data.validation.Constraints;
import play.db.ebean.Model;


@Entity
@Table(name = "ib_feedback")
public class InvestigationReport extends Model{

	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
	
	@ManyToOne(cascade = CascadeType.DETACH)
	public Buero ib;
	
	@ManyToOne(cascade = CascadeType.DETACH)
	public Agent agent;
	
	
	
	@OneToOne(cascade = CascadeType.DETACH)
    public FileAttachment attachment;
	@Constraints.Required
	@Transient
    public String attachment_id;
	
}
