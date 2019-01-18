package models;
import java.util.Date;

import views.helpers.DateFormatHelper;
/**
 * 
 * @author TechBulls
 * whole entity is Transient , not saved in db 
 *
 */

public class PaymentReceipt {
    	private String applicantName;
    	private String applicationNumber;
    	private String receiptNumber;
    	private String modeOfPayment;
    	private String authorizationNumber;
    	private String amount ;
    	private String amountInWord ;
    	private String description;
    	private Date paymentDate;
    	
		public String getPaymentDate() {
			if(paymentDate == null) return "NA";
			return DateFormatHelper.formatDate("dd MMM yy",paymentDate);
		}
		public void setPaymentDate(Date paymentDate) {
			this.paymentDate = paymentDate;
		}
		public String getApplicantName() {
			return applicantName;
		}
		public void setApplicantName(String applicantName) {
			this.applicantName = applicantName;
		}
		
		public String getApplicationNumber() {
			return applicationNumber;
		}
		public void setApplicationNumber(String applicationNumber) {
			this.applicationNumber = applicationNumber;
		}
		
		public String getReceiptNumber() {
			return receiptNumber != null ? receiptNumber : "NA";
		}
		public void setReceiptNumber(String receiptNumber) {
			this.receiptNumber = receiptNumber;
		}
		
		public String getModeOfPayment() {
			return modeOfPayment;
		}
		
		public void setModeOfPayment(String modeOfPayment) {
			this.modeOfPayment = modeOfPayment;
		}
		public String getAuthorizationNumber() {
			return authorizationNumber != null ? authorizationNumber : "NA";
		}
		
		public void setAuthorizationNumber(String authorizationNumber) {
			this.authorizationNumber = authorizationNumber;
		}
		
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		
		public String getAmountInWord() {
			return amountInWord;
		}
		public void setAmountInWord(String amountInWord) {
			this.amountInWord = amountInWord;
		}
		
    }