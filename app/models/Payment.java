package models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class Payment extends Model {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Date startTime;
    public Date completionTime;
    public String transactionId;
    public String redirectUrl;
    public Boolean clientProcessed;
    public Boolean gatewayProcessed;
    public Boolean success;
    public String paymentMode ;
    public String bankName;
   
   
    
    

    // CCAvenue return values
    public String authDesc;
    public String nbBid;  //Bank Reference No
    public String nbOrderNo;
    
    @ManyToOne(cascade = { CascadeType.DETACH })
    public Invoice invoice;
    
    public boolean isSuccessful(){
    	return success != null ? success : false;
    }
}
