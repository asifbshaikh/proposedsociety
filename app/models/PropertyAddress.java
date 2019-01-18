package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class PropertyAddress extends Model {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    @Constraints.Required(message = "Required Field!") public String line1;
    @Constraints.Required(message = "Required Field!") public String city;
    public String taluka;
    public String district;
    @Constraints.Required(message = "Required Field!") public String state;
    @Constraints.Required(message = "Required Field!") public String country;
    @Constraints.Required(message = "Required Field!") public String pin;
}