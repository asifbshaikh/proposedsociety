package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.db.ebean.Model;

@Entity
public class RequirementOldModel extends Model {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    public String type;
    public String residentialTypeDetail;
    public String commercialTypeDetail;
    public String agriculturalTypeDetail;

    // Common attributes
    public Integer carpet_area;
    public Integer builtup_area;
    public Boolean terrace_required;
    public Integer terrace_area;
    public String toiletWcBath_requirements;
    public Integer noOfCommonToilets;
    public Integer noOfSeparateWc;
    public Integer noOfSeparateBathroom;
    public String other_details;

    public Boolean open_space_required;
    public Integer open_space;
    public Integer noOfBedrooms;
    public Integer choiceOfFloor;
    public String gardenAreaRequired;
    public Integer gardenArea;

    public String subType;

    public Integer plotArea;
    public String plotAreaMeasure;
    public String useOfLand;

    public Integer noOfRoomsRequired;

    // Residential Flat / Apartment related attributes    

    // Commercial Property / Shop related attributes
    public Boolean loft_required;
    public Integer loft_area;
    public Boolean otla_required;
    public Integer otla_area;
    public Boolean specify_height_required;
    public Integer height;
    public String toiletType;
    public Integer noOfAttachedToilets;
    public Integer noOfAttachedUrinals;
    public Boolean isSeparateWaterConnectionRequired;
    public Integer waterPipeSize;
    public Boolean isSeparatePowerConnectionRequired;
    public String powerConnectionType;
    public String ifConnectionTypeOther;
    
    // Row House / Independent House related attributes

    // Plot related attributes
    
    // Chawl Room related attributes
    public String ifChawlTypeOther;

    // Hotel Plot / Industrial Land / Hotel Space / Hospital Space / Bank Space related attributes
    public Integer areaForEachRoom;
    
    public String city;
    public String taluka;
    public String district;
    public String state;
    public String country;
    public String villageOrDetailLocation;

    public String locationNear;
    
    @ManyToMany(cascade =  { CascadeType.DETACH, CascadeType.PERSIST } ) public List<ExternalAmenity> externalAmenities;
    @ManyToMany (cascade = { CascadeType.DETACH, CascadeType.PERSIST } ) public List<InternalAmenity> internalAmenities;
}
