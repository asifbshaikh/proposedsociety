package models.dao.ebean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.avaje.ebean.Ebean;

import controllers.AdminUserController.ReportData;
import models.ApplicationForm;
import models.Requirement;
import models.RequirementAddress;
import models.User;
import models.dao.ApplicationFormDao;
import play.db.ebean.Model.Finder;

public class EbeanApplicationFormDao extends
		AbstractEbeanDao<Long, ApplicationForm> implements ApplicationFormDao {

	public EbeanApplicationFormDao() {
		super(new Finder<Long, ApplicationForm>(Long.class,
				ApplicationForm.class));
	}

	@Override
	public ApplicationForm findByUserEmail(String email) {
		return null;
	}

	@Override
	public User findUserByFormId(Long id) {
		User user = Ebean.find(User.class).where().eq("form_id", id)
				.findUnique();
		return user;
	}

	@Override
	public List<ApplicationForm> findAllFormByStatus(String status) {
		List<ApplicationForm> list = Ebean.find(ApplicationForm.class).where()
				.eq("status", status).findList();
		return list;
	}

	@Override
	public List<ApplicationForm> getPaidApplicant() {
		List<ApplicationForm> list = Ebean.find(ApplicationForm.class).where()
				.eq("status", "Paid").orderBy().desc("user.joiningDate")
				.findList();
		return list;
	}

	@Override
	public List<ApplicationForm> searchBySuggestedArea(String pincode,
			Long minBug, Long maxBug) {
		List<ApplicationForm> list = Ebean.find(ApplicationForm.class).where()
				.eq("requirements.requirementAddresses.pincode", pincode)
				.ge("user.form.calculatedBudget", minBug)
				.le("user.form.calculatedBudget", maxBug).findList();
		return list;
	}

	public List<ApplicationForm> findApplicationFormForReport(ReportData r) {
		List<Requirement> requirements = new ArrayList<Requirement>();
		List<RequirementAddress> adds = new ArrayList<RequirementAddress>();
		
		List<ApplicationForm> list = Ebean.find(ApplicationForm.class).where()
				.le("calculatedBudget", r.budget).findList();

		System.out.println("###########Found form: " + list.size());
		
		requirements = findRequirements(list,r);
		
		System.out.println("###########Forms requirements: " + requirements.size());
		
		adds = Ebean.find(RequirementAddress.class).where()
				.eq("pincode", r.pincode).eq("state", r.state)
				.eq("taluka", r.taluka).eq("district", r.district).findList();
		
		requirements = filterRequirementsByAddress(adds,requirements);
		list.clear();
		for(Requirement req: requirements){
			list.add(req.form);
		}
		
		System.out.println("###########Found form that mateches all criteria: " + list.size());
		
		return list;
	}

	private List<Requirement> filterRequirementsByAddress(List<RequirementAddress> adds, List<Requirement> requirements) {
		List<Requirement> rs = new ArrayList<Requirement>();
		for(RequirementAddress a: adds){
			rs.add(Ebean.find(Requirement.class).where().
					eq("id", a.requirement.id)
					.findUnique());
		}
		rs.retainAll(requirements);
		System.out.println("###########Requirement size that match add: " + rs.size());
		return rs;
	}

	private List<Requirement> findRequirements(List<ApplicationForm> list, ReportData r) {
		List<Requirement> rs = new ArrayList<Requirement>();
		for(ApplicationForm f : list){
			 rs.addAll(Ebean.find(Requirement.class).where()
				.eq("subType", r.type).eq("form_id", f.id)
				.eq("requirementSubType", "FlatOrTerraceFlat").findList());
		}
		return rs;
	}

	@Override
	public List<ApplicationForm> findAllPaidFormByStatus(String status) {

		String query = "find form fetch invoice where invoice.paid = true and form.status = :status";
		return Ebean.createQuery(ApplicationForm.class, query)
				.setParameter("status", status).findList();
	}

	@Override
	public List<ApplicationForm> findAllPaidForm() {

		String query = "find form fetch invoice where invoice.paid = true";
		return Ebean.createQuery(ApplicationForm.class, query).findList();

	}

	@Override
	public ApplicationForm findByInvoiceId(long id) {
		return finder.where().eq("invoice_id", String.valueOf(id)).findUnique();
	}
}
