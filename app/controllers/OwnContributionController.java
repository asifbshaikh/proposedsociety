package controllers;

import static common.ApplicationConstants.SESSION_KEY_USER;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;

import json.BindingResult;
import json.JsonHelper;
import models.Address;
import models.PersonalContribution;
import models.ApplicationForm;
import models.BankAccount;
import models.FixedDeposit;
import models.Insurance;
import models.MiscBorrowing;
import models.Property;
import models.RecurringDeposit;
import models.User;
import models.dao.BankAccountDao;
import models.dao.DaoProvider;
import models.dao.FDDetailsDao;
import models.dao.InsuranceDao;
import models.dao.MiscBorrowingDao;
import models.dao.PropertyDao;
import models.dao.RDDetailsDao;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import play.data.Form;
import play.data.validation.Constraints.Pattern;
import play.data.validation.Constraints.Required;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import security.auth.VisitorAuthenticator;
import validation.PSForm;
import views.html.*;

import com.avaje.ebean.Ebean;
import com.google.inject.Inject;
import common.RestUtils;

@Authenticated(value = VisitorAuthenticator.class)
public class OwnContributionController extends AuthenticatedUserController {
    private static final String LOANTAKEN = "true";
    private static final String TRUEVAL = "true";
    private static final String FALSEVAL = "false";
    private static final String ASLOAN = "asLoan";
    private static final String ASDONATION = "asDonation";
    private static final String MONEYOWED = "moneyOwed";
    
    private final BankAccountDao bankAccountDao;
    private final FDDetailsDao fdDetailesDao;
    private final RDDetailsDao rdDetailesDao;
    private final InsuranceDao insuranceDao;
    private final MiscBorrowingDao miscBorrowingDao;
    private final PropertyDao propertyDao;

    
    @Inject
    public OwnContributionController(DaoProvider provider) {
		super(provider.userDao());
		this.bankAccountDao = provider.bankAccountDao();
		this.fdDetailesDao=provider.fdDetailsDao();
		this.rdDetailesDao=provider.rdDetailsDao();
		this.insuranceDao=provider.insuranceDao();
		this.miscBorrowingDao=provider.miscBorrowingDao();
		this.propertyDao = provider.propertyDao();
	}
    
    public Result personalContribution() {
    	Form<PersonalContribution> persnalContri = Form.form(PersonalContribution.class).bindFromRequest();
    	User user = currentUser();
    	if (persnalContri.hasErrors()) {
            return badRequest("badrequest"+persnalContri.hasErrors());
        }
    	if (user.form != null) {
    		PersonalContribution fieldVal = persnalContri.get();
    		user.form.cashWithYou=fieldVal.cashWithYou;
    		user.form.personalContribution = fieldVal.personalContribution;
            userDao.save(user);
         }
         return redirect(routes.ApplicationFormController.closingFinishDetails());
    }
    
	public static final class BankAccountDetails {
        //Loan Details
        @Required(message = "Required Field!") public String bankName;
        @Required(message = "Required Field!") public String accountNumber;
        @Required(message = "Required Field!") public String anyDedFromBankAccount;
    
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String balance;

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String allocateToBuy;
        
        public BankAccountDeduction deductions;
        
        public BankAccountLoanDeduction loanTaken;

         public void populate(BankAccount bnkA) {
            /*try
            {*/
				if(!allocateToBuy.isEmpty()){bnkA.allocateToBuy = Integer.parseInt(allocateToBuy);}
				if(anyDedFromBankAccount != null){bnkA.anyDedFromBankAccount = Boolean.valueOf(anyDedFromBankAccount);}
				bnkA.bankName = bankName;
				bnkA.accountNumber = accountNumber;
				if(!balance.isEmpty()){bnkA.balance = Integer.parseInt(balance);}
            /*}
            catch (NumberFormatException nfex)
            {
               System.out.print("exception: "+nfex);
            }*/
        }

    }
    
    public static final class BankAccountDeduction {
    	
    	@Required(message = "Required Field!") public String loan;
    	
    	//@Required(message = "Required Field!")
    	@Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String otherMonthlyDeduction;
        
        //@Required(message = "Required Field!")
    	@Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String totalMonthlyDeduction;
        
    	public void populate(BankAccount bnkA) {
            /*try
            {*/
            	if(loan != null){bnkA.loan = Boolean.valueOf(loan);}
            	if(!otherMonthlyDeduction.isEmpty()){bnkA.otherMonthlyDeduction = Integer.parseInt(otherMonthlyDeduction);}
            	if(!totalMonthlyDeduction.isEmpty()){bnkA.totalMonthlyDeduction = Integer.parseInt(totalMonthlyDeduction);}
            /*}
            catch (NumberFormatException nfex)
            {
               System.out.print("exception: "+nfex);
            }*/
        }
    }
    
    public static final class BankAccountLoanDeduction {
    	
    	@Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String loanEmi;
       
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String balanceInstallments;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String balanceLoanAmount;
        
    	public void populate(BankAccount bnkA) {
            /*try
            {*/
    			if(!loanEmi.isEmpty()){bnkA.loanEmi = Integer.parseInt(loanEmi);}
    			if(!balanceInstallments.isEmpty()){bnkA.balanceInstallments = Integer.parseInt(balanceInstallments);}
    			if(!balanceLoanAmount.isEmpty()){bnkA.balanceLoanAmount = Integer.parseInt(balanceLoanAmount);}
            /*}
            catch (NumberFormatException nfex)
            {
               System.out.print("exception: "+nfex);
            }*/
        }
    }
    
    //temp bean for avoiding validation errors
    public static final class FixedDepositDetailsDedFrmSal {
        @Required(message = "Required Field!") public String monthlyAmntDedctedFrmSal;
        
        public void populate(FixedDeposit fxd) {
            try
            {
            	if(monthlyAmntDedctedFrmSal != null){fxd.monthlyAmntDedctedFrmSal = Boolean.valueOf(monthlyAmntDedctedFrmSal);}
            }
            catch (NumberFormatException nfex)
            {
               System.out.print("exception: "+nfex);
            }
        }
    }
    
    public static final class FixedDepositDetailsWillSurrender {
    	@Required(message = "Required Field!") public String willSurrender;
    	public void populate(FixedDeposit fxd) {
            try
            {
            	if(willSurrender != null){fxd.willSurrender = Boolean.valueOf(willSurrender);}
            }
            catch (NumberFormatException nfex)
            {
               System.out.print("exception: "+nfex);
            }
        }
    }
    
    public static final class FixedDepositDetailsUseAmnt {
    	@Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String allocation;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String amountCanAvail;
        
        public void populate(FixedDeposit fxd) {
            try
            {
            	if(!allocation.isEmpty()){fxd.allocation = Integer.parseInt(allocation);}
            	if(!amountCanAvail.isEmpty()){fxd.amountCanAvail = Integer.parseInt(amountCanAvail);}
            }
            catch (NumberFormatException nfex)
            {
               System.out.print("exception: "+nfex);
            }
        }
    }
    
    public static final class FixedDepositDetails {

        @Required(message = "Required Field!") public String bankName;
        @Required(message = "Required Field!") public String nameOfHolder;
        @Required(message = "Required Field!") public Date startDate;
        @Required(message = "Required Field!") public Date maturityDate;
        @Required(message = "Required Field!") public String haveTakenLoanOnFD;
        //@Required(message = "Required Field!") public String monthlyAmntDedctedFrmSal;
        //@Required(message = "Required Field!") public String willSurrender;

        public FDLoanDetails loanDetails;
        
        public FixedDepositDetailsDedFrmSal fdDedFrmSal;
        
        public FixedDepositDetailsWillSurrender fdWillSurrender;
        
        public FixedDepositDetailsUseAmnt fdUseAmnt;

        //FD vars continued
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String pricipal;
        
        @Required(message = "Required Field!")
        @Pattern(value = "^[0-9]{1,3}(\\.[0-9]{1,2})?$", message = "decimal fraction with max. 3 digits before and max. 2 digits after period is allowed.")
        public String interestRate;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String maturityAmount;
        
        /*@Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String allocation;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String amountCanAvail;*/

        public void populate(FixedDeposit fxd) {
            try
            {
            	if(!pricipal.isEmpty()){fxd.pricipal = Integer.parseInt(pricipal);}
            	if(!interestRate.isEmpty()){fxd.interestRate = new BigDecimal(interestRate);}
                if(!maturityAmount.isEmpty()){fxd.maturityAmount = Integer.parseInt(maturityAmount);}
                /*fxd.allocation = Integer.parseInt(allocation);
                fxd.amountCanAvail = Integer.parseInt(amountCanAvail);*/
            
                if(haveTakenLoanOnFD != null){fxd.haveTakenLoanOnFD = Boolean.valueOf(haveTakenLoanOnFD);}
                /*fxd.monthlyAmntDedctedFrmSal = Boolean.valueOf(monthlyAmntDedctedFrmSal);
                fxd.willSurrender = Boolean.valueOf(willSurrender);*/

                fxd.bankName = bankName;
                fxd.nameOfHolder = nameOfHolder;
                fxd.startDate = startDate;
                fxd.maturityDate = maturityDate;
            }
            catch (NumberFormatException nfex)
            {
               System.out.print("exception: "+nfex);
            }
        }

    }
    public static final class RecurringDepWillSurrenderNo {
    	@Required(message = "Required Field!") public String monthlyAmntDedctedFrmSal;
    	public void populate(RecurringDeposit rd) {
            /*try
            {*/
            	if(monthlyAmntDedctedFrmSal != null){rd.monthlyAmntDedctedFrmSal = Boolean.valueOf(monthlyAmntDedctedFrmSal);}
            /*}
            catch (NumberFormatException nfex)
            {
               System.out.print("exception: "+nfex);
            }*/
        }
    	
    }
    
    public static final class RecurringDepWillSurrenderYes {
    	@Required(message = "Required Field!")
    	@Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String amountCanAvail;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String allocation;
        
        public void populate(RecurringDeposit rd) {
            /*try
            {*/
            	if(!amountCanAvail.isEmpty()){rd.amountCanAvail = Integer.parseInt(amountCanAvail);}
            	if(!allocation.isEmpty()){rd.allocation = Integer.parseInt(allocation);}
            /*}
            catch (NumberFormatException nfex)
            {
               System.out.print("exception: "+nfex);
            }*/
        }
    }
    
    public static final class RecurringDepNoCuttingFrmSal {
	    @Required(message = "Required Field!")
	    @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
	    public String monthlyInstallments;
	
