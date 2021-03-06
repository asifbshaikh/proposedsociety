package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Nearby extends Model {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
    public Long id;
    public String locationNear;
    public String ifOtherText;

	/*	
		public Boolean railwayStation;
		public Boolean highway;
		public Boolean market;
		public Boolean garden;
		public Boolean playground;
		public Boolean school;
		public Boolean college;
		public Boolean hospital;
		public Boolean busDepot;
		public Boolean airport;
		public Boolean mentionOthers;
		public String textForMentionOthers;
	*/

}