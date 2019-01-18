package controllers;

import static common.ApplicationConstants.SESSION_KEY_USER;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import json.BindingResult;
import json.JsonHelper;
import mail.Attachment;
import mail.HtmlMail;
import mail.Mailer;
import models.Address;
import models.Agent;
import models.AgentCommission;
import models.Applicant;
import models.ApplicationForm;
import models.ApplicationForm.PostPayment;
import models.ApplicationForm.PrePayment;
import models.BankAccount;
import models.BudgetValues;
import models.Dependent;
import models.ExternalAmenity;
import models.FileAttachment;
import models.FixedDeposit;
import models.Insurance;
import models.InternalAmenity;
import models.Invoice;
import models.LoanDetails;
import models.MiscBorrowing;
import models.Nearby;
import models.Payment;
import models.PaymentInfo;
import models.PaymentReceipt;
import models.Property;
import models.RecurringDeposit;
import models.Requirement;
import models.Requirement.Chawl;
import models.Requirement.Flat;
import models.Requirement.Hotel;
import models.Requirement.Industry;
import models.Requirement.Office;
import models.Requirement.Other;
import models.Requirement.Row;
import models.Requirement.RowPlot;
import models.Requirement.Shop;
import models.Requirement.Studio;
import models.RequirementAddress;
import models.Role;
import models.Summary;
import models.User;
import models.dao.AgentDao;
import models.dao.ApplicantDao;
import models.dao.ApplicationFormDao;
import models.dao.DaoProvider;
import models.dao.DependentDao;
import models.dao.FileAttachmentDao;
import models.dao.InvoiceDao;
import models.dao.LoanDetailsDao;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.TypeReference;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.Constraints.Pattern;
import play.data.validation.Constraints.Required;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import security.auth.AgentAuthenticator;
import security.auth.VisitorAuthenticator;
import utils.IdGenerator;
import utils.IdGenerator.Prefix;
import utils.IdManager;
import utils.IdManager.IdType;
import utils.Mail;
import utils.Notifier;
import utils.PaymentGateway;
import utils.PaymentGatewayError;
import utils.SmsGateway;
import utils.SmsGatewayException;
import utils.SmsTemplate;
import utils.TransactionIdGenerator;
import validation.PSForm;
import views.html.closingFinish;
import views.html.closingFinishDetails;
import views.html.dependents;
import views.html.getLoan;
import views.html.getRequirements;
import views.html.payment;
import views.html.paymentInfo;
import views.html.renderSubReq;
import views.html.shortForm;
import views.html.successMessage;
import views.html.summaryPage;
import views.html.wantToAddCoApplicant;
import views.html.wholePersonalInfo;
import birt.ReportEngine;
import birt.ReportEngineException;

import com.avaje.ebean.Ebean;
import com.google.inject.Inject;

import common.PSError;
import common.RestUtils;
import common.Roles;
import controllers.Application.VisitorLogin;
//import org.specs2.matcher.MatchResultMessages.SuccessMessage;
@Authenticated(value = VisitorAuthenticator.class)
public class ApplicationFormController extends AuthenticatedUserController {
    
