package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import play.data.Form;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import security.auth.BueroAuthenticator;
import views.html.investigationBuero.*;
import views.html.*;
import models.Agent;
import models.AgentFeedbackFormFile;
import models.InvestigationReport;
import models.Buero;
import models.User;
import models.dao.AgentDao;
import models.dao.AgentFeedbackFormFileDao;
import models.dao.InvestigationReportDao;
import models.dao.DaoProvider;
import models.dao.FileAttachmentDao;
import models.dao.BueroDao;

@Authenticated(value = BueroAuthenticator.class)
public class BueroController extends AuthenticatedUserController{
	private final AgentDao agentDao;
	private final InvestigationReportDao investigationReportDao;
	private final FileAttachmentDao fileAttachmentDao;
	private final BueroDao bueroDao;
	private final AgentFeedbackFormFileDao agentFeedbackFormFileDao;
	private final Integer PAGE_SIZE =15;
	private static final Logger LOG = LoggerFactory.getLogger(BueroController.class);
	
	@Inject
	public BueroController(DaoProvider provider) {
		super(provider.userDao());
		this.agentDao = provider.agentDao();
		this.bueroDao =provider.bueroDao();
		this.investigationReportDao =provider.investigationReportDao();
		this.fileAttachmentDao =provider.fileAttachmentDao();
		this.agentFeedbackFormFileDao = provider.agentFeedbackFormFileDao();
	}

	public Result searchIbsAgent(int pageNumber){
		User user = currentUser();
		Buero buero=null;
		if(user !=null){
			buero =  bueroDao.findUniqueByUserId(user.id);
		}
		return ok(agentList.render(agentDao.findPageForIb(PAGE_SIZE, pageNumber,buero),user));
	}
	 
	public Result bueroDashboard() {
		return ok(bueroDashboard.render(currentUser()));
	}
	
	public Result displayAgentInfoToBuero(Long id){ //Display info of peticular id 
		Agent agent = agentDao.findById(id);
		Form<InvestigationReport> feedbackForm = Form.form(InvestigationReport.class);
		User user = currentUser();
		Buero buero = bueroDao.findUniqueByUserId(user.id);
		InvestigationReport form =null;
		if(agent !=null && buero !=null){
			form = investigationReportDao.getFeedbackForm(buero, agent);
		}
		if(form != null){
			feedbackForm = feedbackForm.fill(form);
		}
		return ok(displayAgentInfoToBuero.render(agent,user,feedbackForm));
	}
	
	
	public Result uploadFeedbackFile(Long id){ //Id is agent primary key
		Form<InvestigationReport> feedbackform = Form.form(InvestigationReport.class).bindFromRequest();
		User user = currentUser();
		if(feedbackform.hasErrors()){
			 return badRequest(displayAgentInfoToBuero.render(agentDao.findById(id),user,feedbackform));
		}
		
		InvestigationReport bueroForm = feedbackform.get();
		bueroForm.ib = bueroDao.findUniqueByUserId(user.id);
		bueroForm.agent =agentDao.findById(id);
		bueroForm.attachment = fileAttachmentDao.findByFilePath(bueroForm.attachment_id);
		InvestigationReport form= null;
		if(bueroForm.ib != null && bueroForm.agent!=null){
			 form = investigationReportDao.getFeedbackForm(bueroDao.findUniqueByUserId(user.id),agentDao.findById(id));//Buero & Agent is passed as parameter 
		}
		if(form == null){
			investigationReportDao.save(bueroForm);
		}
		LOG.info("Buero feedback report saved for agent_id"+agentDao.findById(id).id); 
		return ok(feedbackSubmitWindow.render(user));
	}

	public Result downloadAgentFeedbackForm() {
		AgentFeedbackFormFile file = agentFeedbackFormFileDao.findLatest();
		return ok(downloadAgentFeedbackForm.render(file, currentUser()));
	}
}