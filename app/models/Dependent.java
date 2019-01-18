package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class Dependent extends Model {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    public String dep_title;
    public String dep_relation;
    public String dep_fname;
    public String dep_mname;
    public String dep_lname;
    public Boolean isApplicant;

    @ManyToOne
    public ApplicationForm form;
}