    private static final String BIRTH_PLACE = "birth_place";
    private static final String PERMANENT_ADDRESS = "permanent_address";
    private static final String RESIDENTIAL_ADDRESS = "residential_address";
    private static final String OCCUPATION_SELF_EMPLOYED = "S";
    private static final String OCCUPATION_EMPLOYED = "E";
    private static final int FORM_FEES = 500;
	private final int AGENT_COMMISSION = 100 ;
    private static final String TRUEVAL = "true";
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationFormController.class);
    
    private final PaymentGateway paymentGateway;
    private final ApplicantDao applicantDao;
    private final ApplicationFormDao applicationFormDao;
    private final FileAttachmentDao attachmentDao;
    
    private final DependentDao dependentDao;
    private final SmsGateway smsGateway;
    private final LoanDetailsDao loanDetailsDao;
    private final InvoiceDao invoiceDao;
    private final AgentDao agentDao;
    private final Mailer mailer;
    private final TransactionIdGenerator idgen;
    private final ReportEngine reportEngine;
    private final IdGenerator idGenerator;
    private final IdManager idManager;
    private final Map<String, Class<?>> propertyTypeToGroup = new HashMap<String, Class<?>>();

    @Inject
    public ApplicationFormController(DaoProvider provider, PaymentGateway paymentGateway,SmsGateway smsGateway, Mailer mailer,ReportEngine reportEngine,TransactionIdGenerator idgen, IdGenerator idGenerator, IdManager idManager) {
    	super(provider.userDao());
    	this.paymentGateway = paymentGateway;
    	this.attachmentDao = provider.fileAttachmentDao();

    	this.dependentDao = provider.dependentDao();
    	this.loanDetailsDao = provider.loanDetailsDao();

    	this.applicantDao = provider.applicantDao();
    	this.applicationFormDao = provider.applicationFormDao();
    	this.agentDao 	= provider.agentDao();
    	this.smsGateway = smsGateway ;
    	this.mailer = mailer;
    	this.reportEngine = reportEngine;
        this.idgen = idgen;
    	this.invoiceDao = provider.invoiceDao();
    	this.idGenerator = idGenerator ;
    	this.idManager = idManager ;
    	
    	propertyTypeToGroup.put("ChawlRoom", Chawl.class);
	   	
	   	propertyTypeToGroup.put("FlatOrTerraceFlat", Flat.class);
	   	propertyTypeToGroup.put("DuplexFlat", Flat.class);
	   	propertyTypeToGroup.put("SecondHome", Flat.class);
	   	
	   	propertyTypeToGroup.put("RowHouse", Row.class);
	   	propertyTypeToGroup.put("IndependentBungalow", Row.class);
	   	propertyTypeToGroup.put("FarmHouse", Row.class);
	   	propertyTypeToGroup.put("JointBanglowOrTwinBungalow", Row.class);
	   	propertyTypeToGroup.put("HolidayHome", Row.class);
	   	propertyTypeToGroup.put("Villa", Row.class);
	   	propertyTypeToGroup.put("PentHouse", Row.class);
	   	propertyTypeToGroup.put("GuestHouse", Row.class);
	   	
	   	propertyTypeToGroup.put("RowHousePlot", RowPlot.class);
	   	propertyTypeToGroup.put("IndependentBungalowPlot", RowPlot.class);
	   	propertyTypeToGroup.put("JointBungalowPlot", RowPlot.class);
	   	propertyTypeToGroup.put("FarmHousePlot", RowPlot.class);
	   	propertyTypeToGroup.put("CommercialLand", RowPlot.class);
	   	propertyTypeToGroup.put("BusinessCenterOrBusinessPark", RowPlot.class);
	   	propertyTypeToGroup.put("AgriculturalLand", RowPlot.class);
	   	propertyTypeToGroup.put("FarmLand", RowPlot.class);
	   	
	   	propertyTypeToGroup.put("StudioApartment", Studio.class);
	   	propertyTypeToGroup.put("HighriseApartment", Studio.class);
	   	propertyTypeToGroup.put("ServiceApartment", Studio.class);
	   	
	   	propertyTypeToGroup.put("Shop", Shop.class);
	   	
	   	propertyTypeToGroup.put("OfficeInITParkOrSez", Office.class);
	   	propertyTypeToGroup.put("Warehouse", Office.class);
	   	propertyTypeToGroup.put("CommercialOfficeOrSpace", Office.class);
	   	propertyTypeToGroup.put("ConstructedHotelSpace", Office.class);
	   	propertyTypeToGroup.put("BenquetHall", Office.class);
	   	propertyTypeToGroup.put("IndustrialBuilding", Office.class);
	   	
	   	propertyTypeToGroup.put("HotelPlot", Hotel.class);
	   	propertyTypeToGroup.put("IndustrialLand", Hotel.class);
	   	propertyTypeToGroup.put("HotelSpace", Hotel.class);
	   	propertyTypeToGroup.put("HospitalSpace", Hotel.class);
	   	propertyTypeToGroup.put("SchoolSpace", Hotel.class);
	   	propertyTypeToGroup.put("BankSpace", Hotel.class);
	   	
	   	propertyTypeToGroup.put("IndustrialShed", Industry.class);
	   	propertyTypeToGroup.put("SpaceInRetailMall", Industry.class);
	   	propertyTypeToGroup.put("Godown", Industry.class);
	   	propertyTypeToGroup.put("ColdStorage", Industry.class);
	   	propertyTypeToGroup.put("ManufacturingUnit", Industry.class);
	   	propertyTypeToGroup.put("Factory", Industry.class);
	   	propertyTypeToGroup.put("CommercialShowroom", Industry.class);
	   	propertyTypeToGroup.put("Kiosk", Industry.class);
	   	propertyTypeToGroup.put("Other", Other.class);

    }
    
    /*public static final class LoanAndDependents {
    	@Required(message = "Required Field.") public String amount_per_month_from_sal;
        @Required(message = "Required Field.") public String loan_shown_on_bs;
    }*/
    
    public static class DependentDetails {
        public String id;
        @Required(message = "title is required")public String applicant_dep_title;
        @Required(message = "First name is required") public String applicant_dep_fname;
        @Required(message = "Middle name is required") public String applicant_dep_mname;
        @Required(message = "Last name is required") public String applicant_dep_lname;
        @Required(message = "Relation is required") public String applicant_dep_relation;
        
        public void populate(Dependent dpt) {
			dpt.dep_title = applicant_dep_title;
			dpt.dep_fname = applicant_dep_fname;
			dpt.dep_mname = applicant_dep_mname;
			dpt.dep_lname = applicant_dep_lname;
			dpt.dep_relation = applicant_dep_relation;
			dpt.isApplicant = true;
        }
    }
    
    public static class CoAppDependentDetails {
        public String id;
        @Required(message = "title is required")public String co_applicant_dep_title;
        @Required(message = "First name is required") public String co_applicant_dep_fname;
        @Required(message = "Middle name is required") public String co_applicant_dep_mname;
        @Required(message = "Last name is required") public String co_applicant_dep_lname;
        @Required(message = "Relation is required") public String co_applicant_dep_relation;
        
        public void populate(Dependent dpt) {
			dpt.dep_title = co_applicant_dep_title;
			dpt.dep_fname = co_applicant_dep_fname;
			dpt.dep_mname = co_applicant_dep_mname;
			dpt.dep_lname = co_applicant_dep_lname;
			dpt.dep_relation = co_applicant_dep_relation;
			dpt.isApplicant = false;
        }
    }
    
    public static class WantToAddCoAppliacnt {
    	@Required(message = "Required Field!") public String coApplicantExists;
    	
    	public void populate(ApplicationForm fm) {
            	if(coApplicantExists != null){fm.coApplicantExists = Boolean.valueOf(coApplicantExists);}
    	}
    }
   
  
    public static final class LoanFormDetails {
        //Loan Details
    	
    	@Required(message = "Required Field.") public String applicant_amountPerMonthFromSal;
    	
    	@Required(message = "Required Field!")
        public String applicant_financer;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String applicant_loanAmount;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String applicant_monthlyEmi;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String applicant_installmentsPaid;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String applicant_installmentsBalance;
      
        public void populate(LoanDetails il) {
            try
            {	
            	if(applicant_amountPerMonthFromSal != null){il.amountPerMonthFromSal = Boolean.valueOf(applicant_amountPerMonthFromSal);}
            	il.financer = applicant_financer;
            	if(!applicant_loanAmount.isEmpty()){il.loanAmount = Integer.parseInt(applicant_loanAmount);}
            	if(!applicant_monthlyEmi.isEmpty()){il.monthlyEmi = Integer.parseInt(applicant_monthlyEmi);}
            	if(!applicant_installmentsPaid.isEmpty()){il.installmentsPaid = Integer.parseInt(applicant_installmentsPaid);}
            	if(!applicant_installmentsBalance.isEmpty()){il.installmentsBalance = Integer.parseInt(applicant_installmentsBalance);}
            	il.isApplicant = true;
            }
            catch (NumberFormatException nfex)
            {
               LOG.info("exception: "+nfex);
            }
        }

    }
    
    public static final class CoAppLoanFormDetails {
        //Loan Details
    	
    	@Required(message = "Required Field.") public String co_applicant_amountPerMonthFromSal;
    	
    	@Required(message = "Required Field!")
        public String co_applicant_financer;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String co_applicant_loanAmount;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String co_applicant_monthlyEmi;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String co_applicant_installmentsPaid;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String co_applicant_installmentsBalance;
      
        public void populate(LoanDetails il) {
            try
            {	
            	if(co_applicant_amountPerMonthFromSal != null){il.amountPerMonthFromSal = Boolean.valueOf(co_applicant_amountPerMonthFromSal);}
            	il.financer = co_applicant_financer;
            	if(!co_applicant_loanAmount.isEmpty()){il.loanAmount = Integer.parseInt(co_applicant_loanAmount);}
            	if(!co_applicant_monthlyEmi.isEmpty()){il.monthlyEmi = Integer.parseInt(co_applicant_monthlyEmi);}
            	if(!co_applicant_installmentsPaid.isEmpty()){il.installmentsPaid = Integer.parseInt(co_applicant_installmentsPaid);}
            	if(!co_applicant_installmentsBalance.isEmpty()){il.installmentsBalance = Integer.parseInt(co_applicant_installmentsBalance);}
            	il.isApplicant = false;
            }
            catch (NumberFormatException nfex)
            {
               LOG.info("exception: "+nfex);
            }
        }

    }

    public static final class AmountFromFriends {

        @Required(message = "Required Field!")
        public String isAmountFromFriends;
        
        @Required(message = "Required Field!")
        public String amountNature;

        public void populate(MiscBorrowing mb) {
            try
            {
                mb.type = amountNature;
            }
            catch (NumberFormatException nfex)
            {
               LOG.info("exception: "+nfex);
            }
        }
    }

    public static final class AmountFromFriendsAsLoan {

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String amount;

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String numberOfYears;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String interestRate;

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String emi;

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String totalDeduction;    

        public void populate(MiscBorrowing mb) {
            try
            {
               mb.amount = Integer.parseInt(amount);
               mb.numberOfYears = Integer.parseInt(numberOfYears);
               mb.interestRate = new BigDecimal(interestRate);
               mb.emi = Integer.parseInt(emi);
               mb.totalDeduction = Integer.parseInt(totalDeduction);
            }
            catch (NumberFormatException nfex)
            {
               LOG.info("exception: "+nfex);
            }
        }
    }

    public static final class AmountFromFriendsAsDonation {

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String donation;    

        public void populate(MiscBorrowing mb) {
            try
            {
               mb.amount = Integer.parseInt(donation);
            }
            catch (NumberFormatException nfex)
            {
               LOG.info("exception: "+nfex);
            }
        }
    }

    public static final class AmountFromFriendsAsRepayment {

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String repayment;    

        public void populate(MiscBorrowing mb) {
            try
            {
               mb.amount = Integer.parseInt(repayment);
            }
            catch (NumberFormatException nfex)
            {
               LOG.info("exception: "+nfex);
            }
        }
    }

    public static class DependentAndLoanForm {
    	@Required(message = "Required Field!") public Boolean hasDependentOnApplicant;
    	@Required(message = "Required Field!") public Boolean hasDependentOnCoApplicant;
    	@Required(message = "Required Field!") public Boolean hasLoanOnApplicant;
    	@Required(message = "Required Field!") public Boolean hasLoanOnCoApplicant;
    }

    public Result shortForm(){
    	Form<ApplicationForm> formForm = PSForm.form(ApplicationForm.class);
    	Form<Requirement> req = Form.form(Requirement.class);
    	return ok(shortForm.render(formForm,req,"applicant",""));
    }

    public Result shortFormSubmit(){
    	DynamicForm req_dynmc_form = Form.form().bindFromRequest();
    	String requirementType = req_dynmc_form.get("requirementSubType");
    	LOG.info("requirement_type: "+requirementType);
    	
    	Class<?> group = propertyTypeToGroup.get(requirementType);
    	
    	Form<ApplicationForm> formForm = PSForm.form(ApplicationForm.class).bindFromRequest();
    	Form<Requirement> req = Form.form(Requirement.class,group).bindFromRequest();
    	
    	if(formForm.hasErrors() || req.hasErrors()){
    		LOG.info("Form has errors"+formForm.errors()+req.errors());
    		return badRequest(shortForm.render(formForm,req,"applicant",PSError.VALIDATION_ERRORS));
    	}
    	
    	ApplicationForm applicationForm =formForm.get(); 
    	
    	User user = userDao.findUniqueByEmail(applicationForm.applicant.email); //Unique email with applicantPassword is set
		if(user != null && user.applicantPassword != null){
			return badRequest(shortForm.render(formForm,req,"applicant",PSError.USER_EXIST));
		}
		
		user = userDao.findUniqueByMobile(applicationForm.applicant.phone1); //Unique mobileNumber with applicantPassword is set
		if(user != null && user.applicantPassword != null){
			return badRequest(shortForm.render(formForm,req,"applicant",PSError.USER_EXIST));
		}
    	
		applicationForm.requirements = new ArrayList<Requirement>();
		
        String internalAmntsToString = req_dynmc_form.get("internalAmenitiesHidden");
		String externalAmntsToString = req_dynmc_form.get("externalAmenitiesHidden");
		String nearbyToString = req_dynmc_form.get("nearbyHidden");
		
		String ifOtherTextForIntAmnts = req_dynmc_form.get("ifOtherTextForIntAmnts");
		String ifOtherTextForExtAmnts = req_dynmc_form.get("ifOtherTextForExtAmnts");
		String ifOtherTextForNearBy = req_dynmc_form.get("ifOtherTextForNearBy");
		
		Set<String> internalAmntsSet = new HashSet<String>();
		String[] internalAmntsArr = internalAmntsToString.split("\\|");
		for(String s:internalAmntsArr) {
			internalAmntsSet.add(s);
		}
		
		Set<String> externalAmntsSet = new HashSet<String>();
		String[] externalAmntsArr = externalAmntsToString.split("\\|");
		for(String s:externalAmntsArr) {
			externalAmntsSet.add(s);
		}
		
		Set<String> nearbySet = new HashSet<String>();
	    String[] nearbyArr = nearbyToString.split("\\|");
	    for(String s:nearbyArr) {
	    	nearbySet.add(s);
	    }

	    Requirement requirement = req.get();

	    for(String intAmnts : internalAmntsSet) {
			InternalAmenity intrnlAmntObj = new InternalAmenity();
			intrnlAmntObj.internalAmenityName = intAmnts;
			if(!intAmnts.equals("other")){
    	    	intrnlAmntObj.ifOtherText = "";
    	    	if(intAmnts != null && !intAmnts.isEmpty()){
    	    		requirement.internalAmenities.add(intrnlAmntObj);
    	    	}
    	    }else{
				intrnlAmntObj.ifOtherText = ifOtherTextForIntAmnts;
				requirement.internalAmenities.add(intrnlAmntObj);
			}
		}
		
		for(String extAmnts : externalAmntsSet) {
			ExternalAmenity extrnlAmntObj = new ExternalAmenity();
	    	extrnlAmntObj.externalAmenityName = extAmnts;
			if(!extAmnts.equals("other")){
    	    	extrnlAmntObj.ifOtherText = "";
    	    	if(extAmnts != null && !extAmnts.isEmpty()){
    	    		requirement.externalAmenities.add(extrnlAmntObj);
    	    	}
    	    }else{
				extrnlAmntObj.ifOtherText = ifOtherTextForExtAmnts;
				requirement.externalAmenities.add(extrnlAmntObj);
			}
		}
		
		for(String nrBy : nearbySet) {
			Nearby nearbyObj = new Nearby();
			nearbyObj.locationNear = nrBy;
			if(!nrBy.equals("other")){
      			nearbyObj.ifOtherText = "";
      			if(nrBy != null && !nrBy.isEmpty()){
      				requirement.nearbys.add(nearbyObj);
      			}
          	}else{
				nearbyObj.ifOtherText = ifOtherTextForNearBy;
				requirement.nearbys.add(nearbyObj);
			}
		}
        String json = req_dynmc_form.get("locationsHiddenField");
        ObjectMapper mapper = new ObjectMapper();
        try{
               JsonNode node = mapper.readTree(json);
               //node = node.get("location");
               TypeReference<List<RequirementAddress>> typeRef = new TypeReference<List<RequirementAddress>>(){};
               List<RequirementAddress> listOfReqAdd = mapper.readValue(node.traverse(), typeRef);
               
               for (RequirementAddress radss : listOfReqAdd) {
                       if(radss.pincode.length() > 6){    //If Pincode followed with locality is set to pincode id then this if condition work
                               LOG.info("Pincode is Greater than 6 length");
                               String pin =radss.pincode;
                               radss.pincode = pin.substring(0,6);
                               radss.locality =pin.substring(7);
         
                       }
                       
                       if(LOG.isDebugEnabled()) {
                    	   LOG.debug("Found State: " + radss.state + " District: " + radss.district + " Taluka: " + radss.taluka + " Pin: " + radss.pincode);
                       }
              }
               requirement.requirementAddresses.addAll(listOfReqAdd);
        }catch (JsonProcessingException e) {
                       LOG.warn("Json exception", e);
               }catch (IOException e) {
                       LOG.warn("IO exception",e);
               }
		
		 
		
        applicationForm.requirements.add(requirement);
        
        Invoice invoice = new Invoice();
        invoice.amount = new BigDecimal(FORM_FEES);
        invoice.description = "Proposed Society Form Fee";
        invoice.invoiceNumber = "PSINV-" + System.currentTimeMillis();
        invoice.paid = false;
        LOG.info("creating invoice of amount for form : " + invoice.amount);
        applicationForm.invoice = invoice;
        applicationForm.status = FormStatus.INCOMPLETE;
    
    
        if(!utils.StringUtils.isTrivial(applicationForm.agentCode)){
        	Agent agent = agentDao.findByCode(applicationForm.agentCode);
        	
            if(agent == null){
            	return badRequest(shortForm.render(formForm,req,"applicant",PSError.AGENT_NOT_EXIST));
            }else{
            	applicationForm.agent = agent;
            	 Ebean.save(applicationForm);
            	return ok(views.html.shortFormForms.getPaymentMode.render(applicationForm));
            }
        }
        
        Ebean.save(applicationForm);
        return ok(views.html.shortFormForms.payment.render(applicationForm));
    }
  
    
    
    public Result wholePersonalInfo() {
    	Form<ApplicationForm> formForm = PSForm.form(ApplicationForm.class);
    	Form<DependentAndLoanForm> dependentAndLoanForm = Form.form(DependentAndLoanForm.class);
    	Form<DependentDetails> dependentDetailsForm = Form.form(DependentDetails.class);
    	Form<LoanFormDetails> LoanFormDetailsForm = Form.form(LoanFormDetails.class);
        Boolean payment_status  = false;
    	User user = currentUser();
    	
        if(user.form == null) {
            user.form = new ApplicationForm();
            user.form.status = FormStatus.PENDING;
            userDao.save(user);
        }
    	
    	if (user.form != null) {
    		formForm = formForm.fill(user.form);
    	}
    	if(user.form.invoice != null && user.form.invoice.paid){
    		payment_status = true;
    	}
       	return ok(wholePersonalInfo.render(formForm, dependentAndLoanForm, dependentDetailsForm, LoanFormDetailsForm,
       	        user.form.getApplicantDependents(),
       	        user.form.getCoApplicantDependents(),
       	        user.form.getApplicantLoanDetails(),
       	        user.form.getCoApplicantLoanDetails(),payment_status));
    }
    
    public Result wholePersonalInfoSubmit(){
    	// Should never be null
    	User user = currentUser();
    	Form<ApplicationForm> applicationFormForm ;
    	
    	if(user.form != null){
    		if(user.form.invoice != null && user.form.invoice.paid){
    			 applicationFormForm = PSForm.form(ApplicationForm.class,PostPayment.class).bindFromRequest();
    		}else{
    			applicationFormForm = PSForm.form(ApplicationForm.class,PrePayment.class).bindFromRequest();
    		}
    	}else{
    		applicationFormForm = PSForm.form(ApplicationForm.class,PrePayment.class).bindFromRequest();
    	}
    	Form<DependentAndLoanForm> data = Form.form(DependentAndLoanForm.class).bindFromRequest();
    	Form<DependentDetails> dd = Form.form(DependentDetails.class);
    	Form<LoanFormDetails> lfd = Form.form(LoanFormDetails.class);
    	
        if (applicationFormForm.hasErrors() || data.hasErrors()) 
        {
        	if(applicationFormForm.hasErrors()){
        		
        		LOG.debug("Application Form Errors: " + applicationFormForm.errors());
        }
        	if (LOG.isDebugEnabled()) {
        	    LOG.debug("some debug text");
        	}
        	boolean payment_status = false ;
        	if(user.form.invoice != null && user.form.invoice.paid){
        		payment_status = true ;
        	}
            return badRequest(wholePersonalInfo.render(applicationFormForm,data,dd,lfd,user.form.getApplicantDependents(),user.form.getCoApplicantDependents(),user.form.getApplicantLoanDetails(),user.form.getCoApplicantLoanDetails(),payment_status));
        }
        
        ApplicationForm form = applicationFormForm.get();
        Applicant applicant = form.applicant;

        if(user.form == null) {
            user.form = new ApplicationForm();
            user.form.status = FormStatus.PENDING;
        }
        
        if (user.form.applicant != null) {
            // Delink applicant from Form and save.
            Applicant old = user.form.applicant;
            user.form.applicant = null;
            applicant.title = old.title ;
            applicant.fname = old.fname ;
            applicant.mname = old.mname ;
            applicant.lname = old.lname ;
            applicant.dob = old.dob ;
            
            /*applicant.residential_address_same_as = old.residential_address_same_as ;
            applicant.residential_address = new Address(old.residential_address );*/
           
            applicationFormDao.save(user.form);
            
            // Delete applicant;
            delete(old);
        }
        
        user.form.applicant = prepareForSave(applicant);

        if(form.coApplicantExists != null) {
        	Applicant coApplicant = form.co_applicant;
	        if (user.form.co_applicant != null) {
	            // Delink applicant from Form and save.
	            Applicant old = user.form.co_applicant;
	            user.form.coApplicantExists = false;
	            user.form.co_applicant = null;
	            applicationFormDao.save(user.form);

	            // Delete co-applicant
	            delete(old);
	        } 
	        if(form.coApplicantExists){
	        	user.form.co_applicant = prepareForSave(coApplicant);
	        }
            user.form.coApplicantExists = form.coApplicantExists;
        }
        
        userDao.save(user);

        return redirect(routes.OwnContributionController.entireOwnContribution());
    }
    
    private void delete(Applicant a) {
        prepareForDelete(a);
        applicantDao.delete(a);
    }

    private void prepareForDelete(Applicant a) {
        // Ensure you visit all the OneToOne and ManyToMany properties to ensure those are loaded prior to deletion.
        load(a.spouse_details);
        load(a.birth_place);
        load(a.permanent_address);
        load(a.residential_address);
        load(a.communication_address);
        load(a.office_address);
        
        load(a.employedIncome);
        load(a.selfEmployedIncome);
    }

    /**
     * This method does not do anything. You can call this method by simply
     * accessing a property of a bean managed by Ebean to ensure that if its
     * lazy loaded, it will be loaded in memory. We are essentially using this
     * for delete as cascading does not work on properties which are lazy loaded
     * and are null at the time of delete.
     * 
     * @param o
     */
    private void load(Object o) {
        
    }

    public Applicant prepareForSave(Applicant applicant) {
    	User user = currentUser();
        if (applicant.photograph_id != null ) {
            FileAttachment file = attachmentDao.findByFilePath(applicant.photograph_id,user);
            if (file != null) {
                applicant.photograph = file;
            }
        }

        if (applicant.identity1_id != null ) {
            FileAttachment file = attachmentDao.findByFilePath(applicant.identity1_id,user);
            if (file != null) {
                applicant.identity1 = file;
            }
        }

        if (applicant.identity2_id != null ) {
            FileAttachment file3 = attachmentDao.findByFilePath(applicant.identity2_id,user);
            if (file3 != null) {
                applicant.identity2 = file3;
            }
        }
        if (applicant.identity3_id != null ) {
            FileAttachment file4 = attachmentDao.findByFilePath(applicant.identity3_id,user);
            if (file4 != null) {
                applicant.identity3 = file4;
            }
        }
        if (applicant.addressproof_id != null ) {
            FileAttachment file5 = attachmentDao.findByFilePath(applicant.addressproof_id,user);
            if (file5 != null) {
                applicant.addressproof = file5;
            }
        }

        // Erase spouse details if unmarried
        if (applicant.marital_status != 'M') {
            applicant.spouse_details = null;
        }
        
        if (applicant.permanent_address_same_as.equals(BIRTH_PLACE)) {
            applicant.permanent_address = new Address(applicant.birth_place);
        }
        
        if (applicant.residential_address_same_as.equals(BIRTH_PLACE)) {
            applicant.residential_address = new Address(applicant.birth_place);
        }else if (applicant.residential_address_same_as.equals(PERMANENT_ADDRESS)) {
            applicant.residential_address = new Address(applicant.permanent_address);
        }
        
        if (applicant.communication_address_same_as.equals(BIRTH_PLACE)) {
            applicant.communication_address = new Address(applicant.birth_place);
        }else if (applicant.communication_address_same_as.equals(PERMANENT_ADDRESS)) {
            applicant.communication_address = new Address(applicant.permanent_address);
        }else if (applicant.communication_address_same_as.equals(RESIDENTIAL_ADDRESS)) {
            applicant.communication_address = new Address(applicant.residential_address);
        }

        // Erase Self-Employed / Employed Income if opposite is selected.
        if (applicant.occupation.equals(OCCUPATION_EMPLOYED)) {
            applicant.selfEmployedIncome = null;
        }
        else if (applicant.occupation.equals(OCCUPATION_SELF_EMPLOYED)) {
            applicant.employedIncome = null;
        }
        
        return applicant;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result addLoan() {
        JsonNode json = request().body().asJson();
        LoanFormDetails loanBean = Json.fromJson(json, LoanFormDetails.class);
        javax.validation.Validator validator = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<LoanFormDetails>> violations = validator.validate(loanBean);
        LOG.info("Errors: " + violations);

        if (!violations.isEmpty()) {
            return badRequest(RestUtils.createFailureResponse(violations));
        }

        String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        if(user != null){
            if(user.form == null) {
                user.form = new ApplicationForm();
                user.form.loanDetailsList = new ArrayList<LoanDetails>();
            } 
            else if (user.form.loanDetailsList == null) {
                user.form.loanDetailsList = new ArrayList<LoanDetails>();
            }
            LoanDetails loanD = new LoanDetails();
            loanBean.populate(loanD);
            user.form.loanDetailsList.add(loanD); 

            Ebean.save(user.form);
            Ebean.save(user);

            return ok("Saved Json");
        }        
        
        return ok(RestUtils.createFailureResponse());
    }   

    public Result getLoan() {
        try{
            String email = session(SESSION_KEY_USER);
            User user = userDao.findUniqueByEmail(email);
            if(user != null && user.form != null && user.form.loanDetailsList != null){
                return ok(getLoan.render(user.form.getApplicantLoanDetails(),true));
            }
        }
        catch (NullPointerException e) {
            LOG.info("Caught NullPointerException: " + e.getMessage());
        }
        return ok("something went wrong!!!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result addDep() {
        JsonNode json = request().body().asJson();
        DependentDetails depBean = Json.fromJson(json, DependentDetails.class);
        javax.validation.Validator validator = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<DependentDetails>> violations = validator.validate(depBean);
        LOG.info("Errors: " + violations);

        if (!violations.isEmpty()) {
            return badRequest(RestUtils.createFailureResponse(violations));
        }

        String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        if(user != null){
            if(user.form == null) {
                user.form = new ApplicationForm();
                user.form.dependents = new ArrayList<Dependent>();
            }
            else if (user.form.dependents == null) {
                user.form.dependents = new ArrayList<Dependent>();
            }
            
            Dependent Dpt = new Dependent();
            depBean.populate(Dpt);
            user.form.dependents.add(Dpt); 

            Ebean.save(user.form);
            Ebean.save(user);

            return ok("Saved Json");
        }        
        
        return ok(RestUtils.createFailureResponse());
    }   

    public Result dependents() {
        try{
            String email = session(SESSION_KEY_USER);
            User user = userDao.findUniqueByEmail(email);
            if(user != null && user.form != null && user.form.dependents != null){
                return ok(dependents.render(user.form.getApplicantDependents(),true));
            }
        }
        catch (NullPointerException e) {
        	LOG.info("Caught NullPointerException: " + e.getMessage());
        }
        return ok("something went wrong!!!");
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public Result addCoApplicantsLoan() {
        JsonNode json = request().body().asJson();
        CoAppLoanFormDetails loanBean = Json.fromJson(json, CoAppLoanFormDetails.class);
        javax.validation.Validator validator = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<CoAppLoanFormDetails>> violations = validator.validate(loanBean);
        LOG.info("Errors: " + violations);

        if (!violations.isEmpty()) {
            return badRequest(RestUtils.createFailureResponse(violations));
        }

        String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        if(user != null){
            if(user.form == null) {
                user.form = new ApplicationForm();
                user.form.loanDetailsList = new ArrayList<LoanDetails>();
            } 
            else if (user.form.loanDetailsList == null) {
            	user.form.loanDetailsList = new ArrayList<LoanDetails>();
            }
            LoanDetails loanD = new LoanDetails();
            loanBean.populate(loanD);
            user.form.loanDetailsList.add(loanD); 

            Ebean.save(user.form);
            Ebean.save(user);

            return ok("Saved Json");
        }        
        
        return ok(RestUtils.createFailureResponse());
    }   

    public Result getCoApplicantsLoan() {
        try{
            String email = session(SESSION_KEY_USER);
            User user = userDao.findUniqueByEmail(email);
            if(user != null && user.form != null && user.form.loanDetailsList != null){
                return ok(getLoan.render(user.form.getCoApplicantLoanDetails(),false));
            }
            
        }
        catch (NullPointerException e) {
            LOG.info("Caught NullPointerException: " + e.getMessage());
        }
        return ok("something went wrong!!!");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result addCoApplicantsDep() {
        JsonNode json = request().body().asJson();
        CoAppDependentDetails depBean = Json.fromJson(json, CoAppDependentDetails.class);
        javax.validation.Validator validator = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<CoAppDependentDetails>> violations = validator.validate(depBean);
        LOG.info("Errors: " + violations);

        if (!violations.isEmpty()) {
            return badRequest(RestUtils.createFailureResponse(violations));
        }

        String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        if(user != null){
            if(user.form == null) {
                user.form = new ApplicationForm();
                user.form.dependents = new ArrayList<Dependent>();
            } 
            else if (user.form.dependents == null) {
            	user.form.dependents = new ArrayList<Dependent>();
            }
            Dependent Dpt = new Dependent();
            depBean.populate(Dpt);
            user.form.dependents.add(Dpt); 

            Ebean.save(user.form);
            Ebean.save(user);

            return ok("Saved Json");
        }        
        
        return ok(RestUtils.createFailureResponse());
    }   

    public Result getCoApplicantsDep() {
        try{
            String email = session(SESSION_KEY_USER);
            User user = userDao.findUniqueByEmail(email);

            if(user != null && user.form != null && user.form.dependents != null){
            	return ok(dependents.render(user.form.getCoApplicantDependents(),false));
            }
            
        }
        catch (NullPointerException e) {
            LOG.info("Caught NullPointerException: " + e.getMessage());
        }
        return ok("something went wrong!!!");
    }

    public Result renderSubReq(String requirementSubTypeWithPrefix) {
    	Form<Requirement> req = Form.form(Requirement.class);
    	String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        if(user != null){
            if(user.form != null) {
                if(user.form.requirements != null){
                	if(!user.form.requirements.isEmpty()){
                		req = req.fill(user.form.requirements.get(0));
                	}
                }
            }
        }
        return ok(renderSubReq.render(req,requirementSubTypeWithPrefix));
    }
  
    @BodyParser.Of(BodyParser.Json.class)
    public Result addMiscBorrowingDetails() {
        JsonNode json = request().body().asJson();

        JsonNode amountNatureObj = json.get("amountNatureObj");
        AmountFromFriends aff = Json.fromJson(amountNatureObj, AmountFromFriends.class);
        javax.validation.Validator validator = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<AmountFromFriends>> violations = validator.validate(aff);
        LOG.info("Errors<violations>: " + violations);
        
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        Boolean flagForValuePutInResponse = false;
        MiscBorrowing mb = new MiscBorrowing();

        if(!violations.isEmpty()){
            flagForValuePutInResponse = true;
            response.put("amountNatureObj",RestUtils.createFailureResponse(violations));
        }else{
            aff.populate(mb);
        }

        String loanF = "asLoan";
        if(aff.amountNature.equals(loanF)){
            JsonNode loanFields = json.get("loanFields");
            AmountFromFriendsAsLoan affal = Json.fromJson(loanFields, AmountFromFriendsAsLoan.class);
            javax.validation.Validator validatorForLoan = play.data.validation.Validation.getValidator();
            Set<ConstraintViolation<AmountFromFriendsAsLoan>> violationsForLoan = validatorForLoan.validate(affal);
            LOG.info("Errors<violationsForLoan>: " + violationsForLoan);

            if(!violationsForLoan.isEmpty()){
                flagForValuePutInResponse = true;
                response.put("loanFields",RestUtils.createFailureResponse(violationsForLoan));
            }else{
                affal.populate(mb);
            }
        }

        String donationF = "asDonation";
        if(aff.amountNature.equals(donationF)){
            JsonNode donationFields = json.get("donationFields");
            AmountFromFriendsAsDonation affad = Json.fromJson(donationFields, AmountFromFriendsAsDonation.class);
            javax.validation.Validator validatorForDonation = play.data.validation.Validation.getValidator();
            Set<ConstraintViolation<AmountFromFriendsAsDonation>> violationsForDonation = validatorForDonation.validate(affad);
            LOG.info("Errors<violationsForDonation>: " + violationsForDonation);

            if(!violationsForDonation.isEmpty()){
                LOG.info("asDonation");
                flagForValuePutInResponse = true;
                response.put("donationFields",RestUtils.createFailureResponse(violationsForDonation));
            }else{
                affad.populate(mb);
            }
        }

        String moneyOwedF = "moneyOwed";
        if(aff.amountNature.equals(moneyOwedF)){
            JsonNode moneyOwedFields = json.get("moneyOwedFields");
            AmountFromFriendsAsRepayment affar = Json.fromJson(moneyOwedFields, AmountFromFriendsAsRepayment.class);
            javax.validation.Validator validatorForRepayment = play.data.validation.Validation.getValidator();
            Set<ConstraintViolation<AmountFromFriendsAsRepayment>> violationsForRepayment = validatorForRepayment.validate(affar);
            LOG.info("Errors<violationsForRepayment>: " + violationsForRepayment);
            if(!violationsForRepayment.isEmpty()){
                LOG.info("moneyOwed");
                flagForValuePutInResponse = true;
                response.put("moneyOwedFields",RestUtils.createFailureResponse(violationsForRepayment));
                LOG.info("response: "+response.toString());
            }else{
                affar.populate(mb);
            }
        }

        if (flagForValuePutInResponse == true) {               
            LOG.info("response: "+response.toString());
            return badRequest(response);
        }

        String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        if(user != null){
            if(user.form == null) {
                user.form = new ApplicationForm();
                user.form.miscBorrowings = new ArrayList<MiscBorrowing>();
            } else if (user.form.miscBorrowings == null) {
                user.form.miscBorrowings = new ArrayList<MiscBorrowing>();
            }
            user.form.miscBorrowings.add(mb); 

            Ebean.save(user.form);
            Ebean.save(user);
            return ok("Saved Json");
        }     
        return ok("Couldnt Save");
        //return ok(RestUtils.createFailureResponse());
    }   

    public Result getRequirements(){
        try{
            String email = session(SESSION_KEY_USER);
            User user = userDao.findUniqueByEmail(email);

            if(user != null && user.form != null && user.form.requirements != null){
                        return ok(getRequirements.render(user.form.requirements));
            }
        }
        catch (NullPointerException e) {
            LOG.info("Caught NullPointerException: " + e.getMessage());
        }
        return ok("something went wrong!!!");
    }
    
    public Result wantToAddCoApplicant() {
    	Form<WantToAddCoAppliacnt> waca = Form.form(WantToAddCoAppliacnt.class);
        return ok(wantToAddCoApplicant.render(waca));
        
    }
    
    public Result wantToAddCoApplicantSubmit() {
    	Form<WantToAddCoAppliacnt> waca = Form.form(WantToAddCoAppliacnt.class).bindFromRequest();
    	
    	if (waca.hasErrors()) {
            LOG.info("Errors: " + waca.errors());
            return badRequest(wantToAddCoApplicant.render(waca));
        }
        
        WantToAddCoAppliacnt choice = waca.get();
        String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        if(user != null){
            if(user.form == null) {
                user.form = new ApplicationForm();
            }    
            LOG.info("choice.coApplicantExists  : " + choice.coApplicantExists);
           
            choice.populate(user.form);
            
            Ebean.save(user.form);
            Ebean.save(user);
            
            if(choice.coApplicantExists.equals(TRUEVAL)){
            	//return redirect(routes.ApplicationFormController.coApplicantInfo());
            	return ok("Working on it");
            }
            else{
            	return redirect(routes.OwnContributionController.entireOwnContribution());
            }
        }
        return ok("Something Went Wrong");
    }
    
    public Result savedProperly(){
    	return ok("Saved Successfully");
    }
	
	 public static final class ClosingFinish {
		public String agreementAccept;
		public String howSiteWasKnown;
		public String Feedback;
	}
	 
	public BudgetValues budgetCalculation(Summary smry){
		String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
		
		BudgetValues budgetValues = new BudgetValues();
        budgetValues.approx_loan_elgblty = 0;
        
        if(user != null && user.form != null && user.form.applicant != null){
	    	if(user.form.co_applicant != null){
	        	if(user.form.applicant.occupation.equals(OCCUPATION_EMPLOYED) && user.form.co_applicant.occupation.equals(OCCUPATION_EMPLOYED)){
	        		if(user.form.applicant.employedIncome.net_sal != null && user.form.co_applicant.employedIncome.net_sal != null){
	        			budgetValues.approx_loan_elgblty = ((user.form.applicant.employedIncome.net_sal + user.form.co_applicant.employedIncome.net_sal)*50);
	        		}else if(user.form.applicant.employedIncome.net_sal != null && user.form.co_applicant.employedIncome.net_sal == null){
	        			budgetValues.approx_loan_elgblty = ((user.form.applicant.employedIncome.net_sal)*50);
	        		}
	            }
	        	else if(user.form.applicant.occupation.equals(OCCUPATION_SELF_EMPLOYED) && user.form.co_applicant.occupation.equals(OCCUPATION_SELF_EMPLOYED)){
	        		Integer applcntsTotalProfitFor3Years = user.form.applicant.selfEmployedIncome.yr1_pat + user.form.applicant.selfEmployedIncome.yr2_pat + user.form.applicant.selfEmployedIncome.yr3_pat;
	        		Integer applcntsTotalDepreciationFor3Years = user.form.applicant.selfEmployedIncome.yr1_depr + user.form.applicant.selfEmployedIncome.yr2_depr + user.form.applicant.selfEmployedIncome.yr3_depr;
	        		
	        		Integer coapplcntsTotalProfitFor3Years = user.form.co_applicant.selfEmployedIncome.yr1_pat + user.form.co_applicant.selfEmployedIncome.yr2_pat + user.form.co_applicant.selfEmployedIncome.yr3_pat;
	        		Integer coApplcntsTotalDepreciationFor3Years = user.form.co_applicant.selfEmployedIncome.yr1_depr + user.form.co_applicant.selfEmployedIncome.yr2_depr + user.form.co_applicant.selfEmployedIncome.yr3_depr;
	        		
	            	budgetValues.approx_loan_elgblty = (((applcntsTotalProfitFor3Years + applcntsTotalDepreciationFor3Years + coapplcntsTotalProfitFor3Years + coApplcntsTotalDepreciationFor3Years)/36)*50);
	            }
	        	else if(user.form.applicant.occupation.equals(OCCUPATION_EMPLOYED) && user.form.co_applicant.occupation.equals(OCCUPATION_SELF_EMPLOYED)){
	        		Integer coapplcntsTotalProfitFor3Years = user.form.co_applicant.selfEmployedIncome.yr1_pat + user.form.co_applicant.selfEmployedIncome.yr2_pat + user.form.co_applicant.selfEmployedIncome.yr3_pat;
	        		Integer coApplcntsTotalDepreciationFor3Years = user.form.co_applicant.selfEmployedIncome.yr1_depr + user.form.co_applicant.selfEmployedIncome.yr2_depr + user.form.co_applicant.selfEmployedIncome.yr3_depr;
	        		
	            	budgetValues.approx_loan_elgblty = ((((coapplcntsTotalProfitFor3Years + coApplcntsTotalDepreciationFor3Years)/36)+user.form.applicant.employedIncome.net_sal)*50);
	            }
	        	else if(user.form.applicant.occupation.equals(OCCUPATION_SELF_EMPLOYED) && user.form.co_applicant.occupation.equals(OCCUPATION_EMPLOYED)){
	        		Integer applcntsTotalProfitFor3Years = user.form.applicant.selfEmployedIncome.yr1_pat + user.form.applicant.selfEmployedIncome.yr2_pat + user.form.applicant.selfEmployedIncome.yr3_pat;
	        		Integer applcntsTotalDepreciationFor3Years = user.form.applicant.selfEmployedIncome.yr1_depr + user.form.applicant.selfEmployedIncome.yr2_depr + user.form.applicant.selfEmployedIncome.yr3_depr;
	        		
	            	budgetValues.approx_loan_elgblty = ((((applcntsTotalProfitFor3Years + applcntsTotalDepreciationFor3Years)/36)+user.form.co_applicant.employedIncome.net_sal)*50);
	            }
	    	}else{
	    		if(user.form.applicant.occupation.equals(OCCUPATION_EMPLOYED)){
	            	budgetValues.approx_loan_elgblty = (user.form.applicant.employedIncome.net_sal*50);
	            }
	    		else if(user.form.applicant.occupation.equals(OCCUPATION_SELF_EMPLOYED)){
	        		Integer applcntsTotalProfitFor3Years = returnZeroIfNull(user.form.applicant.selfEmployedIncome.yr1_pat) + returnZeroIfNull(user.form.applicant.selfEmployedIncome.yr2_pat) +returnZeroIfNull( user.form.applicant.selfEmployedIncome.yr3_pat);
	        		Integer applcntsTotalDepreciationFor3Years = returnZeroIfNull(user.form.applicant.selfEmployedIncome.yr1_depr) + returnZeroIfNull(user.form.applicant.selfEmployedIncome.yr2_depr) + returnZeroIfNull(user.form.applicant.selfEmployedIncome.yr3_depr);
	
	        		budgetValues.approx_loan_elgblty = (((applcntsTotalProfitFor3Years + applcntsTotalDepreciationFor3Years)/36)*50);
	            }
	    	}
        }
        
        budgetValues.all_bank_accounts_amnt_allocated = 0;
        if(user.form.bankAccounts != null){
	        for (BankAccount bankAccount : user.form.bankAccounts) {
	    		LOG.info("Bankaccount "+bankAccount);
	    		if(bankAccount.allocateToBuy != null && bankAccount.balance != null && bankAccount.loanEmi != null && bankAccount.balanceInstallments != null){
		    		if(bankAccount.allocateToBuy < (bankAccount.balance-(bankAccount.loanEmi*bankAccount.balanceInstallments))){
		    			budgetValues.all_bank_accounts_amnt_allocated = budgetValues.all_bank_accounts_amnt_allocated + bankAccount.allocateToBuy;
		    		}else{
		    			budgetValues.all_bank_accounts_amnt_allocated = budgetValues.all_bank_accounts_amnt_allocated + (bankAccount.balance-(bankAccount.loanEmi*bankAccount.balanceInstallments));  
		    		}
	    		}
	    	}
        }
        
        budgetValues.all_bank_accounts_bal = 0;
        if(user.form.bankAccounts != null){    
        	for (BankAccount bankAccount : user.form.bankAccounts) {
	    		LOG.info("Bankaccount "+bankAccount);
	    		if(bankAccount.balance != null){
	    			budgetValues.all_bank_accounts_bal = budgetValues.all_bank_accounts_bal + bankAccount.balance;
	    		}
	    	}
        }
        budgetValues.all_bank_accounts_deductions_in_year = 0;
        Integer totalBalanceLoanAmount = 0;
        if(user.form.bankAccounts != null){       
        	for (BankAccount bankAccount : user.form.bankAccounts) {
	    		//Integer totalMonthlyOtherRegularDeduction = 0;
	    		//Integer totalDeductionsInYearOfSingleAccount = 0;
	    		if(bankAccount.loanEmi != null && bankAccount.balanceInstallments!= null){
	    			totalBalanceLoanAmount = totalBalanceLoanAmount + (bankAccount.loanEmi*bankAccount.balanceInstallments);
	    		}
	    		//totalMonthlyOtherRegularDeduction = totalMonthlyOtherRegularDeduction + bankAccount.otherMonthlyDeduction;
	    		//totalDeductionsInYearOfSingleAccount = totalBalanceLoanAmount + (totalMonthlyOtherRegularDeduction*12);
	    		//totalDeductionsInYearFromAllAccounts = totalDeductionsInYearFromAllAccounts + totalDeductionsInYearOfSingleAccount;
	    		budgetValues.all_bank_accounts_deductions_in_year = totalBalanceLoanAmount;
	    		//not considering regular deductions here right now as we cant know for how long these deductions will be there or should be considered.
	    	}
        }
        //get current date time with Date()
        /*Date date = new Date();
        Integer totalOfAllFDAmountsMaturingIn2Years = 0;*/

        /*for (FixedDeposit fd : user.form.fixedDeposits) {
        	long difference = fd.maturityDate.getTime() - date.getTime();
        	long DifferenceInNoOfDays = difference / (24 * 60 * 60 * 1000);
        	//730 days = 2 years,hence the following
        	if(DifferenceInNoOfDays < 730.00){
        		totalOfAllFDAmountsMaturingIn2Years = totalOfAllFDAmountsMaturingIn2Years + fd.maturityAmount;
        	}
        	
    	}*/
        
        budgetValues.total_of_all_fd_amounts = 0;
        budgetValues.total_fds_deduction_loan_amount = 0;
        budgetValues.total_allocation_from_all_fds = 0;
        if(user.form.fixedDeposits != null){    
        	for (FixedDeposit fd : user.form.fixedDeposits){
	        	if(fd.maturityAmount != null && fd.allocation !=null && fd.montlyEmi != null &&fd.balanceInstallments != null){
	        		budgetValues.total_of_all_fd_amounts = budgetValues.total_of_all_fd_amounts + fd.maturityAmount;
		        	budgetValues.total_fds_deduction_loan_amount = budgetValues.total_fds_deduction_loan_amount + (fd.montlyEmi*fd.balanceInstallments);
		        	if(fd.allocation < (fd.maturityAmount-(fd.montlyEmi*fd.balanceInstallments))){
		        		budgetValues.total_allocation_from_all_fds = budgetValues.total_allocation_from_all_fds + fd.allocation;
		    		}else{
		    			budgetValues.total_allocation_from_all_fds = budgetValues.total_allocation_from_all_fds + (fd.maturityAmount-(fd.montlyEmi*fd.balanceInstallments));  
		    		}
	        	}
	        }
        }
        
        budgetValues.total_of_all_rd_amounts =0;
        budgetValues.total_deduction_loan_amount_from_rds = 0;
        budgetValues.total_allocation_from_all_rds = 0;
        if(user.form.recurringDeposits != null){
	        for (RecurringDeposit rd : user.form.recurringDeposits){
	        	if(rd.maturityAmount != null && rd.allocation !=null && rd.installment != null && rd.installmentsBalance != null){
		        	budgetValues.total_of_all_rd_amounts = budgetValues.total_of_all_rd_amounts + rd.maturityAmount;
		        	budgetValues.total_deduction_loan_amount_from_rds = budgetValues.total_deduction_loan_amount_from_rds + (rd.installment*rd.installmentsBalance);
		        	if(rd.allocation < (rd.maturityAmount - (rd.installment*rd.installmentsBalance))){
		        		budgetValues.total_allocation_from_all_rds = budgetValues.total_allocation_from_all_rds + rd.allocation;
		        	}else{
		        		budgetValues.total_allocation_from_all_rds = budgetValues.total_allocation_from_all_rds + (rd.maturityAmount - (rd.installment*rd.installmentsBalance));
		        	}
	        	}
	        }
        }
        
        budgetValues.total_bal_insu_amnt_of_all_insurances = 0;
        budgetValues.total_allocation_from_all_insurances = 0;
        Date date = new Date();
        budgetValues.total_of_all_insu_amounts_maturing_in_2_months = 0;
        if(user.form.insurances != null){
	        for (Insurance insu : user.form.insurances){
	        	if(insu.allocation != null && insu.maturityAmount != null && insu.premium != null && insu.premiumsBalance != null){
		        	budgetValues.total_bal_insu_amnt_of_all_insurances = budgetValues.total_bal_insu_amnt_of_all_insurances + (insu.premium*insu.premiumsBalance);
		
		        	long difference = insu.maturityDate.getTime() - date.getTime();
		        	long DifferenceInNoOfDays = difference / (24 * 60 * 60 * 1000);
		        	
		        	//61 days = 2 months,hence the following
		        	if(DifferenceInNoOfDays < 61.00){
		        		budgetValues.total_of_all_insu_amounts_maturing_in_2_months = budgetValues.total_of_all_insu_amounts_maturing_in_2_months + insu.maturityAmount;
		        		if(insu.allocation < (insu.maturityAmount - (insu.premium*insu.premiumsBalance))){
		        			budgetValues.total_allocation_from_all_insurances = budgetValues.total_allocation_from_all_insurances + insu.allocation;
		        		}else{
		        			budgetValues.total_allocation_from_all_insurances = budgetValues.total_allocation_from_all_insurances + (insu.maturityAmount - (insu.premium*insu.premiumsBalance));
		        		}
		        	}
	        	}
	        }
        }
        
        budgetValues.total_of_all_property_sell_amounts =0;
        budgetValues.total_deduction_loan_amount_from_properties = 0;
        budgetValues.total_allocation_from_all_properties = 0;
        if(user.form.property != null){
	        for (Property prop : user.form.property){
	        	if(prop.expectedPrice != null){
	        		budgetValues.total_of_all_property_sell_amounts = budgetValues.total_of_all_property_sell_amounts + prop.expectedPrice;
	        	}
	        	int pendingEmiAmount = 0;
	        	if (prop.loan != null && prop.loan && !prop.monthlyAmntDedctedFrmSal) {
	        		if(prop.montlyEmi != null && prop.balanceInstallments !=null){
	        			pendingEmiAmount = prop.montlyEmi * prop.balanceInstallments;
	        		}
	        	}
	        	
        		budgetValues.total_deduction_loan_amount_from_properties = budgetValues.total_deduction_loan_amount_from_properties + pendingEmiAmount;
	        	
        		if(prop.allocation != null && prop.expectedPrice != null){
		        	if(prop.allocation <= (prop.expectedPrice - pendingEmiAmount)){
		        		budgetValues.total_allocation_from_all_properties = budgetValues.total_allocation_from_all_properties + prop.allocation;
		        	}else{
		        		budgetValues.total_allocation_from_all_properties = budgetValues.total_allocation_from_all_properties + (prop.expectedPrice - pendingEmiAmount);
		        	}
        		}
	        }
        }
        
        budgetValues.total_of_all_loans_from_friends =0;
        budgetValues.total_deduction_loan_amount_from_misc_b = 0;
        budgetValues.total_of_all_donations_from_friends = 0;
        budgetValues.total_of_all_allocations_from_friend_as_loan = 0;
        budgetValues.total_of_all_money_owed = 0;
        String ASLOAN = "asLoan";
        String ASDONATION = "asDonation";
        String MONEYOWED = "moneyOwed";
        if(user.form.miscBorrowings != null){
	        for (MiscBorrowing mb : user.form.miscBorrowings){
	        	if(mb.type.equals(ASLOAN)){
	        		if(mb.amount != null && mb.numberOfYears != null && mb.emi != null){
		        		budgetValues.total_of_all_loans_from_friends = budgetValues.total_of_all_loans_from_friends + mb.amount;
		        		budgetValues.total_deduction_loan_amount_from_misc_b = budgetValues.total_deduction_loan_amount_from_misc_b + (12*mb.numberOfYears*mb.emi);
		        		budgetValues.total_of_all_allocations_from_friend_as_loan = budgetValues.total_of_all_allocations_from_friend_as_loan + (mb.amount-(12*mb.numberOfYears*mb.emi));
	        		}
	        	}
	        	if(mb.type.equals(ASDONATION)){
	        		if(mb.amount != null){
	        			budgetValues.total_of_all_donations_from_friends = budgetValues.total_of_all_donations_from_friends + mb.amount;
	        		}
	        	}
	        	if(mb.type.equals(MONEYOWED)){
	        		if(mb.amount != null){
	        			budgetValues.total_of_all_money_owed = budgetValues.total_of_all_money_owed + mb.amount;
	        		}
	        	}
	        }
        }
       
        budgetValues.total_of_assumed_cash_with_you =0;
        if(user.form.cashWithYou != null){
        			budgetValues.total_of_assumed_cash_with_you = user.form.cashWithYou;
        }
        
        budgetValues.applicants_total_deduction_loan_amounts = 0;
        if(user.form != null){
	        if(user.form.getApplicantLoanDetails() != null){
		        for (LoanDetails loanList : user.form.getApplicantLoanDetails()) {
		        	if(loanList.monthlyEmi != null && loanList.installmentsBalance != null){
		        		budgetValues.applicants_total_deduction_loan_amounts = budgetValues.applicants_total_deduction_loan_amounts + (loanList.monthlyEmi*loanList.installmentsBalance);
		        	}
		    	}
	        }
        }
        
        budgetValues.co_applicants_total_deduction_loan_amounts = 0;
        if(user.form != null){
	        if(user.form.getCoApplicantLoanDetails() != null){
		        for (LoanDetails loanList : user.form.getCoApplicantLoanDetails()) {
		        	if(loanList.monthlyEmi != null && loanList.installmentsBalance != null){
		        		budgetValues.co_applicants_total_deduction_loan_amounts = budgetValues.co_applicants_total_deduction_loan_amounts + (loanList.monthlyEmi*loanList.installmentsBalance);
		        	}
		    	}
	        }
        }
        
        budgetValues.total_addition_of_budget = budgetValues.approx_loan_elgblty + budgetValues.all_bank_accounts_bal + 
        budgetValues.total_of_all_fd_amounts + budgetValues.total_of_all_rd_amounts + budgetValues.total_of_all_insu_amounts_maturing_in_2_months + 
        budgetValues.total_of_all_property_sell_amounts +  budgetValues.total_of_all_loans_from_friends + 
        budgetValues.total_of_all_donations_from_friends + budgetValues.total_of_all_money_owed + budgetValues.total_of_assumed_cash_with_you;
        
        budgetValues.total_deduction_of_budget = budgetValues.applicants_total_deduction_loan_amounts + budgetValues.co_applicants_total_deduction_loan_amounts + 
        budgetValues.all_bank_accounts_deductions_in_year + budgetValues.total_fds_deduction_loan_amount + 
        budgetValues.total_deduction_loan_amount_from_rds + budgetValues.total_bal_insu_amnt_of_all_insurances + 
        budgetValues.total_deduction_loan_amount_from_properties + budgetValues.total_deduction_loan_amount_from_misc_b;
        
        budgetValues.total_allocation_for_budget = budgetValues.approx_loan_elgblty + budgetValues.all_bank_accounts_amnt_allocated +  
        budgetValues.total_allocation_from_all_fds + budgetValues.total_allocation_from_all_rds + budgetValues.total_allocation_from_all_insurances + 
        budgetValues.total_allocation_from_all_properties + budgetValues.total_of_all_allocations_from_friend_as_loan+
         budgetValues.total_of_all_donations_from_friends + budgetValues.total_of_all_money_owed + budgetValues.total_of_assumed_cash_with_you;
        
        if(smry != null){
	        if(smry.iAccptAbvCal != null){
	        	if(smry.iAccptAbvCal == 'N') {
	        		if(smry.approxLoanEli != null){
		        		if(smry.approxLoanEli == true){
							budgetValues.total_addition_of_budget = budgetValues.total_addition_of_budget - budgetValues.approx_loan_elgblty;
							budgetValues.total_allocation_for_budget = budgetValues.total_allocation_for_budget - budgetValues.approx_loan_elgblty;
		        		}
	        		}
	        		if(smry.allBankBalSummAmnt != null){
		        		if(smry.allBankBalSummAmnt == true){
							budgetValues.total_addition_of_budget = budgetValues.total_addition_of_budget - budgetValues.all_bank_accounts_bal;
							budgetValues.total_deduction_of_budget = budgetValues.total_deduction_of_budget - budgetValues.all_bank_accounts_deductions_in_year;
							budgetValues.total_allocation_for_budget = budgetValues.total_allocation_for_budget - budgetValues.all_bank_accounts_amnt_allocated;
						}
	        		}
	        		if(smry.allFdSummAmnt != null){
		        		if(smry.allFdSummAmnt == true){
							budgetValues.total_addition_of_budget = budgetValues.total_addition_of_budget - budgetValues.total_of_all_fd_amounts;
							budgetValues.total_deduction_of_budget = budgetValues.total_deduction_of_budget - budgetValues.total_fds_deduction_loan_amount;
							budgetValues.total_allocation_for_budget = budgetValues.total_allocation_for_budget - budgetValues.total_allocation_from_all_fds;
						}
	        		}
	        		if(smry.allRdSummAmnt != null){
		        		if(smry.allRdSummAmnt == true){
							budgetValues.total_addition_of_budget = budgetValues.total_addition_of_budget - budgetValues.total_of_all_rd_amounts;
							budgetValues.total_deduction_of_budget = budgetValues.total_deduction_of_budget - budgetValues.total_deduction_loan_amount_from_rds;
							budgetValues.total_allocation_for_budget = budgetValues.total_allocation_for_budget - budgetValues.total_allocation_from_all_rds;
						}
	        		}
	        		if(smry.allInsuranceSummAmnt != null){
		        		if(smry.allInsuranceSummAmnt == true){
							budgetValues.total_addition_of_budget = budgetValues.total_addition_of_budget - budgetValues.total_of_all_insu_amounts_maturing_in_2_months;
							budgetValues.total_deduction_of_budget = budgetValues.total_deduction_of_budget - budgetValues.total_bal_insu_amnt_of_all_insurances;
							budgetValues.total_allocation_for_budget = budgetValues.total_allocation_for_budget - budgetValues.total_allocation_from_all_insurances;
						}
	        		}
	        		if(smry.sellingSummAmnt != null){
		        		if(smry.sellingSummAmnt == true){
							budgetValues.total_addition_of_budget = budgetValues.total_addition_of_budget - budgetValues.total_of_all_property_sell_amounts;
							budgetValues.total_deduction_of_budget = budgetValues.total_deduction_of_budget - budgetValues.total_deduction_loan_amount_from_properties;
							budgetValues.total_allocation_for_budget = budgetValues.total_allocation_for_budget - budgetValues.total_allocation_from_all_properties;
						}
	        		}
	        		if(smry.borrowedSummary != null){
		        		if(smry.borrowedSummary == true){
							budgetValues.total_addition_of_budget = budgetValues.total_addition_of_budget - budgetValues.total_of_all_loans_from_friends;
							budgetValues.total_deduction_of_budget = budgetValues.total_deduction_of_budget - budgetValues.total_deduction_loan_amount_from_misc_b;
							budgetValues.total_allocation_for_budget = budgetValues.total_allocation_for_budget - budgetValues.total_of_all_allocations_from_friend_as_loan;
						}
	        		}
	        		if(smry.donationSummary != null){
		        		if(smry.donationSummary == true){
							budgetValues.total_addition_of_budget = budgetValues.total_addition_of_budget - budgetValues.total_of_all_donations_from_friends;
							budgetValues.total_allocation_for_budget = budgetValues.total_allocation_for_budget - budgetValues.total_of_all_donations_from_friends;
						}
	        		}
	        		if(smry.returnedAmount != null){
		        		if(smry.returnedAmount == true){
							budgetValues.total_addition_of_budget = budgetValues.total_addition_of_budget - budgetValues.total_of_all_money_owed;
							budgetValues.total_allocation_for_budget = budgetValues.total_allocation_for_budget - budgetValues.total_of_all_money_owed;
						}
	        		}
	        		if(smry.cashBalance != null){
		        		if(smry.cashBalance == true){
							budgetValues.total_addition_of_budget = budgetValues.total_addition_of_budget - budgetValues.total_of_assumed_cash_with_you;
							budgetValues.total_allocation_for_budget = budgetValues.total_allocation_for_budget - budgetValues.total_of_assumed_cash_with_you;
						}
	        		}
	        	}	
		    }
        }
        budgetValues.total_max_budget_calculated = budgetValues.total_addition_of_budget - budgetValues.total_deduction_of_budget;
        user.form.calculatedBudget = budgetValues.total_allocation_for_budget;
        userDao.save(user);
        return budgetValues;
	}
	
	/**
	 * 
	 * @param val
	 * @return
	 * This is quick fix for the fields self-employee income details 
	 * In future may be not required. Need to fix there either we need to force user to give zero rather than empty
	 */
	private Integer returnZeroIfNull(Integer val) {
		if(val != null) return val;
		return 0;
	}

	public Result summaryPage(){
		Form<Summary> summary = Form.form(Summary.class);
        
		String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        
        BudgetValues budgetVals = new BudgetValues();
        
        if(user != null){
			if(user.form != null){
				if(user.form.summary != null){
					summary = summary.fill(user.form.summary);
					budgetVals = budgetCalculation(user.form.summary);
				}else{
					budgetVals = budgetCalculation(null);
				}
			}else{
				budgetVals = budgetCalculation(null);
			}
		}else{
			budgetVals = budgetCalculation(null);
		}
		
        return ok(summaryPage.render(summary,budgetVals,user));
   }

	public Result summaryPageSubmit(){
		Form<Summary> summary = Form.form(Summary.class).bindFromRequest();
        
		String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        
        BudgetValues budgetVals = new BudgetValues();
        
		if(summary.hasErrors()){
			if(user != null){
				if(user.form != null){
					if(user.form.summary != null){
						budgetVals = budgetCalculation(user.form.summary);
					}else{
						budgetVals = budgetCalculation(null);
					}
				}else{
					budgetVals = budgetCalculation(null);
				}
			}else{
				budgetVals = budgetCalculation(null);
			}
			
			LOG.info("summaryErrors: " + summary.errors());
			return badRequest(summaryPage.render(summary,budgetVals,user));
		}
		
		Summary smry = summary.get();
		
        if(user != null){
            if(user.form == null) {
                user.form = new ApplicationForm();
				user.form.summary = smry;
            } else if (user.form.summary == null) {
				user.form.summary = smry;                
            } else if (user.form.summary != null) {
                Summary oldSummary = user.form.summary;
                user.form.summary = null;
                Ebean.save(user.form);
                Ebean.delete(oldSummary);
                user.form.summary = smry;
            }
            
            Ebean.save(user.form.summary); 
            Ebean.save(user.form);
            Ebean.save(user);
            
            BudgetValues budgetValues = budgetCalculation(user.form.summary);

            return ok(summaryPage.render(summary,budgetValues,user));
        }
		//budgetValues = summary.submitDataForSummary();
        return ok("Save Operation couldn't happen...");
	}
	
	public Result closingFinish(){
		Form<ClosingFinish> clFinish = Form.form(ClosingFinish.class);
   	return ok(closingFinish.render(clFinish));
   }
	
	public Result closingFinishDetails(){
        /*try{*/
            String email = session(SESSION_KEY_USER);
            User user = userDao.findUniqueByEmail(email);
             
            if(user != null && user.form != null){
               return ok(closingFinishDetails.render(user.form));
            }
      /*}
        catch (NullPointerException e) {
            System.err.println("Caught NullPointerException: " + e.getMessage());
        }*/
        //return badRequest(Json.stringify(RestUtils.createFailureResponse()));
        return ok("something went wrong!!!");
    }

    public Result payment() {
        User user = currentUser();
        Invoice invoice = user.form.invoice;
        if (invoice == null) {
            invoice = new Invoice();
            invoice.amount = new BigDecimal(500);
            invoice.description = "Proposed Society Form Fee";
            invoice.invoiceNumber = Invoice.createInvoiceNumber();
            invoice.paid = false;
            
            // NOTE: it appears as if invoice is getting saved first through Form, due to which
            // User entity fiends invoice persisted already and fails to persist the user_id column.
            invoice.user = user;
            user.invoices.add(invoice);
            user.form.invoice = invoice;
            userDao.save(user);
        }

        LOG.info("Invoice paid: " + invoice.paid);
        if (invoice.paid) {
        	return ok(views.html.payments.formFeesAlreadyPaid.render());
        	
        }
   
        return ok(payment.render(invoice));
    }
    
    public  Result paymentInfo(){
    	Form<PaymentInfo> form = Form.form(PaymentInfo.class);
    	return ok(paymentInfo.render(form));
    }
    
    public Result paymentSubmit() {
        User user = currentUser();
        Invoice invoice = user.form.invoice;
        if (invoice != null) {
            LOG.debug("Found Invoice: " + invoice);
             
            try {
                return paymentGateway.initiateFromClient(invoice, routes.ApplicationFormController.paymentComplete().url());
            } catch (PaymentGatewayError pge) {
            	LOG.error("Failed to initiate payment", pge);
                return badRequest("Failed to redirect to payment gateway");
            }
        }
        
        return badRequest("Could not find invoice.");
    }
    
    
    public Result paymentComplete() {
       DynamicForm form = Form.form().bindFromRequest();
        String secret = form.get(PaymentController.PARAM_SECRET);
        if (secret != null) {
            try {
            	Payment payment = paymentGateway.clientProcessPayment(secret);
            	User user = currentUser();

            	if (payment.success) {
            		LOG.info("Successfully completed payment for form: " + user.form.id);
                	String password = User.generatePassword();
                	user.password = BCrypt.hashpw(password, BCrypt.gensalt());
                	user.form.status = FormStatus.PAID; //form status should paid at this time
                	user.form.filled_date = new Date();
                	userDao.save(user);
                	LOG.info("Sending password to user.");
                	HtmlMail mail = new HtmlMail();
    				mail.setTo(user.email);
    				mail.setFrom(Mail.PS_FROM_ADDR);
    				mail.setSubject("Welcome to Proposed Society");
        			mail.setHtmlBody(views.html.mails.visitorPasswordMail.render(user,password).toString());
        			//Create attachment
        			/*Attachment attachment = new Attachment();
        			attachment.setAttachment(new File("File path"));
        			attachment.setName("payment_receipt.pdf");
        			attachment.setDescription("Payment receipt");
        			mail.setAttachment(attachment);*/
        			mailer.sentHtml(mail);
        			
                    return ok(views.html.payments.paymentResult.render(payment));
                    
                } else {
                	LOG.info("Payment failed for form: " + user.form.id);
                    return ok(views.html.payments.paymentResult.render(payment));
                }
            } catch (PaymentGatewayError e) {
                LOG.error("Failed to confirm payment", e);
            }
        } else {
            LOG.warn("No secret found!");
        }
    	
        return badRequest("Invalid payment!");
    }

    
    public Result enquiryMessage(){
    	DynamicForm form = Form.form().bindFromRequest();
    	 User user = currentUser();
    	 String message = form.get("message");
    	 String subject = form.get("subject");
    	 
    	 Mail mail = new Mail();
		 mail.setSender(Mail.PS_FROM_ADDR);
		 mail.setRecipient(Mail.VISITOR_TO_PS);
		 mail.setSubject(subject);
		 mail.setBody(views.html.mails.enquiryMessage.render(user,message).toString());
    	 Notifier.sendHtmlEmail(mail);
    	 
    	 LOG.info("Message sent successfully to proposed society ");
    	 return ok(successMessage.render(user));
    }
    
    public Result deleteApplicant() {
        User user = currentUser();
        Applicant applicant = user.form.applicant;
        user.form.applicant = null;
        Ebean.save(user.form);
        Ebean.delete(applicant);
        return ok("done!");
    }
    public Result deleteDependent(Long id) {
    	Dependent dep = dependentDao.findById(id, currentUser());
    	if (dep != null) {
    		dependentDao.delete(dep);
    		return ok("Deleted!");
    	}
    	return badRequest("Unauthorized!");
    }
    
    public Result deleteLoanDetails(Long id) {
    	LoanDetails ldtls = loanDetailsDao.findById(id, currentUser());
    	if (ldtls != null) {
    		loanDetailsDao.delete(ldtls);
    		return ok("Deleted!");
    	}
    	return badRequest("Unauthorized!");
    }

    public Result showPayment(Long id) {
    	Payment p = Ebean.find(Payment.class, id);
    	if (p != null) {
    		return ok(views.html.payments.paymentResult.render(p));
    	}
    	
    	return badRequest("Payment not found.");
    }
    
    public Result getPaymentMode(){
    	User  user = currentUser();
    	if(user.form.invoice != null && user.form.invoice.paid == true ){
    		return ok(views.html.payments.formFeesAlreadyPaid.render());
    	}
    	return ok(views.html.getPaymentMode.render());
    }
    public Result intermediate(){
    	DynamicForm form = Form.form().bindFromRequest();
    	String paymentMode = form.get("payment_mode");
    	
    	if(paymentMode.equals("avc_credit")){
    		return ok(views.html.agent.payment_through_avc.render(null));
    	}
    	
    	return redirect(routes.ApplicationFormController.payment());
    }
    
	public Result paymentThroughAVCcredit(){
		DynamicForm form = Form.form().bindFromRequest();
		String password = form.get("password").trim(); //password Can not be null or empty
		User user = currentUser();
		
		// FIXME: We will check password of User temporarily. There are issues currently in production for account visitor@ps.com
		User agent = user.agent.user;
		
		if((user != null && user.checkPassword(password)) || (agent != null && agent.checkPassword(password))){
			if(user.agent.formCredits > 0){
				createInvoiceAndPaymentForAVCPayment();
				 
		     		LOG.info("Successfully completed payment for form: " + user.form.id);
		     		password = User.generatePassword();
		        	user.password = BCrypt.hashpw(password, BCrypt.gensalt());
		        	user.form.status = FormStatus.PAID; //form status should paid at this time
		        	user.form.filled_date = new Date();
		        	userDao.save(user);
		        	LOG.info("Sending password to user.");
		        	
		        	HtmlMail mail = new HtmlMail();
    				mail.setTo(user.email);
    				mail.setFrom(Mail.PS_FROM_ADDR);
    				mail.setSubject("Welcome to Proposed Society");
        			mail.setHtmlBody(views.html.mails.visitorPasswordMail.render(user,password).toString());
        			//Create attachment
        			/*Attachment attachment = new Attachment();
        			attachment.setAttachment(new File("File path"));
        			attachment.setName("payment_receipt.pdf");
        			attachment.setDescription("Payment receipt");
        			mail.setAttachment(attachment);*/
        			mailer.sentHtml(mail);
		        	
		        	user.agent.manageAgentAVCcreditAndCommission(user);
		        	
		        	Ebean.save(user.agent);
		    		userDao.save(user);
		    		
				    return redirect(routes.ApplicationFormController.wholePersonalInfo());
			}else{
				return ok(views.html.agent.payment_through_avc.render(" Please rechager your AVC ,you have " + user.agent.formCredits + "form credits"));
			}
		}else {
			return ok(views.html.agent.payment_through_avc.render(" Password is incorrect"));
		}
		
		
	}



	private void createInvoiceAndPaymentForAVCPayment() {
		User user = currentUser();
		user.form.invoice = new Invoice();
		user.form.invoice.amount = new BigDecimal(FORM_FEES) ;
		user.form.invoice.invoiceNumber = Invoice.createInvoiceNumber();
		user.form.invoice.description = "Proposed Society Form Fees";
		user.form.invoice.paid = true ;
		user.form.invoice.user = user ;
		
		Payment payment = new Payment() ;
		payment.success = true ;
		payment.paymentMode = "Through AVC credit" ;
		payment.startTime = new Date();
		payment.invoice = user.form.invoice ;
		Ebean.save(payment);
		
		user.form.invoice .payments = new ArrayList<Payment>();
		user.form.invoice .payments.add(payment);
		user.invoices.add(	user.form.invoice);
		Ebean.save(user.form.invoice);
		Ebean.save(user.form);
		
		//Adding agent commission
		agentDao.save(user.agent);
		userDao.save(user);
		
		
	}
	
	@Authenticated(value = AgentAuthenticator.class)
	public Result createNewApplicantPage(){
		
		Agent agent  = agentDao.findByUser(currentUser());
		return ok(views.html.agent.createNewApplicant.render(agent,null));
	}
	
	// Handling agent filling application form behalf of applicant
	
	public Result generateAuthCode(VisitorLogin login) {

        Agent agent  = agentDao.findByUser(currentUser());

        if (login.email == null && login.mobile == null && login.name == null) { 	// May be not need necessary
        	return badRequest(views.html.agent.createNewApplicant.render(agent,PSError.VALIDATION_ERRORS));
        }

        User userByEmail = userDao.findUniqueByEmail(login.email);

        User userByMobile = userDao.findUniqueByMobile(login.mobile);

        if ((userByEmail != null ^ userByMobile != null)) {
        	return badRequest(views.html.agent.createNewApplicant.render(agent,PSError.EMAIL_OR_MOBILE_EXISTS));
        }

        User user = userByEmail;

        if (user == null) {
            user = User.create(login.name, login.email, login.mobile, Roles.VISITOR,agent);
            user.joiningDate = new Date();
        }


        String authcode = user.authcode;
        if (authcode == null) {
            authcode = User.generateAuthCode();
            user.authcode = authcode;
            user.authcode_hash = BCrypt.hashpw(authcode, BCrypt.gensalt());
        }

        userDao.save(user);

		Mail mail = new Mail();
					mail.setSender(Mail.PS_FROM_ADDR);
					mail.setRecipient(user.email);
					mail.setSubject("Welcome to Proposed Society");
					mail.setBody(views.html.mails.visitorWelcome.render(user, authcode).toString());
		Notifier.sendHtmlEmail(mail);

        try {
			smsGateway.send(user.mobile, SmsTemplate.SEND_AUTHCODE, user.name, authcode);
		} catch (SmsGatewayException e) {
			LOG.warn("Failed to send SMS.", e);
		}

        return ok(views.html.agent.createNewApplicant.render(agent,PSError.OK));
    }

    public Result createNewApplicant(){
        DynamicForm form = Form.form().bindFromRequest();
        VisitorLogin login = new VisitorLogin();
        login.name = form.get("name");
        login.mobile = form.get("mobile");
        login.email = form.get("email");

        login.name =  login.name .trim();
        login.mobile =  login.mobile.trim();
        login.email = login.email.trim();

        return generateAuthCode(login);

    }
    /*******************************************Short form payment functions start*********************************************************/
	 public Result shortFormPaymentSubmit() {
   		DynamicForm form = Form.form().bindFromRequest();
   		String form_id = form.get("form_id");

   		if(form_id == null){
   			return redirect(routes.ApplicationFormController.shortForm());
   		}
   		if(form_id.equals("")){
   			return redirect(routes.ApplicationFormController.shortForm());
   		}

   		long id = ApplicationForm.decryptId(form_id);

   		ApplicationForm applicationForm = applicationFormDao.findById(id);
   		if(applicationForm == null){
   			return redirect(routes.ApplicationFormController.shortForm());
   		}

       	if (applicationForm.invoice != null ) {
	   		if(applicationForm.invoice.paid){
	   			 return badRequest("payment already done");
	   		}
	        LOG.debug("Found Invoice: " + applicationForm.invoice);
	        try {
	        	return paymentGateway.initiateFromClient(applicationForm.invoice, routes.ApplicationFormController.shortFormPaymentComplete().url());

	        }catch (PaymentGatewayError pge) {
	        	LOG.error("Failed to initiate payment", pge);
	        	return badRequest("Failed to redirect to payment gateway");
	        }
        }
       return badRequest("Could not find invoice.");
    }
   
	 public Result shortFormPaymentComplete() {
	       DynamicForm form = Form.form().bindFromRequest();
	       String secret = form.get(PaymentController.PARAM_SECRET);
	       String applicantPassword ;
	       if (secret != null) {
	            try {
		            	Payment payment = paymentGateway.clientProcessPayment(secret);
		            	ApplicationForm applicationForm = applicationFormDao.findByInvoiceId(payment.invoice.id);
		            	
		            	//At this point either there is user entry in database with email id in form 
		            	
		            	if (payment.success) {
		                	LOG.info("Successfully completed payment for form: " + applicationForm.id);
		                	User user = userDao.findUniqueByEmail(applicationForm.applicant.email);
		                	user =userDao.findUniqueByMobile(applicationForm.applicant.phone1);

		                	if(user == null ){
		                		user = new User();
			                    user.name = applicationForm.applicant.getFullName();
			                    user.mobile = applicationForm.applicant.phone1;
			                    applicantPassword = User.generatePassword();
			                    user.authcode =applicantPassword;
			                    user.applicantPassword= BCrypt.hashpw(applicantPassword, BCrypt.gensalt());
			                    user.authcode_hash = user.applicantPassword;
			                    user.email = applicationForm.applicant.email;
			                    user.roles = new ArrayList<Role>();
			                    user.roles.add(new Role(user, Roles.APPLICANT));
			                    user.roles.add(new Role(user, Roles.VISITOR));
			                    user.joiningDate = new Date();
			                    user.lastLogin = new Date();
		                	}else{
		                		user.roles = new ArrayList<Role>();
		                		user.roles.add(new Role(user, Roles.APPLICANT));
		                		applicantPassword = User.generatePassword();
		                		user.applicantPassword = BCrypt.hashpw(
								applicantPassword, BCrypt.gensalt());
		                	}
		                	
		                	user.form =applicationForm;
		                	user.form.invoice.user = user;
		                	user.form.status =FormStatus.INCOMPLETE;
		                	user.form.filled_date = new Date();
		                	generateFormId(user, applicationForm.agent);
		                	applicationFormDao.save(user.form);
		                	userDao.save(user);
		                	//Handling agent commission report .
	              	        Agent agent = applicationForm.agent;
	              	        	
	              	        if(agent != null){ 
	              	        		createInvoiceAndPaymentForAVCPayment(applicationForm);
	              	        		manageAgentAVCcreditAndCommission(agent,applicationForm.id);
	              	        		LOG.info("Managing agent commission report and saving it with agent id : " +agent.id);
	              	        		agentDao.save(agent);
	              	        }
		                	//Generating receipt id for payment.
		                    payment.invoice.receiptId = idManager.generateReceiptNumber(IdType.RECEIPT_ID);
		            		invoiceDao.save(payment.invoice);
		            		
	                		LOG.info("Sending password to Applicant with email " + applicationForm.applicant.email);
	                		
	                		PaymentReceipt paymentReceipt = new PaymentReceipt();
	                    	paymentReceipt.setAmount(user.form.invoice.amount.toPlainString());
	                    	paymentReceipt.setAmountInWord("Five Hundred only");
	                    	paymentReceipt.setApplicantName(user.form.applicant.title+". "+ user.form.applicant.fname + " " + user.form.applicant.lname);
	                    	paymentReceipt.setApplicationNumber(user.form.formNumber);
	                    	paymentReceipt.setAuthorizationNumber(user.form.invoice.payments.get(user.form.invoice.payments.size()-1).transactionId);
	                    	paymentReceipt.setDescription(user.form.invoice.description);
	                    	paymentReceipt.setModeOfPayment(user.form.invoice.payments.get(user.form.invoice.payments.size()-1).paymentMode);
	                    	if(payment.invoice.receiptId != null){
	                    		paymentReceipt.setReceiptNumber(payment.invoice.receiptId.toString());
	                    	}else{
	                    		paymentReceipt.setReceiptNumber("NA");
	                    	}
	                    	paymentReceipt.setPaymentDate(user.form.filled_date);
	                    	
	                    	File file = null;
	                    	try {
	                			file = reportEngine.generatePdf("report/applicant/paymentReceipt.rptdesign", paymentReceipt,"payment_receipt");
	                			
	                			HtmlMail mail = new HtmlMail();
	            				mail.setTo(user.email);
	            				mail.setFrom(Mail.PS_FROM_ADDR);
	            				mail.setSubject("Welcome to Proposed Society");
	                			mail.setHtmlBody(views.html.mails.visitorPasswordMail.render(user,applicantPassword).toString());
	                			//Create attachment
	                			Attachment attachment = new Attachment();
	                			attachment.setAttachment(file);
	                			attachment.setName("payment_receipt.pdf");
	                			attachment.setDescription("Payment receipt");
	                			mail.setAttachment(attachment);
	                			mailer.sentHtml(mail);
	                            
	                		} catch (ReportEngineException e) {
	                			LOG.error("Got exception while creating report", e);
	                		}
	                    	
	                    	try {
	                    		//smsGateway.sendTextSms(user.mobile, views.html.sms.visitorPasswordSms.render(user.email, applicantPassword).toString());
	                    		smsGateway.send(user.mobile, SmsTemplate.APPLICANT_PASSWORD, user.email, applicantPassword);
	                    	} catch (SmsGatewayException e) {
	                    		LOG.warn("Failed to send SMS.", e);
	                    	}
	                    	
		                    return ok(views.html.shortFormForms.paymentResult.render(payment,applicationForm));
		                    
		                } else {
		                	
		                    return ok(views.html.shortFormForms.paymentResult.render(payment,applicationForm));
		                }
	            }catch (PaymentGatewayError e) {
	            		LOG.error("Failed to confirm payment", e);
	            }
	       } else{
	            LOG.warn("No secret found!");
	       }
	    
	       return badRequest("Invalid payment!");
   }
   public Result shortFormIntermediate(){
	   	DynamicForm form = Form.form().bindFromRequest();
	   	String paymentMode = form.get("payment_mode");
	   	long id = ApplicationForm.decryptId(form.get("form_id"));
		
	   	ApplicationForm applicationForm = applicationFormDao.findById(id);
	   	
	   	if(paymentMode == null && applicationForm == null){
	   		 return redirect(routes.ApplicationFormController.shortForm());
	   	}
	   	
	   	return paymentMode.equals("avc_credit") ? 
	   			ok(views.html.shortFormForms.payment_through_avc.render(null,applicationForm)) :
   				ok(views.html.shortFormForms.payment.render(applicationForm));
   }
   
   public Result shortFormPaymentThroughAVCcredit(){
		DynamicForm form = Form.form().bindFromRequest();
		String password = form.get("password").trim(); //password Can not be null or empty
		Long id = ApplicationForm.decryptId(form.get("form_id"));
		
		if(id == null && password.equals("")){
			LOG.info("Password or/And Form Id null (paymentThroughAVCcredit)");
			return redirect(routes.Application.internalError505());
		}
		ApplicationForm applicationForm = applicationFormDao.findById(id);
		
		if(applicationForm == null){
			LOG.info("Form not found with form id  > in (paymentThroughAVCcredit) " + id );
			return redirect(routes.ApplicationFormController.shortForm());
		}
		Agent agent =applicationForm.agent;
		if(agent == null){
			return ok(views.html.shortFormForms.payment_through_avc.render("Not found any agent with given code!",applicationForm)); 
		}
		
		if(!agent.user.checkPassword(password)){
			return ok(views.html.shortFormForms.payment_through_avc.render("Password is incorrect!",applicationForm));
		}
		
		if( !(agent.formCredits > 0)){
			String msg = " Please rechager your AVC ,you have " + agent.formCredits + "form credits" ;
			return ok(views.html.shortFormForms.payment_through_avc.render(msg,applicationForm)); 
		}
		Payment payment = createInvoiceAndPaymentForAVCPayment(applicationForm);

		manageAgentAVCcreditAndCommission(agent,applicationForm.id);
		agentDao.save(agent);
		
		String applicantPassword ;
		User user = userDao.findUniqueByEmail(applicationForm.applicant.email);
	   	if(user == null){
	   		user = new User();
	           user.name = applicationForm.applicant.fname;
	           user.mobile = applicationForm.applicant.phone1;
	           applicantPassword = User.generatePassword();
	           user.applicantPassword= BCrypt.hashpw(applicantPassword, BCrypt.gensalt());
	           user.authcode =applicantPassword;
	           user.authcode_hash = user.applicantPassword;
	           user.email = applicationForm.applicant.email;
	           user.roles = new ArrayList<Role>();
	           user.roles.add(new Role(user, Roles.APPLICANT));
	           user.roles.add(new Role(user, Roles.VISITOR));
	           user.joiningDate = new Date();
	           user.lastLogin = new Date();
	           user.agent =agent;
	   	}else{
	   		user.roles = new ArrayList<Role>();
		   		user.roles.add(new Role(user, Roles.APPLICANT));
		   		applicantPassword = User.generatePassword();
		   		user.agent = agent;
		   		user.applicantPassword = BCrypt.hashpw(applicantPassword, BCrypt.gensalt());
	   	}
	   	user.form = applicationForm;
		user.form.invoice.user = user;
	   	user.form.status =FormStatus.INCOMPLETE;
    	user.form.filled_date = new Date();
    	generateFormId(user, applicationForm.agent);
   		userDao.save(user);
   	    //Generating receipt id for payment.
        payment.invoice.receiptId = payment.invoice.receiptId = idManager.generateReceiptNumber(IdType.RECEIPT_ID);
		invoiceDao.save(payment.invoice);
		
   		LOG.info("Sending password to Applicant with email " + applicationForm.applicant.email);
   		
   		PaymentReceipt paymentReceipt = new PaymentReceipt();
    	paymentReceipt.setAmount(user.form.invoice.amount.toPlainString());
    	paymentReceipt.setAmountInWord("Five Hundred only");
    	paymentReceipt.setApplicantName(user.form.applicant.title+". "+ user.form.applicant.fname + " " + user.form.applicant.lname);
    	paymentReceipt.setApplicationNumber(user.form.formNumber);
    	paymentReceipt.setAuthorizationNumber(user.form.invoice.payments.get(user.form.invoice.payments.size()-1).transactionId);
    	paymentReceipt.setDescription(user.form.invoice.description);
    	paymentReceipt.setModeOfPayment(user.form.invoice.payments.get(user.form.invoice.payments.size()-1).paymentMode);
    	if(payment.invoice.receiptId != null){
    		paymentReceipt.setReceiptNumber(payment.invoice.receiptId.toString());
    	}else{
    		paymentReceipt.setReceiptNumber("NA");
    	}
    	paymentReceipt.setPaymentDate(user.form.filled_date);
    	
    	File file = null;
    	try {
			file = reportEngine.generatePdf("report/applicant/paymentReceipt.rptdesign", paymentReceipt,"payment_receipt");
			
			HtmlMail mail = new HtmlMail();
			mail.setTo(user.email);
			mail.setFrom(Mail.PS_FROM_ADDR);
			mail.setSubject("Welcome to Proposed Society");
			mail.setHtmlBody(views.html.mails.visitorPasswordMail.render(user,applicantPassword).toString());
			
			//Create attachment
			Attachment attachment = new Attachment();
			attachment.setAttachment(file);
			attachment.setName("payment_receipt.pdf");
			attachment.setDescription("Payment receipt");
			mail.setAttachment(attachment);
			mailer.sentHtml(mail);
            
		} catch (ReportEngineException e) {
			LOG.error("Got exception while creating report", e);
		}
    	
    	try {
			//smsGateway.sendTextSms(user.mobile, views.html.sms.visitorPasswordSms.render(user,applicantPassword).toString());
    		smsGateway.send(user.mobile, SmsTemplate.APPLICANT_PASSWORD, user.email, applicantPassword);
		} catch (SmsGatewayException e) {
			LOG.warn("Failed to send SMS.", e);
		}
    	
    	return ok(views.html.shortFormForms.paymentResult.render(payment,applicationForm));
   }
   
   private void generateFormId(User user, Agent formAgent) {
	   if(user.agent != null && formAgent != null){
		   user.form.formNumber = idManager.generateFormNumber(IdType.FORM_NUMBER, Prefix.APPLICATION_THROUGH_AGENT);
	   }else{
		   user.form.formNumber = idManager.generateFormNumber(IdType.FORM_NUMBER, Prefix.DIRECT_APPLICATION);
	   }
   }
   
   private void manageAgentAVCcreditAndCommission(Agent agent, long id) {
		agent.formCredits -= 1;
		agent.totalFormFilledCount += 1;
		agent.commissions.add(recordCommission(agent, id));
   }
   
	private AgentCommission recordCommission(Agent agent,long commissionAgainstId) {
		AgentCommission agentCommission = new AgentCommission();
			agentCommission.agent = agent;
			agentCommission.commissionAmount = new BigDecimal(AGENT_COMMISSION); 
			agentCommission.description = "Towards Application Form Fees";
			agentCommission.commissionDate = new Date();
			agentCommission.commissionAgainstId = commissionAgainstId;
		
		return agentCommission;
	}

	private Payment createInvoiceAndPaymentForAVCPayment(ApplicationForm form) {
		
			form.invoice.amount = new BigDecimal(FORM_FEES) ;
			form.invoice.invoiceNumber = Invoice.createInvoiceNumber();
			form.invoice.description = "Proposed Society Form Fees";
			form.invoice.paid = true ;
		
		Payment payment = new Payment() ;
			payment.success = true ;
			payment.authDesc = "Y";
			payment.clientProcessed = true;
			payment.gatewayProcessed = true;
			payment.redirectUrl = "/short_form/payment/complete ";
			payment.success = true;
			payment.startTime = new Date();
			payment.completionTime  = new Date();
			payment.paymentMode = "Through AVC credit" ;
			payment.startTime = new Date();
			payment.transactionId = idgen.generateTxnId();
			payment.invoice = form.invoice ;
			
			Ebean.save(payment);
		
		form.invoice .payments.add(payment);
		Ebean.save(form.invoice);
		Ebean.save(form);
		return payment;
	}
/********************************************Short form payment functions over *******************************************************/
}
