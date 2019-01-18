import guice.ProposedSocietyModule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import models.ApplicationForm;
import models.Buero;
import models.Role;
import models.User;
import models.dao.BueroDao;
import models.dao.DaoProvider;
import models.dao.UserDao;

import org.eclipse.birt.core.framework.Platform;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Application;
import play.GlobalSettings;
import play.data.format.Formatters;
import play.data.format.Formatters.SimpleFormatter;
import scheduler.BatchProcessor;
import staticdata.Pincodes;
import utils.MockCCAvenuePaymentGateway;
import utils.PaymentGateway;
import utils.StringUtils;

import birt.ReportEngineException;
import birt.ReportEngine;

import com.avaje.ebean.Ebean;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.util.Modules;

import common.Roles;

public class Global extends GlobalSettings {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalSettings.class);
    private static final String CONFIG_USE_MOCK_PAYMENT_GATEWAY = "epg.usemockgateway";
    private final Injector injector;
    public Global() {
        super();
        injector = Guice.createInjector(Modules.override(new ProposedSocietyModule()).with(new AbstractModule() {
        	@Override
        	protected void configure() {
        		String useMockGateay = System.getProperty(CONFIG_USE_MOCK_PAYMENT_GATEWAY);

        		if (!StringUtils.isTrivial(useMockGateay) && "Y".equals(useMockGateay)) {
        			LOG.warn("################ Mock payment gateway is enabled. Users will be able to simulate fake payments ######################");
        			bind(PaymentGateway.class).to(MockCCAvenuePaymentGateway.class).in(Singleton.class);
        		}
        	}
        }));
    }
    

    @Override
    public void onStart(Application app) {
    	// Note: We are forcing instantiation of Pincodes class to ensure that it loads the data 
    	// upfront instead of lazy loading when requested. Please maintain this piece of code here. 
    	injector.getInstance(Pincodes.class);
    	
    	ReportEngine reportEngine = injector.getInstance(ReportEngine.class);
    	
    	try {
			reportEngine.start();
		} catch (ReportEngineException e) {
			throw new RuntimeException("Not able to start birt engine");
		}
    	
    	BatchProcessor batchProcessor = injector.getInstance(BatchProcessor.class);
        UserDao userDao = injector.getInstance(DaoProvider.class).userDao();
        BueroDao bueroDao = injector.getInstance(DaoProvider.class).bueroDao();
        batchProcessor.start(); //Starting BatchProcessor
        
        User accountant = userDao.findUniqueByEmail("accountant@ps.com");
        if (accountant == null) {
            accountant = new User();
            accountant.name = "accountant";
            accountant.mobile = "00000007011";
            accountant.password = BCrypt.hashpw("accountant", BCrypt.gensalt());
            accountant.authcode_hash = BCrypt.hashpw("accountant", BCrypt.gensalt());
            accountant.email = "accountant@ps.com";
            accountant.roles = new ArrayList<Role>();
            accountant.roles.add(new Role(accountant, Roles.VISITOR));
            accountant.roles.add(new Role(accountant,Roles.ACCOUNTANT));
            accountant.form = new ApplicationForm();
            
            // accountant.form.user = accountant;
            accountant.form.formNumber = "6666";
            accountant.form.status = "Pending";
            accountant.joiningDate = new Date();
            accountant.lastLogin = new Date();
            Ebean.save(accountant);
            LOG.debug("Added dummy accountant user.");
        }

        User admin = userDao.findUniqueByEmail("admin@ps.com");
        if(admin != null && (!admin.hasRole(Roles.ADMIN))){
        		admin.roles.add(new Role(admin,Roles.ADMIN));
        		Ebean.save(admin);
        }
        
        if (admin == null) {
            admin = new User();
            admin.name = "Administrator";
            admin.mobile = "00000000001";
            admin.password = BCrypt.hashpw("admin", BCrypt.gensalt());
            admin.authcode_hash = BCrypt.hashpw("admin", BCrypt.gensalt());
            admin.email = "admin@ps.com";
            admin.roles = new ArrayList<Role>();
            admin.roles.add(new Role(admin, Roles.VISITOR));
            admin.roles.add(new Role(admin,Roles.ADMIN));
            admin.form = new ApplicationForm();
            
            // admin.form.user = admin;
            admin.form.formNumber = "XYZ";
            admin.form.status = "Pending";
            admin.joiningDate = new Date();
            admin.lastLogin = new Date();
            Ebean.save(admin);
            LOG.debug("Added dummy admin user.");
        }

        User visitor = userDao.findUniqueByEmail("visitor@ps.com");

        if (visitor == null) {
            visitor = new User();
            visitor.name = "Visitor";
            visitor.password = BCrypt.hashpw("visitor", BCrypt.gensalt());
            visitor.authcode_hash = BCrypt.hashpw("visitor", BCrypt.gensalt());
            visitor.email = "visitor@ps.com";
            visitor.mobile = "12345";
            visitor.roles = new ArrayList<Role>();
            visitor.roles.add(new Role(visitor, Roles.VISITOR));
            visitor.form = new ApplicationForm();
            // visitor.form.user = visitor;
            visitor.form.formNumber = "AXYZ";
            visitor.form.status = "Pending";
            visitor.joiningDate = new Date();
            visitor.lastLogin = new Date();
            Ebean.save(visitor);
            LOG.debug("Added dummy visitor.");
        }

        // Add manager for demonstrating Management Dashboard.
        User manager = userDao.findUniqueByEmail("manager@ps.com");

        if (manager == null) {
            manager = new User();
            manager.name = "Manager";
            manager.mobile = "12356";
            manager.password = BCrypt.hashpw("manager", BCrypt.gensalt());
            manager.authcode_hash = BCrypt.hashpw("manager", BCrypt.gensalt());
            manager.email = "manager@ps.com";
            manager.roles = new ArrayList<Role>();
            manager.roles.add(new Role(manager, Roles.MANAGER));
            manager.form = new ApplicationForm();
            manager.form.formNumber = "MXYZ";
            manager.form.status = "Pending";
            manager.joiningDate = new Date();
            manager.lastLogin = new Date();
            Ebean.save(manager);
            LOG.debug("Added dummy manager user.");
        }

        User hybrid = userDao.findUniqueByEmail("hybrid@ps.com");
        
        if (hybrid == null) {
            hybrid = new User();
            hybrid.name = "hybrid";
            hybrid.mobile = "12231333";
            hybrid.password = BCrypt.hashpw("hybrid", BCrypt.gensalt());
            hybrid.authcode_hash = BCrypt.hashpw("hybrid", BCrypt.gensalt());
            hybrid.email = "hybrid@ps.com";
            hybrid.roles = new ArrayList<Role>();
            hybrid.roles.add(new Role(hybrid, Roles.ADMIN));
            hybrid.roles.add(new Role(hybrid, Roles.VISITOR));
            hybrid.joiningDate = new Date();
            hybrid.lastLogin = new Date();
            Ebean.save(hybrid);
            LOG.debug("Added dummy hybrid user.");
        }
        
       //Bydefault Investigatiob Buero id & passord
     
        //Adding Role user BUERO
        User ibUser = userDao.findUniqueByEmail("buero@ps.com");
        if(ibUser == null){
        	ibUser = new User();
        	ibUser.name = "buero";
        	ibUser.mobile = "221221122";
        	ibUser.password = BCrypt.hashpw("buero", BCrypt.gensalt());
        	ibUser.authcode_hash = BCrypt.hashpw("buero", BCrypt.gensalt());
        	ibUser.email = "buero@ps.com";
        	ibUser.roles = new ArrayList<Role>();
        	ibUser.roles.add(new Role(ibUser, Roles.BUERO));
        	ibUser.roles.add(new Role(ibUser, Roles.VISITOR));
        	ibUser.joiningDate = new Date();
        	ibUser.lastLogin = new Date();
        	Ebean.save(ibUser);
        	//Adding User as Buero
        	Buero buero =new Buero();
        	buero.user =ibUser;
        	bueroDao.save(buero);
        	
            LOG.info("Added user as buero");
        }
       
        // Register a formatter for Date field.
        Formatters.register(Date.class, new SimpleFormatter<Date>() {
            private final ThreadLocal<SimpleDateFormat> dateFormatLocal = new ThreadLocal<SimpleDateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat("yyyy-MM-dd");
                }
            };
            
            @Override
            public Date parse(String value, Locale locale) throws ParseException {
                return value == null? null : dateFormatLocal.get().parse(value);
            }

            @Override
            public String print(Date value, Locale locale) {
                return value == null? null : dateFormatLocal.get().format(value);
            }
        });
    }

    @Override
	public void onStop(Application arg0) {
		super.onStop(arg0);
		Platform.shutdown();
	}

	@Override
    public <A> A getControllerInstance(Class<A> clazz) throws Exception {
        LOG.debug("Creating controller: " + clazz.getName());
        return injector.getInstance(clazz);
    }

}