	    @Required(message = "Required Field!")
	    @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
	    public String balanceInstallments;
	
	    @Required(message = "Required Field!")
	    @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
	    public String balanceDedAmount;
	    
	    public void populate(RecurringDeposit rd) {
            /*try
            {*/
            	if(!monthlyInstallments.isEmpty()){rd.monthlyInstallments = Integer.parseInt(monthlyInstallments);}
            	if(!balanceInstallments.isEmpty()){rd.balanceInstallments = Integer.parseInt(balanceInstallments);}
            	if(!balanceDedAmount.isEmpty()){rd.balanceDedAmount = Integer.parseInt(balanceDedAmount);}
            /*}
            catch (NumberFormatException nfex)
            {
               System.out.print("exception: "+nfex);
            }*/
        }
	    
    }
    
    public static final class RecurringDep {

        @Required(message = "Required Field!") public String nameOfHolder;
        @Required(message = "Required Field!") public String bankName;
        @Required(message = "Required Field!") public Date rdstartDate;
        @Required(message = "Required Field!") public Date rdmaturityDate;
        @Required(message = "Required Field!") public String willSurrender;
        //@Required(message = "Required Field!") public String monthlyAmntDedctedFrmSal;

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String installment;

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String installmentsPaid;

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String installmentsBalance;

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String principal;

        @Required(message = "Required Field!")
        @Pattern(value = "^[0-9]{1,3}(\\.[0-9]{1,2})?$", message = "decimal fraction with max. 3 digits before and max. 2 digits after period is allowed.")
        public String interestRate;

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String maturityAmount;
        
		public RecurringDepWillSurrenderNo rdWillSurrenderNo;
		public RecurringDepNoCuttingFrmSal rdNoCuttingFrmSal;
		public RecurringDepWillSurrenderYes rdWillSurrenderYes;

        /*@Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String amountCanAvail;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String allocation;

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String monthlyInstallments;

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String balanceInstallments;

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String balanceDedAmount;*/

        public void populate(RecurringDeposit rcd) {
            /*try
            {*/
            	if(!installment.isEmpty()){rcd.installment = Integer.parseInt(installment);}
            	if(!installmentsPaid.isEmpty()){rcd.installmentsPaid = Integer.parseInt(installmentsPaid);}
            	if(!installmentsBalance.isEmpty()){rcd.installmentsBalance = Integer.parseInt(installmentsBalance);}
            	if(!principal.isEmpty()){rcd.principal = Integer.parseInt(principal);}
            	if(!interestRate.isEmpty()){rcd.interestRate = new BigDecimal(interestRate);}
            	if(!maturityAmount.isEmpty()){rcd.maturityAmount = Integer.parseInt(maturityAmount);}
               /* rcd.allocation = Integer.parseInt(allocation);

                rcd.monthlyAmntDedctedFrmSal = Boolean.valueOf(monthlyAmntDedctedFrmSal);
                rcd.monthlyInstallments = Integer.parseInt(monthlyInstallments);
                rcd.balanceInstallments = Integer.parseInt(balanceInstallments);
                rcd.balanceDedAmount = Integer.parseInt(balanceDedAmount);
                rcd.amountCanAvail = Integer.parseInt(amountCanAvail);*/
            	if(willSurrender != null){rcd.willSurrender = Boolean.valueOf(willSurrender);}
                
                rcd.bankName = bankName;
                rcd.nameOfHolder = nameOfHolder;
                rcd.startDate = rdstartDate;
                rcd.maturityDate = rdmaturityDate;
            /*}
            catch (NumberFormatException nfex)
            {
               System.out.print("exception: "+nfex);
            }*/
        }

    }
    
    public static final class InsuranceDetailsAmountUsage {
    	@Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String allocation;
    	
    	public void populate(Insurance ins) {
            try
            {
                ins.allocation = Integer.parseInt(allocation);
            }
            catch (NumberFormatException nfex)
            {
               System.out.print("exception: "+nfex);
            }
        }
    }

    public static final class InsuranceDetails {    

        @Required(message = "Required Field!") 
        public String nameOfHolder;

        @Required(message = "Required Field!")
        public String insurerName;

        @Required(message = "Required Field!")
        public Date insuStartDate;

        @Required(message = "Required Field!")
        public Date insuMaturityDate;

        @Required(message = "Required Field!")
        public String premiumFreqency;

        @Required(message = "Required Field!")
        public Date insuLastPaymentDate;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.") 
        public String sumAssured;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.") 
        public String premium;

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.") 
        public String premiumsBalance;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.") 
        public String premiumsPaid;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.") 
        public String maturityAmount;

        @Required(message = "Required Field!")
        public String monthlyAmntDedctedFrmSal;

        /*@Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String allocation;*/

        @Required(message = "Required Field!")
        public String receivingAnyMaturedAmount;
        
        public InsuranceDetailsAmountUsage insuAmountUsage;
        
        public void populate(Insurance ins) {
            try
            {
                ins.sumAssured = Integer.parseInt(sumAssured);
                ins.premium = Integer.parseInt(premium);
                ins.premiumsBalance = Integer.parseInt(premiumsBalance);
                ins.premiumsPaid = Integer.parseInt(premiumsPaid);
                ins.maturityAmount = Integer.parseInt(maturityAmount);
                ins.monthlyAmntDedctedFrmSal = Boolean.valueOf(monthlyAmntDedctedFrmSal);
                ins.receivingAnyMaturedAmount = Boolean.valueOf(receivingAnyMaturedAmount);
                //ins.allocation = Integer.parseInt(allocation);
            
                ins.nameOfHolder = nameOfHolder;
                ins.insurerName = insurerName;
                ins.startDate = insuStartDate;
                ins.maturityDate = insuMaturityDate;
                ins.premiumFreqency = premiumFreqency;
                ins.lastPaymentDate = insuLastPaymentDate;
            }
            catch (NumberFormatException nfex)
            {
               System.out.print("exception: "+nfex);
            }
        }

    }

    public static final class FDLoanDetails {
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String loanAmount;

        public String financer;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String montlyEmi;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String installmentsPaid;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String balanceInstallments;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String balanceLoanAmount;

         public void populate(FixedDeposit fxd) {
            try
            {
            	fxd.financer = "";
            	fxd.loanAmount = Integer.parseInt(loanAmount);
            	fxd.montlyEmi = Integer.parseInt(montlyEmi);
            	fxd.installmentsPaid = Integer.parseInt(installmentsPaid);
            	fxd.balanceInstallments = Integer.parseInt(balanceInstallments);
            	fxd.balanceLoanAmount = Integer.parseInt(balanceLoanAmount);
            }
            catch (NumberFormatException nfex)
            {
               System.out.print("exception: "+nfex);
            }
        }

    }

    public static final class AmountFromFriendsAmountNature {
    	@Required(message = "Required Field!")
        public String amountNature;
    	
    	public void populate(MiscBorrowing mb) {
            try
            {
                mb.type = amountNature;
            }
            catch (NumberFormatException nfex)
            {
               System.out.println("exception: "+nfex);
            }
        }
    }
    
    public static final class AmountFromFriends {

    	@Required(message = "Required Field!")
        public String isAmountFromFriends;
    	
    	public AmountFromFriendsAmountNature AmntNtr;
        
        public AmountFromFriendsAsLoan loanFields;
        
        public AmountFromFriendsAsDonation donationFields;
        
        public AmountFromFriendsAsRepayment moneyOwedFields;

    }

    public static final class AmountFromFriendsAsLoan {

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String amount;

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String numberOfYears;
        
        @Required(message = "Required Field!")
        @Pattern(value = "^[0-9]{1,3}(\\.[0-9]{1,2})?$", message = "decimal fraction with max. 3 digits before and max. 2 digits after period is allowed.")
        public String interestRate;

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String emi;

        public void populate(MiscBorrowing mb) {
            try
            {
               mb.amount = Integer.parseInt(amount);
               mb.numberOfYears = Integer.parseInt(numberOfYears);
               mb.interestRate = new BigDecimal(interestRate);
               mb.emi = Integer.parseInt(emi);
            }
            catch (NumberFormatException nfex)
            {
               System.out.println("exception: "+nfex);
            }
        }
    }

    public static final class AmountFromFriendsAsDonation {

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String donation;    

        public void populate(MiscBorrowing mb) {
            try
            {
               mb.amount = Integer.parseInt(donation);
            }
            catch (NumberFormatException nfex)
            {
               System.out.println("exception: "+nfex);
            }
        }
    }

    public static final class AmountFromFriendsAsRepayment {

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String repayment;    

        public void populate(MiscBorrowing mb) {
            try
            {
               mb.amount = Integer.parseInt(repayment);
            }
            catch (NumberFormatException nfex)
            {
               System.out.println("exception: "+nfex);
            }
        }
    }
    
    public static final class HaveIncomeFromeProperty {
    	@Required(message = "Required Field!") public String IncomeFrmExistingProperty;
    }
    
    public static final class PropertyTypeDetails {
    	@Required(message = "Required Field!")
        public String type;
    	 public void populate(Property prp) {
             try
             {
                 prp.type = type;
             }
             catch (NumberFormatException nfex)
             {
                System.out.println("exception: "+nfex);
             }
         }
    }
    
    public static final class PropertyLoanMnthlyDedFrmSal {
    	 @Required(message = "Required Field!")
         public String monthlyAmntDedctedFrmSal;
    	 public void populate(Property prp) {
             try
             {
            	 prp.monthlyAmntDedctedFrmSal = Boolean.valueOf(monthlyAmntDedctedFrmSal);
             }
             catch (NumberFormatException nfex)
             {
                System.out.println("exception: "+nfex);
             }
         }
    }
    
    public static final class PropertySellToBuyAnother {
    	
    	@Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String expectedPrice;

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String allocation;
        
   	 	public void populate(Property prp) {
            try
            {
            	prp.expectedPrice = Integer.parseInt(expectedPrice);
                prp.allocation = Integer.parseInt(allocation);
            }
            catch (NumberFormatException nfex)
            {
               System.out.println("exception: "+nfex);
            }
        }
   }
    
    public static final class PropertyDetails {    

        @Required(message = "Required Field!")        
        public String typeDetail;

        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String area;

        @Required(message = "Required Field!")
        public String loan;

        @Required(message = "Required Field!")
        public String willingToSell;
        
        public HaveIncomeFromeProperty haveIncome;
        
        public PropertyTypeDetails propType;
        
        public PropertyAddresses propAdd;
        
        public PropertyLoanMnthlyDedFrmSal propDedFromSal;
        
