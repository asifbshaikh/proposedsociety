package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class SpouseDetails extends Model {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
     public String sp_title;
     public String sp_fname;
     public String sp_mname;
     public String sp_lname;
     public String sp_profession;
     public Date sp_dob;
     public Date anniversary;
     public Integer children;

}
