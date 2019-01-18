package controllers;

import java.math.BigDecimal;
import java.util.List;

import models.Agent;
import models.AgentAddress;
import models.AgentForm;
import models.FileAttachment;
import models.Invoice;
import models.Payment;
import models.PaymentInfo;
import models.Role;
import models.User;
import models.dao.AgentDao;
import models.dao.DaoProvider;
import models.dao.FileAttachmentDao;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import security.auth.AgentAuthenticator;
import security.auth.VisitorAuthenticator;
import utils.IdGenerator;
import utils.IdGenerator.Prefix;
import utils.Mail;
import utils.Notifier;
import utils.PaymentGateway;
import utils.PaymentGatewayError;
import utils.SmsGateway;
import validation.PSForm;
import views.html.displayAgentInfo;
import views.html.paymentInfo;
import views.html.agent.*;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;
import com.google.inject.Inject;
import common.Roles;


public class AgentFormController extends AuthenticatedUserController {
	
    private static final String BIRTH_PLACE = "birth_place";
    private static final String PERMANENT_ADDRESS = "permanent_address";
    private static final String RESIDENTIAL_ADDRESS = "residential_address";
    private static final String OFFICE_ADDRESS ="office_address";
    private static final String NRI ="N";
    private static final String NOT_HAVE ="notHave";
	private static final Logger LOG = LoggerFactory.getLogger(Agent.class);
	private static final int FORM_COST = 500;
    private final FileAttachmentDao attachmentDao;
    private final AgentDao agentDao;
	private final SmsGateway smsGateway;
    private final PaymentGateway paymentGateway;
    private final IdGenerator idGenerator;
    private final int PAGE_SIZE = 5; // 15 agents per page

	@Inject
    public AgentFormController(DaoProvider provider,PaymentGateway paymentGateway,SmsGateway smsGateway,IdGenerator idGenerator) {
		super(provider.userDao());
    	this.attachmentDao = provider.fileAttachmentDao();
    	this.agentDao = provider.agentDao();
    	this.smsGateway = smsGateway;
    	this.paymentGateway = paymentGateway;
    	this.idGenerator = idGenerator ;
	}
	

	@Authenticated(value = VisitorAuthenticator.class)
	public Result agentForm() {
        Form<AgentForm> form = Form.form(AgentForm.class);
        User user = currentUser();
        Agent agent = agentDao.findByUser(user);
        String formStatus = "noStatus";
        if(agent != null){
        	if(agent.form != null){
        		if(agent.form.status != null && !agent.form.status.equals(FormStatus.REJECTED) && !agent.form.status.equals(FormStatus.ON_HOLD)){
                	return redirect(routes.AgentDashboardController.agentDashboard());
                }
            	form = form.fill(agent.form);
            	formStatus = agent.form.status;
            }
        }
        
        return ok(agentForm.render(form,formStatus,user));
	}
	
	@Authenticated(value = VisitorAuthenticator.class)
	public Result agentFormSubmit() {
		 Form<AgentForm> form = PSForm.form(AgentForm.class).bindFromRequest();
		 if(form.hasErrors()){
			 LOG.info("Found errors in agent form" + form.errors());
			 return badRequest(agentForm.render(form,"noStatus",currentUser()));
		 }
		 
		 Agent agentOld = agentDao.findByUser(currentUser());
		 String oldFormStatus ;
		 if(agentOld != null){
			 oldFormStatus = agentOld.form.status;
			 prepareForDelete(agentOld);
			 Ebean.delete(agentOld);
		 }
		 
		 Agent agent = Agent.create(currentUser());
		 agent.form = form.get();
		 agent = prepareForSave(agent);
		 agent.form.status =FormStatus.PENDING;
		 agent.form.user = agent.user;
		 Ebean.save(agent.form);
		 Ebean.save(agent);
		 
		 //Note : For identityCode generation there is no separate table for store last agent_id 
		 agent.agentCode = idGenerator.generate(agentDao.findByUser(currentUser()).id, Prefix.AGENT); //Generating IdentityCode for Agent
		 agentDao.save(agent);
		 return redirect(routes.AgentFormController.formPayment());
	}
	