        public PropertySellToBuyAnother propSell;
        
        public GenLoanDetails propLoan;

        public void populate(Property prp) {
            try
            {
                prp.typeDetail = typeDetail;
                prp.area = new BigDecimal(area);
                prp.loan = Boolean.valueOf(loan);
                prp.willingToSell = Boolean.valueOf(willingToSell);

            }
            catch (NumberFormatException nfex)
            {
               System.out.println("exception: "+nfex);
            }
        }

    }

    public static final class PropertyAddresses {
        // Address Details
    	@Required(message = "Line1 is required.") public String line1;
         public String line2 = "";
         public String street = "";
         @Required(message = "City is required.") public String city;
         @Required (message = "Taluka is required.") public String taluka;
         @Required(message = "District is required.") public String district;
         @Required (message = "State is required.") public String state;
         @Required (message = "Country is required.") public String country;
         @Required (message = "PIN is required.") public String pin;
         
         public void populate(Address a) {
            a.line1 = line1;
            a.line2 = line2;
            a.street = street;
            a.city = city;
            a.taluka = taluka;
            a.district =  district;
            a.state = state;
            a.country = country;
            a.pin = pin;
        }

    }
    
    public static final class GenLoanDetails {
        //Property Loan Details
        @Required(message = "Required Field!") public String financer;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String loanAmount;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String montlyEmi;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String installmentsPaid;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String balanceInstallments;
        
        @Required(message = "Required Field!")
        @Pattern(value = "[0-9]*", message = "please, insert numeric value.")
        public String balanceLoanAmount;

         public void populate(Property ld) {
            try
            {
                ld.loanAmount = Integer.parseInt(loanAmount);
                ld.montlyEmi = Integer.parseInt(montlyEmi);
                ld.installmentsPaid = Integer.parseInt(installmentsPaid);
                ld.balanceInstallments = Integer.parseInt(balanceInstallments);
                ld.balanceLoanAmount = Integer.parseInt(balanceLoanAmount);
                ld.financer = financer;
            }
            catch (NumberFormatException nfex)
            {
               System.out.println("exception: "+nfex);
            }
        }

    }
    
    public Result bankAccountDetails() {
        Form<BankAccountDetails> bAForm = Form.form(BankAccountDetails.class);
        Form<BankAccountDeduction> bad = Form.form(BankAccountDeduction.class);
        Form<BankAccountLoanDeduction> bald = Form.form(BankAccountLoanDeduction.class);
        return ok(bankAccountDetails.render(bAForm,bad,bald));
        //return ok("Go home! Live in peace!!2");
    }
    
