package controllers;

import java.util.Collection;


import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;

import play.mvc.Controller;
import play.mvc.Result;
import staticdata.Pincodes;

import com.google.inject.Inject;

public class PincodeController extends Controller {
	private final Pincodes pincodes;

	@Inject
	public PincodeController(Pincodes pincodes) {
		super();
		this.pincodes = pincodes;
	}


	public Result getStates(){
		ArrayNode response =  JsonNodeFactory.instance.arrayNode();
		Collection<String> states = pincodes.getStates();
		for (String state : states) {
			response.add(state);
		}
		return ok(response);
	}

	public Result getDistricts(String state){
		ArrayNode response =  JsonNodeFactory.instance.arrayNode();
		Collection<String> districts = pincodes.getDistricts(state);
		for (String district : districts) {
			response.add(district);
		}
		return ok(response);
	}
	
	public Result getTalukas(String state, String district ){
		ArrayNode response =JsonNodeFactory.instance.arrayNode();
		Collection<String> talukas = pincodes.getTalukas(state, district);
		for(String taluka : talukas ){
			response.add(taluka);
		}
		return ok(response);
	}

	public Result getPincodes(String state, String district ,String taluka){
		ArrayNode response = JsonNodeFactory.instance.arrayNode();
		Collection<String> pinc = pincodes.getPincodes(state, district, taluka);		
		for (String pin : pinc) {
			response.add(pin);
		}
		return ok(response);
	}
} 