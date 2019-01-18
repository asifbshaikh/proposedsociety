package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.codehaus.jackson.map.ObjectMapper;




import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Requirement extends Model {
	

	public interface Flat {}//Step2
	public interface Shop {}//Step3
	public interface Row {}//Step4
	public interface RowPlot {}//Step5
	public interface Chawl {}//Step6
	public interface Office {}//Step7
	public interface Studio {}//Step8
	public interface Hotel {}//Step9
	public interface Industry {}//Step10
	public interface Other {}//Step11 
	
	 @ManyToOne(cascade = {CascadeType.DETACH})
	 public ApplicationForm form;
	
	
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
    public Long id;
    
	
    @Required(groups = {Flat.class, Shop.class, Row.class, Chawl.class, Office.class, Studio.class, Hotel.class, Industry.class}, message = "Required Field!")
	public Integer builtupArea;		//textbox numeric value in sq.ft.
    
    @Required(groups = {Flat.class, Shop.class, Row.class, Chawl.class, Office.class, Studio.class, Industry.class}, message = "Required Field!")
	public Integer carpetArea;		//textbox numeric value in sq.ft.
	
    @Required(groups = {Flat.class, Row.class}, message = "Required Field!")
    public Integer choiceOfFloor;   //numeric value from 1 to 10
    
    //@Required(groups = {Flat.class, Chawl.class}, message = "Required Field!")
	public String subType;     //1BHK/2BHK/3BHK/4BHK/Other
    
    @Required(groups = {Row.class, Studio.class, Industry.class}, message = "Required Field!")
	public Integer gardenArea; 	//textbox numeric value	in sq.ft.
    
    @Required(groups = {Studio.class, Industry.class}, message = "Required Field!")
	public String gardenAreaRequired;		//radio button value
    
    @Required(groups = {Row.class, Studio.class}, message = "Required Field!")
	public Integer noOfBedrooms;      //numeric value from 1 to 10
    
    @Required(groups = {Flat.class, Row.class, Chawl.class, Office.class, Studio.class, Industry.class}, message = "Required Field!")
	public Integer noOfCommonToilets;      //numeric value from 1 to 10
    
    @Required(groups = {Hotel.class, Industry.class}, message = "Required Field!")
	public Integer noOfRoomsRequired;      //numeric value from 1 to 20 only for hotel and industry
    
    @Required(groups = {Flat.class, Row.class, Chawl.class, Office.class, Studio.class, Industry.class}, message = "Required Field!")
	public Integer noOfSeparateBathroom;      //numeric value from 1 to 10
    
    @Required(groups = {Flat.class, Row.class, Chawl.class, Office.class, Studio.class, Industry.class}, message = "Required Field!")
	public Integer noOfSeparateWc;      //numeric value from 1 to 10
    
    @Required(groups = {Shop.class, Row.class, Studio.class, Industry.class}, message = "Required Field!")
	public Integer openSpace;		//textbox numeric value in sq.ft.
    
    @Required(groups = {RowPlot.class, Office.class, Hotel.class, Industry.class}, message = "Required Field!")
	public Integer plotArea;		//textbox numeric value in sq.ft.
    
    @Required(groups = {RowPlot.class, Office.class, Hotel.class, Industry.class}, message = "Required Field!")
	public String plotAreaMeasure;    //drop Down for measuring unit selection
    
    @Required(groups = {Flat.class, Shop.class, Row.class, Chawl.class, Office.class, Studio.class, Industry.class}, message = "Required Field!")
	public Integer terraceArea;		//textbox numeric value in sq.ft.
	
    @Required(groups = {Shop.class}, message = "Required Field!")
    public Boolean terraceRequired; 	//Yes or No[i.e., true or false] Radio button in case of a shop
	
    @Required(groups = {Office.class, Studio.class, Industry.class}, message = "Required Field!")
	public String terraceChoice;		//Required or AsPerPlan radio Button in case of Office/Industry/Studio Apartment etc.
    
    @Required(groups = {Flat.class, Row.class, Chawl.class, Office.class, Studio.class, Industry.class}, message = "Required Field!")
	public String toiletWcBathRequirements;		//Required or AsPerPlan radio Button for most of the cases.
    
    @Required(groups = {RowPlot.class, Hotel.class}, message = "Required Field!")
	public String useOfLand;			//Some text
    
    //@Required(groups = {Shop.class, Row.class, RowPlot.class, Chawl.class, Office.class, Studio.class, Hotel.class, Industry.class}, message = "Required Field!")
	public String otherDetails;		//Some text details if any

										
										/*Uncommon Attributes*/
	//General Searching-property related
    
    public String requirementType;		//searching property type: res/comm/agri
    
    //Following three are temporarily not used. So hard coding them to null.
    	public String residentialTypeDetail = null;  //drop down selection like chawl/ row house etc.
		public String commercialTypeDetail = null;		//drop down selection like industrial area/hotel etc.
		public String agriculturalTypeDetail = null;	//drop down selection for agri options available.
    
    public String requirementSubType;
	
    @Required(groups = {Other.class}, message = "Required Field!")
	public String typeDetail;     //if subtype of searching property: res/comm/agri is selected as other
    
	//For Shop
    @Required(groups = {Shop.class}, message = "Required Field!")
    public Boolean specifyHeightRequired;      //Yes or No[i.e., true or false] Radio button in case of a shop
    
    @Required(groups = {Shop.class}, message = "Required Field!")
	public Integer height;		//numeric value for height of the shop in ft.
    
    @Required(groups = {Shop.class}, message = "Required Field!")
	public String powerConnectionType;		//textbox 'Specify Phase' dropDown value[Single Phase/Double Phase etc.,] in case of shop
    
    @Required(groups = {Shop.class}, message = "Required Field!")
	public String ifConnectionTypeOther;     //textbox if 'Specify Phase' dropDown value is selected as 'Other' in case of shop
    
    @Required(groups = {Shop.class}, message = "Required Field!")
	public Boolean isSeparateWaterConnectionRequired;		//is Separate water Connection required for shop[yes or no i.e., true or false]
    
    @Required(groups = {Shop.class}, message = "Required Field!")
	public Boolean isSeparatePowerConnectionRequired;		//is Separate Electrical Connection required for shop[yes or no i.e., true or false]
    
    @Required(groups = {Shop.class}, message = "Required Field!")
	public Boolean loftRequired;		//is loft area required for shop[yes or no i.e., true or false]
    
    @Required(groups = {Shop.class}, message = "Required Field!")
	public Integer loftArea;		//textbox numeric value in sq.ft.
    
    @Required(groups = {Shop.class}, message = "Required Field!")
	public Boolean otlaRequired;		//is loft area required for shop[yes or no i.e., true or false]
    
    @Required(groups = {Shop.class}, message = "Required Field!")
	public Integer otlaArea;		//textbox numeric value in sq.ft.
    
    @Required(groups = {Shop.class}, message = "Required Field!")
	public Integer noOfAttachedToilets;    //textbox for entering numeric value in case of shop
    
    @Required(groups = {Shop.class}, message = "Required Field!")
	public Integer noOfAttachedUrinals;		//textbox for entering numeric value in case of shop
    
    @Required(groups = {Shop.class}, message = "Required Field!")
	public Boolean openSpaceRequired;		//specify open space required for shop[yes or no i.e., true or false]
    
    @Required(groups = {Shop.class}, message = "Required Field!")
	public String toiletType;       //Common[In building area] or Attached:[Private toilets in your commercial area]
    
    @Required(groups = {Shop.class}, message = "Required Field!")
	public Integer waterPipeSize;		//Diameter of Water Pipe:[Inch]
    
	//For Chawl
    //@Required(groups = {Chawl.class}, message = "Required Field!")
    public String ifChawlTypeOther;     //textbox String value when [Type of Chawl Room] i.e chawl subtype is selected as 'Other' from dropDown
    
	//For Hotel
    @Required(groups = {Hotel.class}, message = "Required Field!")
    public Integer areaForEachRoom;		//textbox numeric value in sq.ft. For hotel
	
	//Location related 
    
    @Transient
    public String locationsHiddenField;
    @Transient
    public String nearbyHidden;
    @Transient
	public String externalAmenitiesHidden;
    @Transient
	public String internalAmenitiesHidden;
    
    @OneToMany(cascade =  { CascadeType.DETACH, CascadeType.PERSIST } ) public List<RequirementAddress> requirementAddresses;
    @OneToMany(cascade =  { CascadeType.DETACH, CascadeType.PERSIST } ) public List<Nearby> nearbys;
	@OneToMany(cascade =  { CascadeType.DETACH, CascadeType.PERSIST } ) public List<ExternalAmenity> externalAmenities;
    @OneToMany (cascade = { CascadeType.DETACH, CascadeType.PERSIST } ) public List<InternalAmenity> internalAmenities;
    
    
    public void setLocationsHiddenField (){
		if(this.requirementAddresses != null){
			this.locationsHiddenField = "";
		    final ObjectMapper mapper = new ObjectMapper();
		    try{
		    	this.locationsHiddenField = mapper.writer().writeValueAsString(this.requirementAddresses);
		    }catch(Exception e){
		    	System.out.println("JsonMappingException: "+e);
		    }
		    System.out.println(this.locationsHiddenField);
		}
	}
    
    public void setNearbyHiddenField() {
		if(this.nearbys != null){
			nearbyHidden = "";
			for(Nearby nrb : this.nearbys) {
				if(nrb.id != (this.nearbys.get(this.nearbys.size() - 1).id)){
					if(!nrb.locationNear.equals("Other")){
						nearbyHidden = nearbyHidden + nrb.locationNear + "|";
					}else{
						nearbyHidden = nearbyHidden + nrb.ifOtherText + "|";
					}
				}else{
					if(!nrb.locationNear.equals("Other")){
						nearbyHidden = nearbyHidden + nrb.locationNear;
					}else{
						nearbyHidden = nearbyHidden + nrb.ifOtherText;
					}
				}
	    	}
		}
    }
    public void setExternalAmenitiesHiddenField() {
		if(this.externalAmenities != null){
			externalAmenitiesHidden = "";
			for(ExternalAmenity extA : this.externalAmenities) {
				if(extA.id != (this.externalAmenities.get(this.externalAmenities.size() - 1).id)){
					if(!extA.externalAmenityName.equals("Other")){
						externalAmenitiesHidden = externalAmenitiesHidden + extA.externalAmenityName + "|";
					}else{
						externalAmenitiesHidden = externalAmenitiesHidden + extA.ifOtherText + "|";
					}
				}else{
					if(!extA.externalAmenityName.equals("Other")){
						externalAmenitiesHidden = externalAmenitiesHidden + extA.externalAmenityName;
					}else{
						externalAmenitiesHidden = externalAmenitiesHidden + extA.ifOtherText;
					}
				}
	    	}
		}
    }
    public void setInternalAmenitiesHiddenField() { 
		if(this.internalAmenities != null){
			internalAmenitiesHidden = "";
			for(InternalAmenity intA : this.internalAmenities) {
				if(intA.id != (this.internalAmenities.get(this.internalAmenities.size() - 1).id)){
					if(!intA.internalAmenityName.equals("Other")){
						internalAmenitiesHidden = internalAmenitiesHidden + intA.internalAmenityName + "|";
					}else{
						internalAmenitiesHidden = internalAmenitiesHidden + intA.ifOtherText + "|";
					}
				}else{
					if(!intA.internalAmenityName.equals("Other")){
						internalAmenitiesHidden = internalAmenitiesHidden + intA.internalAmenityName;
					}else{
						internalAmenitiesHidden = internalAmenitiesHidden + intA.ifOtherText;
					}
				}
	    	}
		}
	}
}