    public Result bankAccountDetailsSubmit() {
        Form<BankAccountDetails> bAForm = Form.form(BankAccountDetails.class).bindFromRequest();
        Form<BankAccountDeduction> bad = Form.form(BankAccountDeduction.class).bindFromRequest();
        Form<BankAccountLoanDeduction> bald = Form.form(BankAccountLoanDeduction.class).bindFromRequest();
        
        if (bAForm.hasErrors()) {
            System.out.print("BankAccountDetails Has errors");
            return badRequest(bankAccountDetails.render(bAForm,bad,bald));
            //return ok("Go home! Live in peace!!3");
        }
        BankAccountDetails bA = bAForm.get();
        
        System.out.println("bA.deductions.loan: "+bA.deductions.loan);
        
        if(bA.deductions.loan.equals(TRUEVAL) && bald.hasErrors()){
        	System.out.print("BankAccountDetails Has errors");
            return badRequest(bankAccountDetails.render(bAForm,bad,bald));
        }
        
        /*BankAccount banAk = new BankAccount();
        bA.populate(banAk);
        
        OwnContribution oc = new OwnContribution();
        oc.bank_accts = new ArrayList<BankAccount>();
        oc.bank_accts.add(banAk);
        Ebean.save(oc);*/

        return redirect(routes.OwnContributionController.fixedDepositDetails());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result addSavingAccount() {
        JsonNode json = request().body().asJson();
        BankAccountDetails bnkAccBean = Json.fromJson(json, BankAccountDetails.class);
        
        javax.validation.Validator validator = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<BankAccountDetails>> violations = validator.validate(bnkAccBean);
        System.out.println("Errors: " + violations);

        javax.validation.Validator validatorForDeductions = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<BankAccountDeduction>> violationsForDeductions = validatorForDeductions.validate(bnkAccBean.deductions);
        System.out.println("Errors(Saving-Account: violationsForDeductions): " + violationsForDeductions);
        
        javax.validation.Validator validatorForLoanTaken = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<BankAccountLoanDeduction>> violationsForLoanTaken = validatorForLoanTaken.validate(bnkAccBean.loanTaken);
        System.out.println("Errors(Saving-Account: violationsForLoanTaken): " + violationsForLoanTaken);
        
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        
        if((!violations.isEmpty()) || (!violationsForDeductions.isEmpty() && !FALSEVAL.equals(bnkAccBean.anyDedFromBankAccount)) || (!violationsForLoanTaken.isEmpty()  && !FALSEVAL.equals(bnkAccBean.deductions.loan))){
        	if (!violations.isEmpty()) {
	        	response.put("BankAccount",RestUtils.createFailureResponse(violations));
        	}
        	if(bnkAccBean.anyDedFromBankAccount != null){
		        if (bnkAccBean.anyDedFromBankAccount.equals(TRUEVAL) && !violationsForDeductions.isEmpty()) {
		        	response.put("BankAccountDeductions",RestUtils.createFailureResponse(violationsForDeductions));
		        }else if (bnkAccBean.anyDedFromBankAccount.equals(TRUEVAL) && violationsForDeductions.isEmpty()){
		        	if(bnkAccBean.deductions.loan != null){
			        	if (bnkAccBean.deductions.loan.equals(TRUEVAL) && !violationsForLoanTaken.isEmpty()) {
				        	response.put("BankAccountLoan",RestUtils.createFailureResponse(violationsForLoanTaken));
				        }
		        	}
		        }
        	}
        	if(response.size() != 0){
        		return badRequest(response);
        	}
        }
        //BankAccount bnkAt = Json.fromJson(json, BankAccount.class);
        String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        if(user != null){
            if(user.form == null) {
                user.form = new ApplicationForm();
                user.form.bankAccounts = new ArrayList<BankAccount>();
            } else if (user.form.bankAccounts == null) {
                user.form.bankAccounts = new ArrayList<BankAccount>();
            }
            BankAccount baccnt = new BankAccount();
            bnkAccBean.deductions.populate(baccnt);
            bnkAccBean.loanTaken.populate(baccnt);
            bnkAccBean.populate(baccnt);
            user.form.bankAccounts.add(baccnt);
            
            Ebean.save(user.form);
            Ebean.save(user);

            return ok("Saved Json");
            //return redirect(routes.OwnContributionController.bank_accts());
            //return ok(RestUtils.createSuccessResponse());
        }        
        
        return ok(RestUtils.createFailureResponse());
    }   

    public Result getBankAccountDetails() {
        try{
            String email = session(SESSION_KEY_USER);
            User user = userDao.findUniqueByEmail(email);
            /*if(user == null){
                System.out.println("user = null");
            }else{
                System.out.println("user != null");
                if(user.form == null){ 
                    System.out.println("user.form = null");
                }else{
                    System.out.println("user.form != null");
                    if(user.form.own_contribution == null){
                        System.out.println("user.form = null");
                    }else{
                        System.out.println("user.form.own_contribution != null");
                        if(user.form.own_contribution.bank_accts == null){
                            System.out.println("user.form.own_contribution.bank_accts = null");
                        }else{
                            System.out.println("user.form.own_contribution.bank_accts != null");
                        }
                    }
                }
            }*/ 

            if(user != null && user.form != null && user.form.bankAccounts != null){
                        return ok(getBankAccountDetails.render(user.form.bankAccounts));
            }
        }
        catch (NullPointerException e) {
            System.err.println("Caught NullPointerException: " + e.getMessage());
        }
        //return badRequest(Json.stringify(RestUtils.createFailureResponse()));
        return ok("something went wrong!!!");
    }
    public Result fixedDepositDetails() {
        Form<FixedDepositDetails> fd = Form.form(FixedDepositDetails.class);
        Form<FixedDepositDetailsDedFrmSal> dedFrmSal = Form.form(FixedDepositDetailsDedFrmSal.class);
        Form<FixedDepositDetailsWillSurrender> will = Form.form(FixedDepositDetailsWillSurrender.class);
        Form<FixedDepositDetailsUseAmnt> useAmnt = Form.form(FixedDepositDetailsUseAmnt.class);
        Form<FDLoanDetails> lonDtls = Form.form(FDLoanDetails.class);
        
        return ok(fixedDepositDetails.render(fd, dedFrmSal, will, useAmnt, lonDtls));
    }
    
    public Result fixedDepositDetailsSubmit() {
        Form<FixedDepositDetails> fd = Form.form(FixedDepositDetails.class).bindFromRequest();
        Form<FixedDepositDetailsDedFrmSal> dedFrmSal = Form.form(FixedDepositDetailsDedFrmSal.class).bindFromRequest();
        Form<FixedDepositDetailsWillSurrender> will = Form.form(FixedDepositDetailsWillSurrender.class).bindFromRequest();
        Form<FixedDepositDetailsUseAmnt> useAmnt = Form.form(FixedDepositDetailsUseAmnt.class).bindFromRequest();
        Form<FDLoanDetails> lonDtls = Form.form(FDLoanDetails.class).bindFromRequest();

        String TRUESTRING = "true";
        if (fd.hasErrors()) {
            System.out.print("FixedDepositDetails Has errors");
            return badRequest(fixedDepositDetails.render(fd, dedFrmSal, will, useAmnt, lonDtls));
        }
        FixedDepositDetails fDep = fd.get();
        
        /*FixedDeposit fixedDep = new FixedDeposit();
        fDep.populate(fixedDep);*/
        
        if (LOANTAKEN.equals(fDep.haveTakenLoanOnFD) && dedFrmSal.hasErrors()) {
            System.out.println("dedFrmSal.hasErrors flag: "+dedFrmSal.hasErrors());
            return badRequest(fixedDepositDetails.render(fd, dedFrmSal, will, useAmnt, lonDtls));
        }
        if (LOANTAKEN.equals(fDep.haveTakenLoanOnFD) && !dedFrmSal.hasErrors()) {
        	FixedDepositDetailsDedFrmSal dedctn = dedFrmSal.get();
        
	        if (TRUESTRING.equals(dedctn.monthlyAmntDedctedFrmSal) && will.hasErrors()) {
	            System.out.println("will.hasErrors flag: "+will.hasErrors());
	            return badRequest(fixedDepositDetails.render(fd, dedFrmSal, will, useAmnt, lonDtls));
	        }
	        
	        if ((!TRUESTRING.equals(dedctn.monthlyAmntDedctedFrmSal)) && (lonDtls.hasErrors() || will.hasErrors())) {
	            System.out.println("will.hasErrors flag: "+will.hasErrors());
	            System.out.println("lonDtls.hasErrors flag: "+lonDtls.hasErrors());
	            return badRequest(fixedDepositDetails.render(fd, dedFrmSal, will, useAmnt, lonDtls));
	        }
	        
	        FixedDepositDetailsWillSurrender wills = will.get();
	        
	        if (TRUESTRING.equals(wills.willSurrender) && useAmnt.hasErrors()) {
	            System.out.println("useAmnt.hasErrors flag: "+useAmnt.hasErrors());
	            return badRequest(fixedDepositDetails.render(fd, dedFrmSal, will, useAmnt, lonDtls));
	        }
        }
        
        /*if (LOANTAKEN.equals(fDep.haveTakenLoanOnFD) && !lonDtls.hasErrors()) {
            System.out.print("lonDtls.hasErrors flag: "+lonDtls.hasErrors());
            FDLoanDetails lndt = lonDtls.get();
            LoanDetails lds = new LoanDetails();
            lndt.populate(lds);
            Ebean.save(lds);
        }

        OwnContribution oc = new OwnContribution();
        oc.fds = new ArrayList<FixedDeposit>();
        oc.fds.add(fixedDep);
        Ebean.save(oc);*/

        return redirect(routes.OwnContributionController.recurringDep());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result addFDDetails() {
        JsonNode json = request().body().asJson();
        FixedDepositDetails fxdpst = Json.fromJson(json, FixedDepositDetails.class);

        javax.validation.Validator validator = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<FixedDepositDetails>> violations = validator.validate(fxdpst);
        System.out.println("Errors(fd violations): " + violations);
        
        javax.validation.Validator validatorForLoan = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<FDLoanDetails>> violationsForLoan = validatorForLoan.validate(fxdpst.loanDetails);
        System.out.println("Errors(fd violationsForLoan): " + violationsForLoan);
        
        javax.validation.Validator validatorForDedFrmSal = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<FixedDepositDetailsDedFrmSal>> violationsForDedFrmSal = validatorForDedFrmSal.validate(fxdpst.fdDedFrmSal);
        System.out.println("Errors(fd violationsForDedFrmSal): " + violationsForDedFrmSal);
        
        javax.validation.Validator validatorForWillSrrndr = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<FixedDepositDetailsWillSurrender>> violationsForWillSrrndr = validatorForWillSrrndr.validate(fxdpst.fdWillSurrender);
        System.out.println("Errors(fd violationsForWillSrrndr): " + violationsForWillSrrndr);
        
        javax.validation.Validator validatorForUseAmnt = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<FixedDepositDetailsUseAmnt>> violationsForUseAmnt = validatorForUseAmnt.validate(fxdpst.fdUseAmnt);
        System.out.println("Errors(fd violationsForUseAmnt): " + violationsForUseAmnt);
        

        ObjectNode response = JsonNodeFactory.instance.objectNode();

        if(!violations.isEmpty()){
            response.put("FD",RestUtils.createFailureResponse(violations));
        }
        if(fxdpst.haveTakenLoanOnFD != null){
        	if(fxdpst.haveTakenLoanOnFD.equals(TRUEVAL) && !violationsForDedFrmSal.isEmpty()){
        		response.put("FdDedFrmSal",RestUtils.createFailureResponse(violationsForDedFrmSal));
        	}
        	if(fxdpst.fdDedFrmSal.monthlyAmntDedctedFrmSal != null){
		        if(fxdpst.fdDedFrmSal.monthlyAmntDedctedFrmSal.equals(FALSEVAL) && !violationsForLoan.isEmpty()){
		            response.put("FDLoan",RestUtils.createFailureResponse(violationsForLoan));
		        }
		        if(!violationsForWillSrrndr.isEmpty()){
		            response.put("FdWillSurrender",RestUtils.createFailureResponse(violationsForWillSrrndr));
		        }
        	}
        	if(fxdpst.fdWillSurrender.willSurrender != null){
		        if(fxdpst.fdWillSurrender.willSurrender.equals(TRUEVAL) && !violationsForUseAmnt.isEmpty()){
		            response.put("FdUseAmnt",RestUtils.createFailureResponse(violationsForUseAmnt));
		        }
        	}
        }
        if(response.size() != 0){
    		return badRequest(response);
    	}

        String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        if(user != null){
            if(user.form == null) {
                user.form = new ApplicationForm();
                user.form.fixedDeposits = new ArrayList<FixedDeposit>();
            }else if (user.form.fixedDeposits == null) {
                user.form.fixedDeposits = new ArrayList<FixedDeposit>();
            }
            
            FixedDeposit fxd = new FixedDeposit();
            fxdpst.fdDedFrmSal.populate(fxd);
            fxdpst.loanDetails.populate(fxd);
            fxdpst.fdWillSurrender.populate(fxd);
            fxdpst.fdUseAmnt.populate(fxd);
            fxdpst.populate(fxd);
            user.form.fixedDeposits.add(fxd);
            Ebean.save(user.form);
            Ebean.save(user);
            return ok("Saved Json");
            //return redirect(routes.OwnContributionController.fds());
            //return ok(RestUtils.createSuccessResponse());
        }        
        
        return ok(RestUtils.createFailureResponse());
    }   

    public Result getFDDetails() {
            String email = session(SESSION_KEY_USER);
            User user = userDao.findUniqueByEmail(email);
            /*if(user == null){
                System.out.println("user = null");
            }else{
                System.out.println("user != null");
                if(user.form == null){ 
                    System.out.println("user.form = null");
                }else{
                    System.out.println("user.form != null");
                    if(user.form.own_contribution == null){
                        System.out.println("user.form.own_contribution = null");
                    }else{
                        System.out.println("user.form.own_contribution != null");
                        if(user.form.own_contribution.fds == null){
                            System.out.println("user.form.own_contribution.fds = null");
                        }else{
                            System.out.println("user.form.own_contribution.fds != null");
                        }
                    }
                } 
            } */
            if(user != null && user.form != null && user.form.fixedDeposits != null){
                        System.out.println("AND operation works");
                        System.out.println(user.form.fixedDeposits);
                        
                        return ok(getFDDetails.render(user.form.fixedDeposits));
                        //return ok("this worked");
            }
        //return badRequest(Json.stringify(RestUtils.createFailureResponse()));
        return ok("something went wrong!!!");
    }

    public Result recurringDep() {
        Form<RecurringDep> rd = Form.form(RecurringDep.class);
        Form<RecurringDepWillSurrenderYes> rdwsy = Form.form(RecurringDepWillSurrenderYes.class);
        Form<RecurringDepWillSurrenderNo> rdwsn = Form.form(RecurringDepWillSurrenderNo.class);
        Form<RecurringDepNoCuttingFrmSal> rdncfs = Form.form(RecurringDepNoCuttingFrmSal.class);
        
        return ok(recurringDep.render(rd, rdwsy, rdwsn, rdncfs));
    }
    
    public Result recurringDepSubmit() {
        Form<RecurringDep> rd = Form.form(RecurringDep.class).bindFromRequest();
        Form<RecurringDepWillSurrenderYes> rdwsy = Form.form(RecurringDepWillSurrenderYes.class).bindFromRequest();
        Form<RecurringDepWillSurrenderNo> rdwsn = Form.form(RecurringDepWillSurrenderNo.class).bindFromRequest();
        Form<RecurringDepNoCuttingFrmSal> rdncfs = Form.form(RecurringDepNoCuttingFrmSal.class).bindFromRequest();
        
        String TRUESTRING = "true";
        if (rd.hasErrors()) {
            System.out.print("RecurringDep Has errors");
            return badRequest(recurringDep.render(rd, rdwsy, rdwsn, rdncfs));
        }
        RecurringDep rDep = rd.get();

        if (TRUESTRING.equals(rDep.willSurrender) && rdwsy.hasErrors()) {
            System.out.println("rdwsy.hasErrors flag: "+rdwsy.hasErrors());
            return badRequest(recurringDep.render(rd, rdwsy, rdwsn, rdncfs));
        }
        
        if ((!TRUESTRING.equals(rDep.willSurrender)) && rdwsn.hasErrors()) {
            System.out.println("rdwsn.hasErrors flag: "+rdwsn.hasErrors());
            return badRequest(recurringDep.render(rd, rdwsy, rdwsn, rdncfs));
        }
        
        if ((!TRUESTRING.equals(rDep.willSurrender)) && (!rdwsn.hasErrors())) {
	        RecurringDepWillSurrenderNo rdwsnVal = rdwsn.get();
	        if ((!TRUESTRING.equals(rdwsnVal.monthlyAmntDedctedFrmSal)) && rdncfs.hasErrors()) {
	            System.out.println("rdncfs.hasErrors flag: "+rdncfs.hasErrors());
	            return badRequest(recurringDep.render(rd, rdwsy, rdwsn, rdncfs));
	        }
        }
        /*RecurringDeposit recDep = new RecurringDeposit();
        rDep.populate(recDep);

        OwnContribution oc = new OwnContribution();
        oc.rds = new ArrayList<RecurringDeposit>();
        oc.rds.add(recDep);
        Ebean.save(oc);*/

        return redirect(routes.OwnContributionController.insuranceDetails());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result addRD() {
        JsonNode json = request().body().asJson();
        RecurringDep rdBean = Json.fromJson(json, RecurringDep.class);
        javax.validation.Validator validator = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<RecurringDep>> violations = validator.validate(rdBean);
        
        javax.validation.Validator validatorForWillSrndrNo = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<RecurringDepWillSurrenderNo>> violationsForWillSrndrNo = validatorForWillSrndrNo.validate(rdBean.rdWillSurrenderNo);
        
        javax.validation.Validator validatorForWillSrndrYes = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<RecurringDepWillSurrenderYes>> violationsForWillSrndrYes = validatorForWillSrndrYes.validate(rdBean.rdWillSurrenderYes);
        
        javax.validation.Validator validatorForNoCuttingFrmSal = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<RecurringDepNoCuttingFrmSal>> violationsForNoCuttingFrmSal = validatorForNoCuttingFrmSal.validate(rdBean.rdNoCuttingFrmSal);
        
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        
        System.out.println("Errors: " + violations);
        System.out.println("Errors violationsForWillSrndrNo: " + violationsForWillSrndrNo);
        System.out.println("Errors violationsForWillSrndrYes: " + violationsForWillSrndrYes);
        System.out.println("Errors violationsForNoCuttingFrmSal: " + violationsForNoCuttingFrmSal);

        if (!violations.isEmpty()) {
        	response.put("RD",RestUtils.createFailureResponse(violations));
        }
        
        if(rdBean.willSurrender != null){
	        if (rdBean.willSurrender.equals(FALSEVAL) && !violationsForWillSrndrNo.isEmpty()) {
	        	response.put("WillSurrenderNo",RestUtils.createFailureResponse(violationsForWillSrndrNo));
	        }
	        
	        if (rdBean.willSurrender.equals(TRUEVAL) && !violationsForWillSrndrYes.isEmpty()) {
	        	response.put("WillSurrenderYes",RestUtils.createFailureResponse(violationsForWillSrndrYes));
	        }
        }
        
        if(rdBean.rdWillSurrenderNo.monthlyAmntDedctedFrmSal != null){
	        if (rdBean.rdWillSurrenderNo.monthlyAmntDedctedFrmSal.equals(FALSEVAL) && !violationsForNoCuttingFrmSal.isEmpty()) {
	        	response.put("NoCuttingFrmSal",RestUtils.createFailureResponse(violationsForNoCuttingFrmSal));
	        }  
        }
        
        if(response.size() != 0){
    		return badRequest(response);
    	}
        
        String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        if(user != null){
            if(user.form == null) {
                user.form = new ApplicationForm();
                user.form.recurringDeposits = new ArrayList<RecurringDeposit>();
            }else if (user.form.recurringDeposits == null) {
                user.form.recurringDeposits = new ArrayList<RecurringDeposit>();
            }
            
            RecurringDeposit rcd = new RecurringDeposit();
            rdBean.rdWillSurrenderYes.populate(rcd);
            rdBean.rdWillSurrenderNo.populate(rcd);
            rdBean.rdNoCuttingFrmSal.populate(rcd);
            rdBean.populate(rcd);
            user.form.recurringDeposits.add(rcd); 

            Ebean.save(user.form);
            Ebean.save(user);

            return ok("Saved Json");
            //return redirect(routes.OwnContributionController.rds());
            //return ok(RestUtils.createSuccessResponse());
        }        
        
        return ok(RestUtils.createFailureResponse());
    }   

    public Result getRDDetails() {
        try{
            String email = session(SESSION_KEY_USER);
            User user = userDao.findUniqueByEmail(email);
            /*if(user == null){
                System.out.println("user = null");
            }else{
                System.out.println("user != null");
                if(user.form == null){ 
                    System.out.println("user.form = null");
                }else{
                    System.out.println("user.form != null");
                    if(user.form.own_contribution == null){
                        System.out.println("user.form.own_contribution = null");
                    }else{
                        System.out.println("user.form.own_contribution != null");
                        if(user.form.own_contribution.rds == null){
                            System.out.println("user.form.own_contribution.rds = null");
                        }else{
                            System.out.println("user.form.own_contribution.rds != null");
                        }
                    }
                }
            }*/ 
            if(user != null && user.form != null && user.form.recurringDeposits != null){
                        return ok(getRDDetails.render(user.form.recurringDeposits));
            }
        }
        catch (NullPointerException e) {
            System.err.println("Caught NullPointerException: " + e.getMessage());
        }
        //return badRequest(Json.stringify(RestUtils.createFailureResponse()));
        return ok("something went wrong!!! ");
    }
    
    public Result entireOwnContribution(){
    	Form<BankAccountDetails> bAForm = Form.form(BankAccountDetails.class);
    	Form<BankAccountDeduction> bad = Form.form(BankAccountDeduction.class);
    	Form<BankAccountLoanDeduction> bald = Form.form(BankAccountLoanDeduction.class);
    	
    	Form<FixedDepositDetails> fd = Form.form(FixedDepositDetails.class);
        Form<FixedDepositDetailsDedFrmSal> dedFrmSal = Form.form(FixedDepositDetailsDedFrmSal.class);
        Form<FixedDepositDetailsWillSurrender> will = Form.form(FixedDepositDetailsWillSurrender.class);
        Form<FixedDepositDetailsUseAmnt> useAmnt = Form.form(FixedDepositDetailsUseAmnt.class);
        Form<FDLoanDetails> lonDtls = Form.form(FDLoanDetails.class);
        
        Form<RecurringDep> rd = Form.form(RecurringDep.class);
        Form<RecurringDepWillSurrenderYes> rdwsy = Form.form(RecurringDepWillSurrenderYes.class);
        Form<RecurringDepWillSurrenderNo> rdwsn = Form.form(RecurringDepWillSurrenderNo.class);
        Form<RecurringDepNoCuttingFrmSal> rdncfs = Form.form(RecurringDepNoCuttingFrmSal.class);
        
    	Form<InsuranceDetails> insD = Form.form(InsuranceDetails.class);
        Form<InsuranceDetailsAmountUsage> insAU = Form.form(InsuranceDetailsAmountUsage.class);
        
        Form<AmountFromFriends> aff = Form.form(AmountFromFriends.class);
        Form<AmountFromFriendsAsLoan> affl = Form.form(AmountFromFriendsAsLoan.class);
        Form<AmountFromFriendsAsDonation> affd = Form.form(AmountFromFriendsAsDonation.class);
        Form<AmountFromFriendsAsRepayment> affr = Form.form(AmountFromFriendsAsRepayment.class);
        Form<Property> property = PSForm.form(Property.class);
        
        Form<PersonalContribution> apptype = Form.form(PersonalContribution.class);
        
        String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
		if(user != null && user.form != null){
			apptype = apptype.fill(new PersonalContribution(user.form.personalContribution,user.form.cashWithYou));
			return ok(entireOwnContribution.render(bAForm,bad,bald,fd,dedFrmSal,will,useAmnt,lonDtls,rd,rdwsy,rdwsn,rdncfs,insD,insAU,aff,affl,affd,affr,property,user.form,apptype));
        }
        /*Form<PropertyDetails> sEP = Form.form(PropertyDetails.class);
        Form<PropertyAddresses> propAdd = Form.form(PropertyAddresses.class);
        Form<PropertyTypeDetails> ptd = Form.form(PropertyTypeDetails.class);
        Form<HaveIncomeFromeProperty> hifp = Form.form(HaveIncomeFromeProperty.class);
        Form<PropertyLoanMnthlyDedFrmSal> propDedctn = Form.form(PropertyLoanMnthlyDedFrmSal.class);
        Form<PropertySellToBuyAnother> pstba = Form.form(PropertySellToBuyAnother.class);
        Form<GenLoanDetails> lnDtls = Form.form(GenLoanDetails.class);*/
        
        //return ok(entireOwnContribution.render(bAForm,bad,bald,fd,dedFrmSal,will,useAmnt,lonDtls,rd,rdwsy,rdwsn,rdncfs,insD,insAU,aff,affl,affd,affr,sEP,ptd,hifp,propDedctn,pstba,propAdd,lnDtls));
        
        return ok(entireOwnContribution.render(bAForm,bad,bald,fd,dedFrmSal,will,useAmnt,lonDtls,rd,rdwsy,rdwsn,rdncfs,insD,insAU,aff,affl,affd,affr,property,null,apptype));
    }
    
    public Result insuranceDetails() {
        Form<InsuranceDetails> insD = Form.form(InsuranceDetails.class);
        Form<InsuranceDetailsAmountUsage> insAU = Form.form(InsuranceDetailsAmountUsage.class);
        
        return ok(insuranceDetails.render(insD,insAU));
        //return ok("Go home! Live in peace!!2");
    }
    
    public Result insuranceDetailsSubmit() {
        Form<InsuranceDetails> insD = Form.form(InsuranceDetails.class).bindFromRequest();
        Form<InsuranceDetailsAmountUsage> insAU = Form.form(InsuranceDetailsAmountUsage.class).bindFromRequest();
        String TRUEVAL = "true";
        if (insD.hasErrors()) {
            System.out.print("InsuranceDetails Has errors");
            return badRequest(insuranceDetails.render(insD,insAU));
            //return ok("Go home! Live in peace!!3");
        }
        InsuranceDetails iDet = insD.get();
        if (TRUEVAL.equals(iDet.receivingAnyMaturedAmount) && insAU.hasErrors()) {
            System.out.print("InsuranceDetailsAmountUsage Has errors");
            return badRequest(insuranceDetails.render(insD,insAU));
            //return ok("Go home! Live in peace!!3");
        }
        
        /*
        Insurance insudet = new Insurance();
        iDet.populate(insudet);
        OwnContribution oc = new OwnContribution();
        oc.insurance = new ArrayList<Insurance>();
        oc.insurance.add(insudet);
        Ebean.save(oc);*/

        return redirect(routes.OwnContributionController.amountFromFriends());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result addInsurance() {
        JsonNode json = request().body().asJson();
        InsuranceDetails insuBean = Json.fromJson(json, InsuranceDetails.class);
        javax.validation.Validator validator = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<InsuranceDetails>> violations = validator.validate(insuBean);
        System.out.println("Errors: " + violations);
        
        javax.validation.Validator validatorForAmountUsage = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<InsuranceDetailsAmountUsage>> violationsForAmountUsage = validatorForAmountUsage.validate(insuBean.insuAmountUsage);
        System.out.println("Insurance Errors ForAmountUsage: " + violationsForAmountUsage);

        ObjectNode response = JsonNodeFactory.instance.objectNode();
        
        if (!violations.isEmpty()) {
        	response.put("Insurance",RestUtils.createFailureResponse(violations));
        }
        
        if(insuBean.receivingAnyMaturedAmount != null){
	        if (insuBean.receivingAnyMaturedAmount.equals(TRUEVAL) && !violationsForAmountUsage.isEmpty()) {
	        	response.put("InsuranceAllocation",RestUtils.createFailureResponse(violationsForAmountUsage));
	        }
        }
        
        if(response.size() != 0){
    		return badRequest(response);
    	}
        
        String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        if(user != null){
            if(user.form == null) {
                user.form = new ApplicationForm();
                user.form.insurances = new ArrayList<Insurance>();
            }else if (user.form.insurances == null) {
                user.form.insurances = new ArrayList<Insurance>();
            }
            
            Insurance ins = new Insurance();
            insuBean.insuAmountUsage.populate(ins);
            insuBean.populate(ins);
            
            user.form.insurances.add(ins); 

            Ebean.save(user.form);
            Ebean.save(user);

            return ok("Saved Json");
            //return redirect(routes.OwnContributionController.insurance());
            //return ok(RestUtils.createSuccessResponse());
        }        
        
        return ok(RestUtils.createFailureResponse());
    }   

    public Result getInsuranceDetails() {
        try{
            String email = session(SESSION_KEY_USER);
            User user = userDao.findUniqueByEmail(email);
            /*
             if(user == null){
                System.out.println("user = null");
            }else{
                System.out.println("user != null");
                if(user.form == null){ 
                    System.out.println("user.form = null");
                }else{
                    System.out.println("user.form != null");
                    if(user.form.own_contribution == null){
                        System.out.println("user.form.own_contribution = null");
                    }else{
                        System.out.println("user.form.own_contribution != null");
                        if(user.form.own_contribution.insurance == null){
                            System.out.println("user.form.own_contribution.insurance = null");
                        }else{
                            System.out.println("user.form.own_contribution.insurance != null");
                        }
                    }
                }
            }*/
            if(user != null && user.form != null && user.form.insurances != null){
                        return ok(getInsuranceDetails.render(user.form.insurances));
            }
        }
        catch (NullPointerException e) {
            System.err.println("Caught NullPointerException: " + e.getMessage());
        }
        //return badRequest(Json.stringify(RestUtils.createFailureResponse()));
        return ok("something went wrong!!!");
    }
    
    public Result amountFromFriends() {
        Form<AmountFromFriends> aff = Form.form(AmountFromFriends.class);
        Form<AmountFromFriendsAmountNature> affan = Form.form(AmountFromFriendsAmountNature.class);
        Form<AmountFromFriendsAsLoan> affl = Form.form(AmountFromFriendsAsLoan.class);
        Form<AmountFromFriendsAsDonation> affd = Form.form(AmountFromFriendsAsDonation.class);
        Form<AmountFromFriendsAsRepayment> affr = Form.form(AmountFromFriendsAsRepayment.class);

        return ok(amountFromFriends.render(aff,affan, affl, affd, affr));
    }

    public Result amountFromFriendsSubmit() {
        Form<AmountFromFriends> aff = Form.form(AmountFromFriends.class).bindFromRequest();
        Form<AmountFromFriendsAmountNature> affan = Form.form(AmountFromFriendsAmountNature.class).bindFromRequest();
        Form<AmountFromFriendsAsLoan> affl = Form.form(AmountFromFriendsAsLoan.class).bindFromRequest();
        Form<AmountFromFriendsAsDonation> affd = Form.form(AmountFromFriendsAsDonation.class).bindFromRequest();
        Form<AmountFromFriendsAsRepayment> affr = Form.form(AmountFromFriendsAsRepayment.class).bindFromRequest();
        
        AmountFromFriends affget = aff.get();
        
        System.out.println("affget.isAmountFromFriends :"+affget.isAmountFromFriends);
        
        if(TRUEVAL.equals(affget.isAmountFromFriends)){
            
            if(affan.hasErrors()){
            	System.out.println("Form AmountFromFriendsAmountNature Has Errors");
                System.out.println("errors: "+affan.errors());
                return badRequest(amountFromFriends.render(aff, affan, affl, affd, affr));
            }
            
            AmountFromFriendsAmountNature affanget = affan.get();
            
            if(ASLOAN.equals(affanget.amountNature) && affl.hasErrors()){
                System.out.println("Form AmountFromFriendsAsLoan Has Errors");
                System.out.println("errors: "+affl.errors());
                return badRequest(amountFromFriends.render(aff, affan, affl, affd, affr));   
            }
            /*else if(ASLOAN.equals(affget.amountNature) && !affl.hasErrors()){
                //System.out.println("Form AmountFromFriendsAsLoan Doesnt Have Errors");
                AmountFromFriendsAsLoan afflget = affl.get();
                afflget.populate(msb);
            }*/
            
            if(ASDONATION.equals(affanget.amountNature) && affd.hasErrors()){
                System.out.println("Form AmountFromFriendsAsDonation Has Errors");
                System.out.println("errors: "+affd.errors());
                return badRequest(amountFromFriends.render(aff, affan, affl, affd, affr));   
            }
            /*else if(ASDONATION.equals(affget.amountNature) && !affd.hasErrors()){
                //System.out.println("Form AmountFromFriendsAsDonation Doesnt Have Errors");
                AmountFromFriendsAsDonation affdget = affd.get();
                affdget.populate(msb);
            }*/
            
            if(MONEYOWED.equals(affanget.amountNature) && affr.hasErrors()){
                System.out.println("Form AmountFromFriendsAsRepayment Has Errors");
                System.out.println("errors: "+affr.errors());
                return badRequest(amountFromFriends.render(aff, affan, affl, affd, affr));   
            }
            /*else if(MONEYOWED.equals(affget.amountNature) && !affr.hasErrors()){
                //System.out.println("Form AmountFromFriendsAsRepayment Doesnt Have Errors");
                AmountFromFriendsAsRepayment affrget = affr.get(); 
                affrget.populate(msb);
            }*/
/*
            String email = session(SESSION_KEY_USER);
            User user = userDao.findUniqueByEmail(email);
            if(user != null){
                if(user.form == null) {
                    user.form = new ApplicationForm();
                    user.form.miscBorrowings = new ArrayList<MiscBorrowing>();
                } else if (user.form.miscBorrowings == null) {
                    user.form.miscBorrowings = new ArrayList<MiscBorrowing>();
                }
                user.form.miscBorrowings.add(msb); 

                Ebean.save(user.form);
                Ebean.save(user);
                return ok("Saved");
            }
        }
        return ok("Something went wrong!!");*/
        }
        return redirect(routes.ApplicationFormController.closingFinishDetails());
        //return ok("Saved Successfully!!");
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public Result addMiscBorrowingDetails() {
    	
    	JsonNode json = request().body().asJson();
    	AmountFromFriends aff = Json.fromJson(json, AmountFromFriends.class);
        javax.validation.Validator validator = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<AmountFromFriends>> violations = validator.validate(aff);
        System.out.println("Errors: " + violations);
        
        javax.validation.Validator validatorForAmountNature = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<AmountFromFriendsAmountNature>> violationsForAmountNature = validatorForAmountNature.validate(aff.AmntNtr);
        System.out.println("violationsForAmountNature :" + violationsForAmountNature);
        
        javax.validation.Validator validatorForAsLoan = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<AmountFromFriendsAsLoan>> violationsForAsLoan = validatorForAsLoan.validate(aff.loanFields);
        System.out.println("violationsForAsLoan :" + violationsForAsLoan);
        
        javax.validation.Validator validatorForAsDonation = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<AmountFromFriendsAsDonation>> violationsForAsDonation = validatorForAsDonation.validate(aff.donationFields);
        System.out.println("violationsForAsDonation :" + violationsForAsDonation);
        
        javax.validation.Validator validatorForAsRepayment = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<AmountFromFriendsAsRepayment>> violationsForAsRepayment = validatorForAsRepayment.validate(aff.moneyOwedFields);
        System.out.println("violationsForAsRepayment :" + violationsForAsRepayment);

        ObjectNode response = JsonNodeFactory.instance.objectNode();
        
        if (!violations.isEmpty()) {
        	response.put("IsAmountFromFriends",RestUtils.createFailureResponse(violations));
        }
        
        if(aff.isAmountFromFriends != null){
	        if (aff.isAmountFromFriends.equals(TRUEVAL) && !violationsForAmountNature.isEmpty()) {
	        	response.put("AmountNature",RestUtils.createFailureResponse(violationsForAmountNature));
	        }
	        if(aff.AmntNtr.amountNature != null){
	        	String loanF = "asLoan";
	        	if(aff.AmntNtr.amountNature.equals(loanF) && !violationsForAsLoan.isEmpty()){
	        		response.put("loanFields",RestUtils.createFailureResponse(violationsForAsLoan));
	        	}
	        	String DonationF = "asDonation";
	        	if(aff.AmntNtr.amountNature.equals(DonationF) && !violationsForAsDonation.isEmpty()){
	        		response.put("donationFields",RestUtils.createFailureResponse(violationsForAsDonation));
	        	}
	        	String moneyOwedF = "moneyOwed";
	        	if(aff.AmntNtr.amountNature.equals(moneyOwedF) && !violationsForAsRepayment.isEmpty()){
	        		response.put("moneyOwedFields",RestUtils.createFailureResponse(violationsForAsRepayment));
	        	}
	        }
        }
        
        if(response.size() != 0){
    		return badRequest(response);
    	}
        
        String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        if(user != null){
            if(user.form == null) {
                user.form = new ApplicationForm();
                user.form.miscBorrowings = new ArrayList<MiscBorrowing>();
            } else if (user.form.miscBorrowings == null) {
                user.form.miscBorrowings = new ArrayList<MiscBorrowing>();
            }
            
            MiscBorrowing mb = new MiscBorrowing();
            
            aff.AmntNtr.populate(mb);
            aff.loanFields.populate(mb);
            aff.donationFields.populate(mb);
            aff.moneyOwedFields.populate(mb);
            
            user.form.miscBorrowings.add(mb); 

            Ebean.save(user.form);
            Ebean.save(user);
            return ok("Saved Json");
        }        
        return ok("Couldnt Save");
    	
        /*JsonNode json = request().body().asJson();

        JsonNode amountNatureObj = json.get("amountNatureObj");
        AmountFromFriends aff = Json.fromJson(amountNatureObj, AmountFromFriends.class);
        javax.validation.Validator validator = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<AmountFromFriends>> violations = validator.validate(aff);
        System.out.println("Errors<violations>: " + violations);
        
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        Boolean flagForValuePutInResponse = false;
        MiscBorrowing mb = new MiscBorrowing();

        if(!violations.isEmpty()){
            flagForValuePutInResponse = true;
            response.put("amountNatureObj",RestUtils.createFailureResponse(violations));
        }else{
            aff.populate(mb);
        }

        String loanF = "asLoan";
        if(aff.amountNature.equals(loanF)){
            JsonNode loanFields = json.get("loanFields");
            AmountFromFriendsAsLoan affal = Json.fromJson(loanFields, AmountFromFriendsAsLoan.class);
            javax.validation.Validator validatorForLoan = play.data.validation.Validation.getValidator();
            Set<ConstraintViolation<AmountFromFriendsAsLoan>> violationsForLoan = validatorForLoan.validate(affal);
            System.out.println("Errors<violationsForLoan>: " + violationsForLoan);

            if(!violationsForLoan.isEmpty()){
                System.out.println("asLoan");
                flagForValuePutInResponse = true;
                response.put("loanFields",RestUtils.createFailureResponse(violationsForLoan));
            }else{
                affal.populate(mb);
            }
        }

        String donationF = "asDonation";
        if(aff.amountNature.equals(donationF)){
            JsonNode donationFields = json.get("donationFields");
            AmountFromFriendsAsDonation affad = Json.fromJson(donationFields, AmountFromFriendsAsDonation.class);
            javax.validation.Validator validatorForDonation = play.data.validation.Validation.getValidator();
            Set<ConstraintViolation<AmountFromFriendsAsDonation>> violationsForDonation = validatorForDonation.validate(affad);
            System.out.println("Errors<violationsForDonation>: " + violationsForDonation);

            if(!violationsForDonation.isEmpty()){
                System.out.println("asDonation");
                flagForValuePutInResponse = true;
                response.put("donationFields",RestUtils.createFailureResponse(violationsForDonation));
            }else{
                affad.populate(mb);
            }
        }

        String moneyOwedF = "moneyOwed";
        if(aff.amountNature.equals(moneyOwedF)){
            JsonNode moneyOwedFields = json.get("moneyOwedFields");
            AmountFromFriendsAsRepayment affar = Json.fromJson(moneyOwedFields, AmountFromFriendsAsRepayment.class);
            javax.validation.Validator validatorForRepayment = play.data.validation.Validation.getValidator();
            Set<ConstraintViolation<AmountFromFriendsAsRepayment>> violationsForRepayment = validatorForRepayment.validate(affar);
            System.out.println("Errors<violationsForRepayment>: " + violationsForRepayment);

            if(!violationsForRepayment.isEmpty()){
                System.out.println("moneyOwed");
                flagForValuePutInResponse = true;
                response.put("moneyOwedFields",RestUtils.createFailureResponse(violationsForRepayment));
                System.out.println("response: "+response.toString());
            }else{
                affar.populate(mb);
            }
        }

        if (flagForValuePutInResponse == true) {               
            System.out.println("response: "+response.toString());
            return badRequest(response);
        }

        String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        if(user != null){
            if(user.form == null) {
                user.form = new ApplicationForm();
                user.form.miscBorrowings = new ArrayList<MiscBorrowing>();
            } else if (user.form.miscBorrowings == null) {
                user.form.miscBorrowings = new ArrayList<MiscBorrowing>();
            }
            user.form.miscBorrowings.add(mb); 

            Ebean.save(user.form);
            Ebean.save(user);
            return ok("Saved Json");
        }     
        return ok("Couldnt Save");
        //return ok(RestUtils.createFailureResponse());
*/    
    }   
    
    /*public Result getMiscBorrowingDetails() {
	    try{
	        String email = session(SESSION_KEY_USER);
	        User user = userDao.findUniqueByEmail(email);
	        if(user == null){
	            System.out.println("user = null");
	        }else{
	            System.out.println("user != null");
	            return ok(getMiscBorrowingDetails.render(MiscBorrowing));
	        }
	    }
	    catch (NullPointerException e) {
	        System.err.println("Caught NullPointerException: " + e.getMessage());
	    }
	    //return badRequest(Json.stringify(RestUtils.createFailureResponse()));
	    return ok("something went wrong!!!");
	}*/
    
    public Result getMiscBorrowingDetails() {
        try{
            String email = session(SESSION_KEY_USER);
            User user = userDao.findUniqueByEmail(email);
            if(user != null && user.form != null && user.form.miscBorrowings != null){
                        return ok(getMiscBorrowingDetails.render(user.form.miscBorrowings));
            }
        }
        catch (NullPointerException e) {
            System.err.println("Caught NullPointerException: " + e.getMessage());
        }
        //return badRequest(Json.stringify(RestUtils.createFailureResponse()));
        return ok("something went wrong!!!");
    }
    
    
    public Result propertyDetails() {
        Form<PropertyDetails> sEP = Form.form(PropertyDetails.class);
        Form<PropertyTypeDetails> ptd = Form.form(PropertyTypeDetails.class);
        Form<HaveIncomeFromeProperty> hifp = Form.form(HaveIncomeFromeProperty.class);
        Form<PropertyLoanMnthlyDedFrmSal> propDedctn = Form.form(PropertyLoanMnthlyDedFrmSal.class);
        Form<PropertySellToBuyAnother> pstba = Form.form(PropertySellToBuyAnother.class);
        Form<PropertyAddresses> propAdd = Form.form(PropertyAddresses.class);
        Form<GenLoanDetails> lnDtls = Form.form(GenLoanDetails.class);

        return ok(propertyDetails.render(sEP,ptd,hifp,propDedctn,pstba,propAdd,lnDtls));
    }

    public Result propertyDetailsSubmit() {
        Form<PropertyDetails> sEP = Form.form(PropertyDetails.class).bindFromRequest();
        Form<PropertyTypeDetails> ptd = Form.form(PropertyTypeDetails.class).bindFromRequest();
        Form<HaveIncomeFromeProperty> hifp = Form.form(HaveIncomeFromeProperty.class).bindFromRequest();
        Form<PropertyLoanMnthlyDedFrmSal> propDedctn = Form.form(PropertyLoanMnthlyDedFrmSal.class).bindFromRequest();
        Form<PropertySellToBuyAnother> pstba = Form.form(PropertySellToBuyAnother.class).bindFromRequest();
        Form<PropertyAddresses> propAdd = Form.form(PropertyAddresses.class).bindFromRequest().bindFromRequest();
        Form<GenLoanDetails> lnDtls = Form.form(GenLoanDetails.class).bindFromRequest().bindFromRequest();

        if (sEP.hasErrors()) {
            System.out.println("PropertyDetails Form Has errors");
            System.out.println("errors: "+sEP.errors());
            return badRequest(propertyDetails.render(sEP,ptd,hifp,propDedctn,pstba,propAdd,lnDtls));
        }

        if(propAdd.hasErrors()){
            System.out.println("PropertyDetails Property Address Has errors");
            System.out.println("errors: "+propAdd.errors());
            return badRequest(propertyDetails.render(sEP,ptd,hifp,propDedctn,pstba,propAdd,lnDtls));   
        }
        PropertyDetails pDet = sEP.get();
        //PropertyAddresses pAdd = propAdd.get();

        if (LOANTAKEN.equals(pDet.loan) && lnDtls.hasErrors()) {
            System.out.println("lnDtls.hasErrors: "+lnDtls.hasErrors());
            System.out.println("errors: "+lnDtls.errors());
            return badRequest(propertyDetails.render(sEP,ptd,hifp,propDedctn,pstba,propAdd,lnDtls));
        }

        /*Property propr = new Property();
        pDet.populate(propr);
        Address PrAdd = new Address();
        pAdd.populate(PrAdd);
        
        OwnContribution oc = new OwnContribution();
        oc.prop_owned = new ArrayList<Property>();

        if (LOANTAKEN.equals(pDet.loan) && !lnDtls.hasErrors()) {
            System.out.println("lnDtls.hasErrors flag: "+lnDtls.hasErrors());
            GenLoanDetails lndt = lnDtls.get();
            LoanDetails lds = new LoanDetails();
            lndt.populate(lds);
            Ebean.save(lds);
        }

        Ebean.save(PrAdd);
        oc.prop_owned.add(propr);
        Ebean.save(oc);*/

        return redirect(routes.ApplicationFormController.closingFinishDetails());
    }
    
    /*@BodyParser.Of(BodyParser.Json.class)
    public Result addPropertyDetails() {
        JsonNode json = request().body().asJson();
        PropertyDetails propD = Json.fromJson(json, PropertyDetails.class);
        
        javax.validation.Validator validatorForPropIncome = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<HaveIncomeFromeProperty>> violationsForPropIncome = validatorForPropIncome.validate(propD.haveIncome);
        System.out.println("Errors(violationsForPropIncome): " + violationsForPropIncome);
        
        javax.validation.Validator validatorForPropType = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<PropertyTypeDetails>> violationsForPropType = validatorForPropType.validate(propD.propType);
        System.out.println("Errors( violationsForPropType): " + violationsForPropType);
        
        javax.validation.Validator validator = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<PropertyDetails>> violations = validator.validate(propD);
        System.out.println("Errors: " + violations);
        
        javax.validation.Validator validatorForPropAdd = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<PropertyAddresses>> violationsForPropAdd = validatorForPropAdd.validate(propD.propAdd);
        System.out.println("Errors( violationsForPropAdd): " + violationsForPropAdd);
        
        javax.validation.Validator validatorForDedFromSal = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<PropertyLoanMnthlyDedFrmSal>> violationsForDedFromSal = validatorForDedFromSal.validate(propD.propDedFromSal);
        System.out.println("Errors( violationsForDedFromSal): " + violationsForDedFromSal);
        
        javax.validation.Validator validatorForPropLoan = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<GenLoanDetails>> violationsForPropLoan = validatorForPropLoan.validate(propD.propLoan);
        System.out.println("Errors( violationsForPropLoan): " + violationsForPropLoan);
        
        javax.validation.Validator validatorForPropSell = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<PropertySellToBuyAnother>> violationsForPropSell = validatorForPropSell.validate(propD.propSell);
        System.out.println("Errors( violationsForPropSell): " + violationsForPropSell);
        
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        
    	if (!violationsForPropIncome.isEmpty()) {
        	response.put("haveIncome",RestUtils.createFailureResponse(violationsForPropIncome));
    	}
    	if(propD.haveIncome.IncomeFrmExistingProperty != null){
	        if (propD.haveIncome.IncomeFrmExistingProperty.equals(TRUEVAL) && !violationsForPropType.isEmpty()) {
	        	response.put("propType",RestUtils.createFailureResponse(violationsForPropType));
	        }
	        if(propD.propType.type != null){
	        	if (!violations.isEmpty()){
	        		response.put("propDetails",RestUtils.createFailureResponse(violations));
	        	}
	        	if (!violationsForPropAdd.isEmpty()){
	        		response.put("propAddress",RestUtils.createFailureResponse(violationsForPropAdd));
	        	}
	        	if(propD.loan != null){
		        	if(propD.loan.equals(TRUEVAL) && !violationsForDedFromSal.isEmpty()){
		        		response.put("propDedFrmSal",RestUtils.createFailureResponse(violationsForDedFromSal));
		        	}
		        	if(propD.propDedFromSal.monthlyAmntDedctedFrmSal != null){
			        	if(propD.propDedFromSal.monthlyAmntDedctedFrmSal.equals(FALSEVAL) && !violationsForPropLoan.isEmpty()){
			        		response.put("propLoan",RestUtils.createFailureResponse(violationsForPropLoan));
			        	}
		        	}
	        	}
	        	if(propD.willingToSell != null){
		        	if(propD.willingToSell.equals(TRUEVAL) && !violationsForPropSell.isEmpty()){
		        		response.put("propSell",RestUtils.createFailureResponse(violationsForPropSell));
		        	}
	        	}
	        }
    	}
    	
    	if(response.size() != 0){
    		return badRequest(response);
    	}
    	
        String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        if(user != null){
        	if(user.form == null) {
                user.form = new ApplicationForm();
                user.form.property = new ArrayList<Property>();
            } else if (user.form.property == null) {
                user.form.property = new ArrayList<Property>();
            }
        	Property prop = new Property();
            propD.propType.populate(prop);
            propD.propLoan.populate(prop);
            propD.propDedFromSal.populate(prop);
            propD.propSell.populate(prop);
            
            Address propaddress = new Address();
            propD.propAdd.populate(propaddress);
            prop.location = propaddress;

            propD.populate(prop);
            
            user.form.property.add(prop);
            
            Ebean.save(user.form);
            Ebean.save(user);

            return ok("Saved Json");
            //return redirect(routes.OwnContributionController.bank_accts());
            //return ok(RestUtils.createSuccessResponse());
        }        
        
        return ok(RestUtils.createFailureResponse());
    }   */
    
    
    @BodyParser.Of(BodyParser.Json.class)
    public Result addPropertyDetails() {
        JsonNode json = request().body().asJson();
        BindingResult<Property> result = JsonHelper.parse(json, Property.class);
        System.out.println("json: " + json);
        System.out.println("json Text: " + json.asText());
        
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        
        if (result.hasErrors()) {
        	response.put("propDetails",RestUtils.createFailureResponse(result.getErrors()));
            return badRequest(response);
        }
        
        Property prop = result.get();
        String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        if(user != null){
        	if(user.form == null) {
                user.form = new ApplicationForm();
                user.form.property = new ArrayList<Property>();
            } else if (user.form.property == null) {
                user.form.property = new ArrayList<Property>();
            }
            
            user.form.property.add(prop);
            
            //Ebean.save(user.form.property);
            Ebean.save(user.form);
            Ebean.save(user);

            return ok("Saved Json");
            //return redirect(routes.OwnContributionController.bank_accts());
            //return ok(RestUtils.createSuccessResponse());
        }        
        
        //return ok(RestUtils.createFailureResponse());
        return ok("Parsed: " + result.get());
    }
    
    /*@BodyParser.Of(BodyParser.Json.class)
    public Result addPropertyDetails() {
        JsonNode json = request().body().asJson();
        Property propD = Json.fromJson(json, Property.class);
        
        JsonNode json = request().body().asJson();
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        String type = json.findPath("type").getTextValue();
        if(type == null) {
        	response.put("status", "KO");
        	response.put("message", "Missing parameter [name]");
          return badRequest(result);
        } else {
          result.put("status", "OK");
          result.put("message", "Hello " + name);
          return ok(result);
        }
        
        javax.validation.Validator validatorForPropIncome = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<HaveIncomeFromeProperty>> violationsForPropIncome = validatorForPropIncome.validate(propD.haveIncome);
        System.out.println("Errors(violationsForPropIncome): " + violationsForPropIncome);
        
        javax.validation.Validator validatorForPropType = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<Property>> violationsForPropType = validatorForPropType.validate(propD.type);
        System.out.println("Errors( violationsForPropType): " + violationsForPropType);
        
        javax.validation.Validator validator = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<Property>> violations = validator.validate(propD);
        System.out.println("Errors: " + violations);
        
        System.out.println("propD.allocation: " + propD.allocation);
        
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        
        javax.validation.Validator validatorForPropAdd = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<PropertyAddresses>> violationsForPropAdd = validatorForPropAdd.validate(propD.propAdd);
        System.out.println("Errors( violationsForPropAdd): " + violationsForPropAdd);
        
        javax.validation.Validator validatorForDedFromSal = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<PropertyLoanMnthlyDedFrmSal>> violationsForDedFromSal = validatorForDedFromSal.validate(propD.propDedFromSal);
        System.out.println("Errors( violationsForDedFromSal): " + violationsForDedFromSal);
        
        javax.validation.Validator validatorForPropLoan = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<GenLoanDetails>> violationsForPropLoan = validatorForPropLoan.validate(propD.propLoan);
        System.out.println("Errors( violationsForPropLoan): " + violationsForPropLoan);
        
        javax.validation.Validator validatorForPropSell = play.data.validation.Validation.getValidator();
        Set<ConstraintViolation<PropertySellToBuyAnother>> violationsForPropSell = validatorForPropSell.validate(propD.propSell);
        System.out.println("Errors( violationsForPropSell): " + violationsForPropSell);
        
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        
    	if (!violationsForPropIncome.isEmpty()) {
        	response.put("haveIncome",RestUtils.createFailureResponse(violationsForPropIncome));
    	}
    	if(propD.haveIncome.IncomeFrmExistingProperty != null){
	        if (propD.haveIncome.IncomeFrmExistingProperty.equals(TRUEVAL) && !violationsForPropType.isEmpty()) {
	        	response.put("propType",RestUtils.createFailureResponse(violationsForPropType));
	        }
	        if(propD.propType.type != null){
	        	if (!violations.isEmpty()){
	        		response.put("propDetails",RestUtils.createFailureResponse(violations));
	        	}
	        	if (!violationsForPropAdd.isEmpty()){
	        		response.put("propAddress",RestUtils.createFailureResponse(violationsForPropAdd));
	        	}
	        	if(propD.loan != null){
		        	if(propD.loan.equals(TRUEVAL) && !violationsForDedFromSal.isEmpty()){
		        		response.put("propDedFrmSal",RestUtils.createFailureResponse(violationsForDedFromSal));
		        	}
		        	if(propD.propDedFromSal.monthlyAmntDedctedFrmSal != null){
			        	if(propD.propDedFromSal.monthlyAmntDedctedFrmSal.equals(FALSEVAL) && !violationsForPropLoan.isEmpty()){
			        		response.put("propLoan",RestUtils.createFailureResponse(violationsForPropLoan));
			        	}
		        	}
	        	}
	        	if(propD.willingToSell != null){
		        	if(propD.willingToSell.equals(TRUEVAL) && !violationsForPropSell.isEmpty()){
		        		response.put("propSell",RestUtils.createFailureResponse(violationsForPropSell));
		        	}
	        	}
	        }
    	}
    	
    	if(response.size() != 0){
    		return badRequest(response);
    	}
    	
    	Property property = propD;
        String email = session(SESSION_KEY_USER);
        User user = userDao.findUniqueByEmail(email);
        if(user != null){
        	if(user.form == null) {
                user.form = new ApplicationForm();
                user.form.property = new ArrayList<Property>();
            } else if (user.form.property == null) {
                user.form.property = new ArrayList<Property>();
            }
        	Property prop = new Property();
            propD.propType.populate(prop);
            propD.propLoan.populate(prop);
            propD.propDedFromSal.populate(prop);
            propD.propSell.populate(prop);
            
            Address propaddress = new Address();
            propD.propAdd.populate(propaddress);
            prop.location = propaddress;

            propD.populate(prop);
        	//property.location = propD.location;
            user.form.property.add(property);
            
            //Ebean.save(user.form.property);
            Ebean.save(user.form);
            Ebean.save(user);

            return ok("Saved Json");
            //return redirect(routes.OwnContributionController.bank_accts());
            //return ok(RestUtils.createSuccessResponse());
        }        
        
        return ok(RestUtils.createFailureResponse());
    }   */

    public Result getPropertyDetails(){
        try{
            String email = session(SESSION_KEY_USER);
            User user = userDao.findUniqueByEmail(email);

            if(user != null && user.form != null && user.form.property != null){
                        return ok(getPropertyDetails.render(user.form.property));
            }
        }
        catch (NullPointerException e) {
            System.err.println("Caught NullPointerException: " + e.getMessage());
        }
        //return badRequest(Json.stringify(RestUtils.createFailureResponse()));
        return ok("something went wrong!!!");
    }
    
   /* public Result propertyDetailsNew() {
        
        return ok(propertyDetailsNew.render(property));
    }*/
    
    public Result deleteBankAccount(Long id) {
    	BankAccount account = bankAccountDao.findById(id, currentUser());
    	if (account != null) {
    		bankAccountDao.delete(account);
    		return ok("Deleted!");
    	}
    	
    	return badRequest("Unauthorized!");
    			
    }
    public Result deleteFDDetail(Long id){
    	FixedDeposit fixedD=fdDetailesDao.findById(id, currentUser());
    	if(fixedD != null){
    		fdDetailesDao.delete(fixedD);
    		return ok("Deleted!");
    	}
    	return badRequest("Unauthorized!");
    	
    }
    public Result deleteRDDetail(Long id){
    	RecurringDeposit recurringD=rdDetailesDao.findById(id, currentUser());
		if(recurringD != null){
			rdDetailesDao.delete(recurringD);
			return ok("Deleted");
		}
		
		return badRequest("Unauthorized!");
    }
    public Result deleteInsuranceDetail(Long id){
    	Insurance insurance =insuranceDao.findById(id, currentUser());
    	if(insurance != null){
    		insuranceDao.delete(insurance);
    		return ok("Deleted");
    	}
    	return badRequest("Unauthorized");
    }
   public Result deleteMiscBDetails(Long id){
	   MiscBorrowing miscBorrowing = miscBorrowingDao.findById(id, currentUser());
	   if(miscBorrowing != null){
		   miscBorrowingDao.delete(miscBorrowing);
		   return ok("Deleted");
	   }
	   return badRequest("Unauthorized");
   }
   public Result deletePropertyDetails(Long id){
	   Property property = propertyDao.findById(id, currentUser());
	   if(property != null){
		   propertyDao.delete(property);
		   return ok("Deleted");
	   }
	   return badRequest("Unauthorised");
   }
}