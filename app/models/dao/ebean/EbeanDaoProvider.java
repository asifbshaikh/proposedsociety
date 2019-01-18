package models.dao.ebean;

import models.dao.AgentDao;
import models.dao.AgentFeedbackFormFileDao;
import models.dao.ApplicantDao;
import models.dao.ApplicationFormDao;
import models.dao.BankAccountDao;
import models.dao.BueroDao;
import models.dao.DaoProvider;
import models.dao.DependentDao;
import models.dao.FDDetailsDao;
import models.dao.FileAttachmentDao;
import models.dao.InsuranceDao;
import models.dao.InvestigationReportDao;
import models.dao.InvoiceDao;
import models.dao.LoanDetailsDao;
import models.dao.ManagerDao;
import models.dao.MiscBorrowingDao;
import models.dao.PageDao;
import models.dao.PaymentCodeDao;
import models.dao.PaymentDao;
import models.dao.PropertyDao;
import models.dao.RDDetailsDao;
import models.dao.RoleDao;
import models.dao.UserDao;

public class EbeanDaoProvider implements DaoProvider {


    private final UserDao userDao = new EbeanUserDao();
    private final ApplicantDao applicantDao = new EbeanApplicantDao();
    private final ApplicationFormDao applicationFormDao = new EbeanApplicationFormDao();
    private final PaymentDao paymentDao = new EbeanPaymentDao();
    private final InvoiceDao invoiceDao = new EbeanInvoiceDao();
    private final FileAttachmentDao fileAttachmentDao = new EbeanFileAttachmentDao();
    private final BankAccountDao bankAccountDao = new EbeanBankAccountDao();
    private final FDDetailsDao fdDetailsDao = new EbeanFDDetailsDao();
    private final RDDetailsDao rdDetailsDao = new EbeanRDDetailsDao();
    private final DependentDao dependentDao = new EbeanDependentDao();
    private final LoanDetailsDao loanDetailsDao = new EbeanLoanDetailsDao();

    private final InsuranceDao insuranceDao = new EbeanInsuranceDao();
    private final MiscBorrowingDao miscBorrowingDao = new EbeanMiscBorrowingDao();
    private final PropertyDao propertyDao = new EbeanPropertyDao();
    private final PaymentCodeDao paymentCodeDao = new EbeanPaymentCodeDao();
    private final AgentDao agentDao = new EbeanAgentDao();
    private final RoleDao roleDao = new EbeanRoleDao();
    private final ManagerDao managerDao = new EbeanManagerDao();
    private final BueroDao bueroDao = new EbeanBueroDao();
    private final InvestigationReportDao investigationReportDao = new EbeanInvestigationReportDao(); 
    private final PageDao pageDao = new EbeanPageDao();
    private final AgentFeedbackFormFileDao agentFeedbackFormFileDao = new EbeanAgentFeedbackFormFileDao();

    @Override
    public UserDao userDao() {
        return userDao;
    }
    
    @Override
    public ManagerDao managerDao(){
    	return managerDao;
    }
    
    @Override
    public ApplicantDao applicantDao() {
        return applicantDao;
    }

    @Override
    public ApplicationFormDao applicationFormDao() {
        return applicationFormDao;
    }

    @Override
    public PaymentDao paymentDao() {
        return paymentDao;
    }

    @Override
    public InvoiceDao invoiceDao() {
        return invoiceDao;
    }

    @Override
    public FileAttachmentDao fileAttachmentDao() {
        return fileAttachmentDao;
    }

	@Override
	public BankAccountDao bankAccountDao() {
		return bankAccountDao;
	}

	@Override
	public FDDetailsDao fdDetailsDao() {
		return fdDetailsDao;
	}

	@Override
	public RDDetailsDao rdDetailsDao() {
		return rdDetailsDao;
	}
	
	@Override
    public DependentDao dependentDao() {
        return dependentDao;
    }
	
	@Override
    public LoanDetailsDao loanDetailsDao() {
        return loanDetailsDao;
    }

	@Override
	public InsuranceDao insuranceDao() {

		return insuranceDao;
	}

	@Override
	public MiscBorrowingDao miscBorrowingDao() {
	
		return miscBorrowingDao;
	}

	@Override
	public PropertyDao propertyDao() {
		
		return propertyDao;
	}

    @Override
    public PaymentCodeDao paymentCodeDao() {
        return paymentCodeDao;
    }

	@Override
	public AgentDao agentDao() {
		
		return agentDao;
	}

	@Override
	public RoleDao roleDao() {
		return roleDao;
	}

	@Override
	public BueroDao bueroDao() {
		return bueroDao;
	}

	@Override
	public InvestigationReportDao investigationReportDao() {
		return investigationReportDao;
	}

	@Override
	public PageDao pageDao() {
		return pageDao;
	}

	@Override
	public AgentFeedbackFormFileDao agentFeedbackFormFileDao() {
		return agentFeedbackFormFileDao;
	}
}
