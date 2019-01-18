package controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import json.BindingResult;
import json.JsonHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.ApplicationForm;
import models.ExternalAmenity;
import models.InternalAmenity;
import models.Invoice;
import models.Nearby;
import models.Requirement;
import models.RequirementAddress;
import models.User;
import models.dao.DaoProvider;
import models.dao.UserDao;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.LoggerFactory;

import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

import com.avaje.ebean.Ebean;
import com.google.inject.Inject;

@Authenticated(value = MobileAuthenticator.class)
public class MobileApplicationFormController extends Controller{
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationFormController.class);
	private static final int FORM_FEES = 500;
    private final Map<String, Class<?>> propertyTypeToGroup = new HashMap<String, Class<?>>();
	private final DaoProvider daoProvider;
    
	@Inject
	public MobileApplicationFormController(DaoProvider provider){
		this.daoProvider = provider;
	
	}
	
    @BodyParser.Of(BodyParser.Json.class)

    public Result Test(){
	    System.out.println("-----------------");
	    return ok();
    }



        public Result MobileshortFormSubmit(){

    	JsonNode formjson = request().body().asJson();
    	JsonNode requirementType = formjson.get("requirementSubType");
    	LOG.info("requirement_type: "+ requirementType);
    	JsonNode requirement = formjson.get("requirement");
    	@SuppressWarnings("unused")
		Class<?> group = propertyTypeToGroup.get(requirementType);
    	BindingResult<Requirement> reqForm = JsonHelper.parse(requirement, Requirement.class, false);
    	BindingResult<ApplicationForm> formForm = JsonHelper.parse(formjson, ApplicationForm.class, false);
    	if(formForm.hasErrors() || reqForm.hasErrors()){
    		LOG.info("Form has errors: " + formForm.getErrors() + reqForm.getErrors());
    		return badRequest();
    	}

    	ApplicationForm applicationForm =formForm.get();

    	User user = daoProvider.userDao().findUniqueByEmail(applicationForm.applicant.email); //Unique email with applicantPassword is set
		if(user != null && user.applicantPassword != null){
			return badRequest();
		}

		user = daoProvider.userDao().findUniqueByMobile(applicationForm.applicant.phone1); //Unique mobileNumber with applicantPassword is set
		if(user != null && user.applicantPassword != null){
			return badRequest();
		}

		applicationForm.requirements = new ArrayList<Requirement>();

	    Requirement requirement1 = reqForm.get();
        JsonNode jsonAddress = formjson.get("locationsHiddenField");
        String address = jsonAddress.toString();
        ObjectMapper mapper = new ObjectMapper();
        try{
               JsonNode node = mapper.readTree(address);
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
               requirement1.requirementAddresses.addAll(listOfReqAdd);
        }catch (JsonProcessingException e) {
                       LOG.warn("Json exception", e);
               }catch (IOException e) {
                       LOG.warn("IO exception",e);
               }

        applicationForm.requirements.add(requirement1);


        Invoice invoice = new Invoice();
        invoice.amount = new BigDecimal(FORM_FEES);
        invoice.description = "Proposed Society Form Fee";
        invoice.invoiceNumber = "PSINV-" + System.currentTimeMillis();
        invoice.paid = false;
        LOG.info("creating invoice of amount for form : " + invoice.amount);
        applicationForm.invoice = invoice;
        applicationForm.status = FormStatus.INCOMPLETE;

        System.out.println("invoice========================= "+invoice.invoiceNumber);
        System.out.println("applicationForm========================= "+applicationForm);

        /*if(!utils.StringUtils.isTrivial(applicationForm.agentCode)){
        	Agent agent = agentDao.findByCode(applicationForm.agentCode);

            if(agent == null){
            	return badRequest();
            }else{
            	applicationForm.agent = agent;
            	 Ebean.save(applicationForm);
            	return ok(views.html.shortFormForms.getPaymentMode.render(applicationForm));
            }
        }*/

        Ebean.save(applicationForm);
        return ok();
//        JsonNode jsonData = Json.toJson(invoice);
//        return ok(Util.createResponse(jsonObject, true));
    }

	
}