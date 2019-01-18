package models;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.ebean.Model;

@Entity
public class Invoice extends Model {
    private static final long serialVersionUID = 1L;
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String invoiceNumber;
    public String description;
    public BigDecimal amount;
    public Boolean paid;
    public Long receiptId;
    
    @ManyToOne(cascade = {CascadeType.DETACH})
    public User user;
    
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "invoice")
    public List<Payment> payments;

    @Override
    public String toString() {
        return "Invoice [id=" + id + ", invoiceNumber=" + invoiceNumber + ", description=" + description + ", amount="
                + amount + ", paid=" + paid + ", user=" + user + "]";
    }
    
    public static String createInvoiceNumber(){
    	return "PSINV-" + System.currentTimeMillis() ;
    }
}
