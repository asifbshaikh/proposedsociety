package models;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

@Entity
public class Buero extends Model{
	
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)public Long id;

	public Buero() {
		// TODO Auto-generated constructor stub
	}
	public Buero(User user) {
		this.user =user;
	}
	
	@OneToMany(mappedBy="ib")
	public List<Agent> agent;
	
	@OneToOne
	public User user;
}
