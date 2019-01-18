package models.dao;

import java.util.List;

import com.avaje.ebean.Page;

import controllers.AdminUserController.ReportData;
import models.Agent;
import models.ApplicationForm;
import models.User;

public interface ApplicationFormDao extends Dao<Long, ApplicationForm> {
	ApplicationForm findByUserEmail(String email);
	List<ApplicationForm> findAllFormByStatus(String status);
	List<ApplicationForm> searchBySuggestedArea(String pincode,Long minBug,Long maxBug);
	List<ApplicationForm> getPaidApplicant();
	List<ApplicationForm> findAllPaidFormByStatus(String status);
	User findUserByFormId(Long id);
	List<ApplicationForm> findAllPaidForm();
	List<ApplicationForm> findApplicationFormForReport(ReportData r);
	ApplicationForm findByInvoiceId(long id);
}