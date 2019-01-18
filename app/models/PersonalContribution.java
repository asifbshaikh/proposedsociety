package models;

import play.data.validation.Constraints;

public class PersonalContribution{
	
	 @Constraints.Required(message = "Required Field!")
	 public Integer   personalContribution;
	 public Integer cashWithYou;
	public PersonalContribution(){
		 
	 }
	public PersonalContribution(Integer personalContribution,Integer cashWithYou ){
		this.personalContribution = personalContribution;
		this.cashWithYou = cashWithYou;
	}
	 
}