package models.dao;

public interface DaoProvider {
    UserDao userDao();
    ApplicantDao applicantDao();
    ApplicationFormDao applicationFormDao();
    PaymentDao paymentDao();
    InvoiceDao invoiceDao();
    FileAttachmentDao fileAttachmentDao();
    BankAccountDao bankAccountDao();
    FDDetailsDao fdDetailsDao();
    RDDetailsDao rdDetailsDao();

    DependentDao dependentDao();
	LoanDetailsDao loanDetailsDao();

	InsuranceDao insuranceDao();
    MiscBorrowingDao miscBorrowingDao();
    PropertyDao propertyDao();
    PaymentCodeDao paymentCodeDao();
    RoleDao roleDao();
    AgentDao agentDao();
    ManagerDao managerDao();
    BueroDao bueroDao();
    InvestigationReportDao investigationReportDao();
    
    PageDao pageDao();
    AgentFeedbackFormFileDao agentFeedbackFormFileDao();
}
