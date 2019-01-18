package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;

@Entity
@Table(name = "short_requirement_address")
public class ShortRequirementAddress extends Model {
	
	private static final long serialVersionUID = 1L;
	
	

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
    public Long id;
	public String state;
	public String district;
	public String taluka;
	public String pincode;
	
	@Column(name = "village_or_detail_location")
    public String locality;


	public String street;

	@Override
	public String toString() {
		return "ShortRequirementAddress [id=" + id + ", state=" + state
				+ ", district=" + district + ", taluka=" + taluka
				+ ", pincode=" + pincode + ", locality=" + locality + "]";
	}
    
}