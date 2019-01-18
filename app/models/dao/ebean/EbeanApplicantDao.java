package models.dao.ebean;

import models.Applicant;
import models.dao.ApplicantDao;
import play.db.ebean.Model.Finder;

public class EbeanApplicantDao extends AbstractEbeanDao<Long, Applicant> implements ApplicantDao {

    public EbeanApplicantDao() {
        super(new Finder<Long, Applicant>(Long.class, Applicant.class));
    }
}

