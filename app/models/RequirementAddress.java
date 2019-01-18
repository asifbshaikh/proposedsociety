package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class RequirementAddress extends Model {
	
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
    public Long id;
	public String state;
	public String district;
	public String taluka;
	public String pincode;
    public String locality;
    public String street;
    @ManyToOne(cascade = {CascadeType.DETACH})
    public Requirement requirement;
}
