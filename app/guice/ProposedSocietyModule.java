package guice;

import mail.Mailer;
import mail.PsMailer;
import models.dao.DaoProvider;
import models.dao.ebean.EbeanDaoProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scheduler.BatchProcessor;
import staticdata.InMemoryPinCodes;
import staticdata.Pincodes;
import utils.DefaultFormIdGenerator;
import utils.DefaultPaymentCodeGenerator;
import utils.DefaultSmsGateway;
import utils.DefaultTransactionIdGenerator;
import utils.FormIdGenerator;
import utils.IdGenerator;
import utils.IdManager;
import utils.MockCCAvenuePaymentGateway;
import utils.PaymentCodeGenerator;
import utils.PaymentGateway;
import utils.PersistentIdManager;
import utils.SmsGateway;
import utils.TransactionIdGenerator;
import birt.BirtReportEngine;
import birt.CsvGenerator;
import birt.PsCsvGenerator;
import birt.ReportEngine;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import controllers.AdminUserController;
import controllers.AgentFormController;
import controllers.ApplicantController;
import controllers.Application;
import controllers.ApplicationFormController;
import controllers.AttachmentsController;
import controllers.BueroController;
import controllers.OwnContributionController;
import controllers.Pages;
import controllers.PaymentCodeController;
import controllers.PincodeController;

public class ProposedSocietyModule extends AbstractModule {
    private static final Logger LOG = LoggerFactory.getLogger(ProposedSocietyModule.class);

	@Override
	protected void configure() {
		LOG.info("Configuring ProposedSociety Module.");
		

		// Simple bindings.
		// TODO: We may want to bind these in a Singleton.
		bind(Application.class).in(Singleton.class);
		bind(ApplicantController.class).in(Singleton.class);
		bind(AgentFormController.class).in(Singleton.class);
		bind(ApplicationFormController.class).in(Singleton.class);
		bind(OwnContributionController.class).in(Singleton.class);
		bind(Pages.class).in(Singleton.class);
		bind(AdminUserController.class).in(Singleton.class);
		bind(AttachmentsController.class).in(Singleton.class);
        bind(PaymentCodeController.class).in(Singleton.class);
        bind(PincodeController.class).in(Singleton.class);
        bind(BatchProcessor.class).in(Singleton.class);
        bind(BueroController.class).in(Singleton.class);
        
        bind(IdGenerator.class).in(Singleton.class);
        bind(FormIdGenerator.class).to(DefaultFormIdGenerator.class).in(Singleton.class);
        bind(IdManager.class).to(PersistentIdManager.class).in(Singleton.class);
        
		// DAO Bindings.
		bind(DaoProvider.class).to(EbeanDaoProvider.class).in(Singleton.class);

		// Other module bindings.
		bind(Mailer.class).to(PsMailer.class).in(Singleton.class);
		bind(CsvGenerator.class).to(PsCsvGenerator.class).in(Singleton.class);
		bind(SmsGateway.class).to(DefaultSmsGateway.class).in(Singleton.class);
		bind(ReportEngine.class).to(BirtReportEngine.class).in(Singleton.class);
		bind(PaymentGateway.class).to(MockCCAvenuePaymentGateway.class).in(Singleton.class);
		bind(TransactionIdGenerator.class).to(DefaultTransactionIdGenerator.class).in(Singleton.class);
		bind(PaymentCodeGenerator.class).to(DefaultPaymentCodeGenerator.class).in(Singleton.class);
		bind(Pincodes.class).to(InMemoryPinCodes.class).in(Singleton.class);
		
	}
}
