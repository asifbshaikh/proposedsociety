package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.ApplicationForm.PostPayment;
import models.ApplicationForm.PrePayment;
import play.data.validation.Constraints;
import play.db.ebean.Model;
@Entity
public class Address extends Model {
	
		public Address(Address address) {//Copy Constructor
			// TODO Auto-generated constructor stub
			this.line1=address.line1;
			this.line2=address.line2;
			this.street=address.street;
			this.area=address.area;
			this.taluka=address.taluka;
			this.city=address.city;
			this.district=address.district;
			this.state=address.state;
			this.country=address.country;
			this.pin=address.pin;
		}
		public Address(){
			
			//Default Constructor
		}
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    @Constraints.Required(groups = { PrePayment.class ,PostPayment.class },message = "Required Field!") public String line1;
    public String line2;
    public String street;
    @Constraints.Required(groups = { PrePayment.class ,PostPayment.class },message = "Required Field!") public String area;
    public String taluka;
    @Constraints.Required(groups = { PrePayment.class ,PostPayment.class },message = "Required Field!") public String city;
    public String district;
    @Constraints.Required(groups = { PrePayment.class ,PostPayment.class },message = "Required Field!") public String state;
    @Constraints.Required(groups = { PrePayment.class ,PostPayment.class },message = "Required Field!") public String country;
    @Constraints.Required(groups = { PrePayment.class ,PostPayment.class },message = "Required Field!") public String pin;
}