	//starting Agent Payment
	@Authenticated(value = VisitorAuthenticator.class)
	 public Result payment() {
		 		User user = currentUser();
		 		Invoice invoice = new Invoice();
	            invoice.amount = new BigDecimal(10000);
	            invoice.description = "Proposed Society Form Fee";
	            invoice.invoiceNumber = "PSINV-" + System.currentTimeMillis();
	            invoice.paid = false;
	            user.invoices.add(invoice);
	            userDao.save(user);
	            
	            LOG.info("creating invoice for user : " + user.id);
	            LOG.info("creating invoice of amount : " + invoice.amount);
	            
	            return ok(views.html.agent.payment.render(invoice));
	    }
	
	@Authenticated(value = VisitorAuthenticator.class)
	 public Result formPayment() {
		 		User user = currentUser();
		 		Invoice invoice = new Invoice();
	            invoice.amount = new BigDecimal(500);
	            invoice.description = "Proposed Society Form Fee";
	            invoice.invoiceNumber = "PSINV-" + System.currentTimeMillis();
	            invoice.paid = false;
	            user.invoices.add(invoice);
	            userDao.save(user);
	            
	            LOG.info("creating invoice for user : " + user.id);
	            LOG.info("creating invoice of amount : " + invoice.amount);
	            
	            return ok(views.html.agent.payment.render(invoice));
	    }
	    
	@Authenticated(value = VisitorAuthenticator.class)
	 public  Result paymentInfo(){
	    	Form<PaymentInfo> form = Form.form(PaymentInfo.class);
	    	return ok(paymentInfo.render(form));
	    }
	@Authenticated(value = VisitorAuthenticator.class)
	 public Result paymentSubmit() {
	        User user = currentUser();
	        if (!user.invoices.isEmpty()) {
	        	
	            // For now we consider last generated invoice
	            Invoice invoice = user.invoices.get((user.invoices.size()-1));
	            LOG.debug("Found Invoice: " + invoice);
	            
	            try {
	                return paymentGateway.initiateFromClient(invoice, routes.AgentFormController.paymentComplete().url());
	            } catch (PaymentGatewayError pge) {
	            	LOG.error("Failed to initiate payment", pge);
	                return badRequest("Failed to redirect to payment gateway");
	            }
	        }
	        
	        return badRequest("Could not find invoice.");
	    }
	    
	@Authenticated(value = VisitorAuthenticator.class)
	 public Result paymentComplete() {
	       DynamicForm form = Form.form().bindFromRequest();
	       String secret = form.get(PaymentController.PARAM_SECRET);
	       if (secret != null) {
	            try {
	            	Payment payment = paymentGateway.clientProcessPayment(secret);
	            	User user = currentUser();
	            	Agent agent  = agentDao.findByUser(user);
	            	if (payment.success) {
	            		
	            		LOG.info("Successfully completed payment for form: " + agent.form.id);
	                	if(!user.hasRole(Roles.AGENT)){    
	                		prepareAgent(agent);
	                		// Send Password (Mail)
	                		String password =User.generatePassword();
	                		user.password =BCrypt.hashpw(password, BCrypt.gensalt());
	                		agentDao.save(agent);
	        				LOG.info("Sending password to user." + agent.user.email);
	        				userDao.save(user);
	        				
	        				Mail mail = new Mail();
	        				mail.setSender(Mail.PS_FROM_ADDR);
	        				mail.setRecipient(agent.user.email);
	        				mail.setSubject("Welcome to Proposed Society");
	        				mail.setBody(views.html.mails.agentPasswordMail.render(user,password).toString());
	        				Notifier.sendHtmlEmail(mail);
	        						
	        				LOG.info("Sending Agent Code to agent " + agent.user.email);
	        				
	   	        			mail.setSender(Mail.PS_FROM_ADDR);
	   	        			mail.setRecipient(agent.user.email);
	        				mail.setSubject("Agent Code from  Proposed Society");
	        				mail.setBody(views.html.mails.agentCodeMail.render(user,agent.agentCode).toString());
	        				Notifier.sendHtmlEmail(mail);
	        				
	                	}else{
	                		// Handling AVC recharge System
	                		 updateAVC(payment,user);
	                		 return ok(views.html.agent.avcpaymentResult.render(payment));
	                	}
	                    return ok(views.html.agent.paymentResult.render(payment));
	                    
	                } else {
	                	LOG.info("Payment failed for form: " + agent.form.id);
	                    return ok(views.html.agent.paymentResult.render(payment));
	                }
	            } catch (PaymentGatewayError e) {
	                LOG.error("Failed to confirm payment", e);
	            }
	        } else {
	            LOG.warn("No secret found!");
	        }
	    	
	        return badRequest("Invalid payment!");
	    }
	    
