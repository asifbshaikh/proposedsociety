package models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class Property extends Model {
    private static final Logger LOG = LoggerFactory.getLogger(Property.class);

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Constraints.Required(message = "Required Field!")
    public String type;
    @Constraints.Required(message = "Required Field!")
    public String typeDetail;

    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE })
    @Valid
    public PropertyAddress location;
    
    public String propertyDesc;
    
    @Constraints.Required(message = "Required Field!")
    public BigDecimal area;
    @Constraints.Required(message = "Required Field!")
    public Boolean loan;

    public Boolean monthlyAmntDedctedFrmSal;
    // @OneToOne (cascade = CascadeType.ALL)public LoanDetails loanDetails;

    @Constraints.Required(message = "Required Field!")
    public Boolean willingToSell;
    
    public Integer expectedPrice;
    public Integer allocation;

    // Loan Details
    public String financer;
    public Integer loanAmount;
    public Integer montlyEmi;
    public Integer installmentsPaid;
    public Integer balanceInstallments;
    public Integer balanceLoanAmount;

    @ManyToOne
    public ApplicationForm form;

    public Map<String, String> validate() {
        Map<String, String> errors = new HashMap<String, String>();

        if (loan != null && loan.equals(true)) {
            if (monthlyAmntDedctedFrmSal == null) {
                errors.put("monthlyAmntDedctedFrmSal", "Required Field");
            }
        }
        if (monthlyAmntDedctedFrmSal != null && monthlyAmntDedctedFrmSal.equals(false)) {
            if (financer == null || financer.isEmpty()) {
                errors.put("financer", "Required Field");
            }
            if (loanAmount == null) {
                errors.put("loanAmount", "Required Field");
            }
            if (montlyEmi == null) {
                errors.put("montlyEmi", "Required Field");
            }
            if (installmentsPaid == null) {
                errors.put("installmentsPaid", "Required Field");
            }
            if (balanceInstallments == null) {
                errors.put("balanceInstallments", "Required Field");
            }
            if (balanceLoanAmount == null) {
                errors.put("balanceLoanAmount", "Required Field");
            }
        }
        if (willingToSell != null && willingToSell.equals(true)) {
            if (expectedPrice == null) {
                errors.put("expectedPrice", "Required Field");
            }
            if (allocation == null) {
                errors.put("allocation", "Required Field");
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("print: " + errors.size() + " " + errors);
            LOG.debug("print: " + this);
            for (Map.Entry<String, String> entry : errors.entrySet()) {
                LOG.debug("entry.getKey() :" + entry.getKey() + "   entry.getValue() :" + entry.getValue());
            }
        }

        return errors;
    }

    @Override
    public String toString() {
        return "Property [type=" + type + ", typeDetail=" + typeDetail + ", area=" + area + ", loan=" + loan
                + ", monthlyAmntDedctedFrmSal=" + monthlyAmntDedctedFrmSal + ", willingToSell=" + willingToSell
                + ", financer=" + financer + "]";
    }
}
