package controllers;

import java.util.Date;

import models.PaymentCode;
import models.dao.DaoProvider;
import models.dao.PaymentCodeDao;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import security.auth.AccountantAuthenticator;
import utils.Mail;
import utils.Notifier;
import utils.PaymentCodeGenerator;
import utils.SmsGateway;
import views.html.accountant.email;
import views.html.accountant.newPaymentCode;
import views.html.accountant.view;

import com.google.inject.Inject;

@Authenticated(value = AccountantAuthenticator.class)
public class PaymentCodeController extends AuthenticatedUserController {
    private final PaymentCodeDao paymentCodeDao;
    private final PaymentCodeGenerator codegen;

    @Inject
    public PaymentCodeController(DaoProvider provider, PaymentCodeGenerator codegen, SmsGateway smsGateway) {
        super(provider.userDao());                                                                                                                                                                                                                                                                                      
        this.paymentCodeDao = provider.paymentCodeDao();
        this.codegen = codegen;
    }

    public Result newPaymentCode() {
        return ok(newPaymentCode.render(currentUser(), Form.form(PaymentCode.class)));
    }

    public Result newPaymentCodeSubmit() {
        Form<PaymentCode> form = Form.form(PaymentCode.class).bindFromRequest();

        if (form.hasErrors()) {
            return badRequest(newPaymentCode.render(currentUser(), form));
        }

        PaymentCode code = form.get();
        code.generationTime = new Date();
        code.used = false;
        code.code = codegen.generate();

        paymentCodeDao.save(code);
        
		Mail mail = new Mail();
		mail.setSender("noreply@proposedsociety.com");
		mail.setRecipient(code.email);
		mail.setSubject("Proposed Society payment code");
		mail.setBody(email.render(code).toString());
		Notifier.sendHtmlEmail(mail);
        // Sending SMS to DND number needs creation of template on the side of SMS gateway provider.
//        try {
//            smsGateway.send(code.mobile, sms.render(code).toString());
//        } catch (SmsGatewayException e) {
//            LOG.error("Failed to send message", e);
//        }
        
        return redirect(routes.PaymentCodeController.view(code.id));
    }
    
    public Result view(Long id) {
        PaymentCode code = paymentCodeDao.findById(id);
        return ok(view.render(currentUser(), code));
    }
}