	    @Authenticated(value = AgentAuthenticator.class)
	    public void updateAVC(Payment payment, User user) {
	    	LOG.info("Updating AVc for user " + user.id);
			Agent agent = agentDao.findByUser(user);
			agent.totalAmountPaid += payment.invoice.amount.intValueExact();
			if(agent.formCredits == 0){
				agent.formCredits += 10;//Initial payment
			}else{
				agent.formCredits += (payment.invoice.amount.intValueExact() / FORM_COST);
			}
			agentDao.save(agent);
			
			Mail mail = new Mail();
			mail.setSender(Mail.PS_FROM_ADDR);
			mail.setRecipient(user.email);
			mail.setSubject("AVC recharge");
			mail.setBody("Your form credits has been update to " + agent.formCredits);
			Notifier.sendHtmlEmail(mail);
			
		}



		public void prepareAgent(Agent agent) {
	    	
				// Adding Role Agent To User
				Role userRole = new Role();
				userRole.role = Roles.AGENT;
				userRole.user = agent.user;
				agent.user.roles.add(userRole);
				Ebean.save(userRole);
				LOG.info("Adding role as an agent");
				userDao.save(agent.user);
				agent.form.status = FormStatus.PAID;
		}


	    
	private void prepareForDelete(Agent agentOld) {
		
		load(agentOld.form.addressproof);
		load(agentOld.form.birth_place);
		load(agentOld.form.birthCertificate);
		load(agentOld.form.communication_address);
		load(agentOld.form.english);
		load(agentOld.form.marathi);
		load(agentOld.form.english);
		load(agentOld.form.identity1);
		load(agentOld.form.identity2);
		load(agentOld.form.identity3);
		load(agentOld.form.office_address);
		load(agentOld.form.office_addressproof);
		load(agentOld.form.permanent_address);
		load(agentOld.form.photograph);
		load(agentOld.form.residential_address);
		
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

	public  Agent prepareForSave(Agent agent) {
		agent.user = currentUser();
		if (agent.form.photograph_id != null ) {
            FileAttachment file = attachmentDao.findByFilePath(agent.form.photograph_id,agent.user);
            if (file != null) {
                agent.form.photograph = file;
            }
        }

        if (agent.form.identity1_id != null ) {
            FileAttachment file = attachmentDao.findByFilePath(agent.form.identity1_id,agent.user);
            if (file != null) {
                agent.form.identity1 = file;
            }
        }

        if (agent.form.identity2_id != null ) {
            FileAttachment file3 = attachmentDao.findByFilePath(agent.form.identity2_id,agent.user);
            if (file3 != null) {
                agent.form.identity2 = file3;
            }
        }
        if (agent.form.identity3_id != null ) {
            FileAttachment file4 = attachmentDao.findByFilePath(agent.form.identity3_id,agent.user);
            if (file4 != null) {
                agent.form.identity3 = file4;
            }
        }
        if (agent.form.addressproof_id != null ) {
            FileAttachment file5 = attachmentDao.findByFilePath(agent.form.addressproof_id,agent.user);
            if (file5 != null) {
                agent.form.addressproof = file5;
            }
        }
        if (agent.form.office_addressproof_id != null ) {
            FileAttachment file5 = attachmentDao.findByFilePath(agent.form.office_addressproof_id,agent.user);
            if (file5 != null) {
                agent.form.office_addressproof = file5;
            }
        }
        if (agent.form.birthCertificate_id != null ) {
            FileAttachment file4 = attachmentDao.findByFilePath(agent.form.birthCertificate_id,agent.user);
            if (file4 != null) {
                agent.form.birthCertificate = file4;
            }
        }
        if (agent.form.resi_light_bill_id != null ) {
            FileAttachment file = attachmentDao.findByFilePath(agent.form.resi_light_bill_id,agent.user);
            if (file != null) {
                agent.form.resi_light_bill = file;
            }
        }
        
        if (agent.form.resi_tax_receipt_id != null ) {
            FileAttachment file = attachmentDao.findByFilePath(agent.form.resi_tax_receipt_id,agent.user);
            if (file != null) {
                agent.form.resi_tax_receipt = file;
            }
        }
        if (agent.form.office_light_bill_id != null ) {
            FileAttachment file = attachmentDao.findByFilePath(agent.form.office_light_bill_id,agent.user);
            if (file != null) {
                agent.form.office_light_bill = file;
            }
        }
        
        if (agent.form.permanent_address_same_as.equals(BIRTH_PLACE)) {
            agent.form.permanent_address = new AgentAddress(agent.form.birth_place);
        }
        
        if (agent.form.residential_address_same_as.equals(BIRTH_PLACE)) {
            agent.form.residential_address = new AgentAddress(agent.form.birth_place);
        }else if (agent.form.residential_address_same_as.equals(PERMANENT_ADDRESS)) {
            agent.form.residential_address = new AgentAddress(agent.form.permanent_address);
        }
        
        if (agent.form.communication_address_same_as.equals(BIRTH_PLACE)) {
            agent.form.communication_address = new AgentAddress(agent.form.birth_place);
        }else if (agent.form.communication_address_same_as.equals(PERMANENT_ADDRESS)) {
            agent.form.communication_address = new AgentAddress(agent.form.permanent_address);
        }else if (agent.form.communication_address_same_as.equals(RESIDENTIAL_ADDRESS)) {
            agent.form.communication_address = new AgentAddress(agent.form.residential_address);
        }else if (agent.form.communication_address_same_as.equals(OFFICE_ADDRESS)) {
            agent.form.communication_address = new AgentAddress(agent.form.office_address);
        }
        
        if(agent.form.office_address_same_as.equals(RESIDENTIAL_ADDRESS)){
        	agent.form.office_address =  new AgentAddress(agent.form.residential_address);
        }
        else if(agent.form.office_address_same_as.equals(NOT_HAVE)){
        	agent.form.office_address = null;
        }
        // Handling NRI Condition
        if( ! agent.form.nationality.equals(NRI)){
        	agent.form.nri_address =null;
        }
		return agent;
	}
	
	/*@Authenticated(value = Agen.class)
	public Result agentDashboard(){
		Agent agent =agentDao.findByUser(currentUser());
		Page<User> applicationFormList = userDao.getIncompleteApplicantForm(PAGE_SIZE,0,agent);
		
		return ok(views.html.agent.agentDashboard.render(applicationFormList,agent));
	}*/
	
	public  List<Agent> getAllAgent(){
			return agentDao.findAll();
	}
	
	@Authenticated(value = VisitorAuthenticator.class)
	public Result displayAgentInfo(Long id){
		Agent agent = agentDao.findById(id);
		return ok(displayAgentInfo.render(agent,null));
	}
	
	@Authenticated(value = AgentAuthenticator.class)
	public Result rechargeAVC(){
		return ok(views.html.agent.rechargeAVC.render());
	}
	
	@Authenticated(value = AgentAuthenticator.class)
	public Result rechargeAVCPayment(){
		DynamicForm form = Form.form().bindFromRequest();
		String formCount = form.get("formCount");
		int amount = 500 * (Integer.parseInt(formCount));
		User user = currentUser();
 		Invoice invoice = new Invoice();
        invoice.amount = new BigDecimal(amount);
        invoice.description = "Proposed Society AVC recharge ";
        invoice.invoiceNumber = "PSINV-" + System.currentTimeMillis();
        invoice.paid = false;
        user.invoices.add(invoice);
        userDao.save(user);
        LOG.info("creating invoice for user : " + user.id);
        LOG.info("creating invoice of amount : " + invoice.amount);
        return ok(views.html.agent.payment.render(invoice));
		
	}
	
	@Authenticated(value = AgentAuthenticator.class)
	public Result incompleteApplicantForms(int pageNumber){
		Agent agent = agentDao.findByUser(currentUser());
		Page<User> applicationList = userDao.getIncompleteApplicantForm(15, pageNumber,agent);
		return ok(incompleteFormUserList.render(applicationList,agent));
	
	}
}	