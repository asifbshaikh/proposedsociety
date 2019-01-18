package utils;


import static utils.CryptUtils.decrypt;
import static utils.CryptUtils.desalt;
import static utils.CryptUtils.encrypt;
import static utils.CryptUtils.salt;
import static utils.StringUtils.isTrivial;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.Adler32;

import models.Invoice;
import models.Payment;
import models.dao.DaoProvider;
import models.dao.InvoiceDao;
import models.dao.PaymentDao;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Controller;
import play.mvc.Result;
import utils.PaymentStatus.Status;

import com.ccavenue.security.AesCryptUtil;
import com.google.inject.Inject;

import common.ApplicationConstants;
import common.ConfigUtils;

import controllers.routes;

public class CCAvenuePaymentGateway implements PaymentGateway {
    protected static final Logger LOG = LoggerFactory.getLogger(CCAvenuePaymentGateway.class);

    private static final String CONFIG_EPG_MID = "epg.mid";
    
    private String CONFIG_REDIRECTION_DOMAIN = ApplicationConstants.CONFIG_REDIRECTION_DOMAIN;
    private static final String CONFIG_EPG_KEY_FILE = "epg.keyfile";
    
    private static final String PAYMENT_GATEWAY_ACTION_URL = "http://www.ccavenue.com/shopzone/cc_details.jsp";
    
    protected static final String CCAV_PARAM_ENC_RESPONSE = "encResponse";
    protected static final String CCAV_PARAM_MERCHANT_ID = "Merchant_Id";
    protected static final String CCAV_PARAM_AMOUNT = "Amount";
    protected static final String CCAV_PARAM_ORDER_ID = "Order_Id";
    protected static final String CCAV_PARAM_REDIRECT_URL = "Redirect_Url";
    protected static final String CCAV_PARAM_CHECKSUM = "Checksum";
    
    protected static final String CCAV_PARAM_AUTH_DESC = "AuthDesc";
    
    protected static final String CCAV_PARAM_BILLING_CUST_NAME = "billing_cust_name";
    protected static final String CCAV_PARAM_BILLING_CUST_ADDRESS = "billing_cust_address";
    protected static final String CCAV_PARAM_BILLING_CUST_COUNTRY = "billing_cust_country";
    protected static final String CCAV_PARAM_BILLING_CUST_TEL = "billing_cust_tel";
    protected static final String CCAV_PARAM_BILLING_CUST_EMAIL = "billing_cust_email";
    protected static final String CCAV_PARAM_BANK_NAME = "bank_name";
    protected static final String CCAV_PARAM_NB_BID = "nb_bid";
    protected static final String CCAV_PARAM_CARD_CATEGORY = "card_category";
    protected static final String CCAV_PARAM_NB_ORDER_NO  = "nb_order_no";

    protected static final String CCAV_VAL_AUTHDESC_SUCCESS = "Y";
    protected static final String CCAV_VAL_AUTHDESC_FAILURE = "N";
    

    protected final TransactionIdGenerator idgen;
    protected final PaymentDao paymentDao;
    protected final InvoiceDao invoiceDao;
    private final String merchantId;
    private final String key;
    private final String redirectionDomain;

    @Inject
    public CCAvenuePaymentGateway(DaoProvider provider, TransactionIdGenerator idgen) {
        this.merchantId = ConfigUtils.getConfigString(CONFIG_EPG_MID, true);
        this.key = readKey(ConfigUtils.getConfigString(CONFIG_EPG_KEY_FILE, true));
        this.redirectionDomain = removeTrailingSlash(ConfigUtils.getConfigString(CONFIG_REDIRECTION_DOMAIN, true));
        this.idgen = idgen;
        this.paymentDao = provider.paymentDao();
        this.invoiceDao = provider.invoiceDao();
    }

    private String readKey(String keyFile) {
        try {
            return FileUtils.readFileToString(new File(keyFile));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read key from file", e);
        }
    }

    private String removeTrailingSlash(String url) {
        return url.trim().endsWith("/") ? url.trim().substring(0, url.length()) : url;
    }
    
    @Override
    public Result redirectToBank(String secret, String redirectionUrl, String remoteAddress, String userAgent, Locale locale) throws PaymentGatewayError {
        return Controller.ok(views.html.payments.redirect.render(PAYMENT_GATEWAY_ACTION_URL, identifyPaymentAndObtainParameters(secret, redirectionUrl)));
    }
    
    protected Map<String, String> identifyPaymentAndObtainParameters(String secret, String redirectionUrl) throws PaymentGatewayError {
        Long paymentId = decryptPaymentId(secret);
        
        Payment payment = paymentDao.findById(paymentId);
        
        if (payment == null) {
            throw new PaymentGatewayError("Payment with id: " + paymentId + " not found");
        }
        
        if (payment.clientProcessed) {
            throw new PaymentGatewayError("Payment with id: " + paymentId + " already processed");
        }

        LOG.debug("Payment: " + payment);
        LOG.debug("Invoice: " + payment.invoice);
        LOG.debug("Amount: " + payment.invoice.amount);

        String orderId = payment.transactionId;
        String amount = payment.invoice.amount.toPlainString();
        String redirectUrl = redirectionDomain + redirectionUrl;
        String checksum = getChecksum(merchantId, orderId, amount, redirectUrl, key);
        
        Map<String, String> params = new HashMap<String, String>();
 
        params.put(CCAV_PARAM_MERCHANT_ID, merchantId);
        params.put(CCAV_PARAM_ORDER_ID, orderId);
        params.put(CCAV_PARAM_AMOUNT, amount);
        params.put(CCAV_PARAM_REDIRECT_URL, redirectUrl);
        params.put(CCAV_PARAM_CHECKSUM, checksum);
        
        return params;
    }
    
    @Override
    public Result redirectToClient(PaymentStatus status) throws PaymentGatewayError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("secret", encryptPaymentId(status.getPayment()));
        return Controller.ok(views.html.payments.redirect.render(status.getPayment().redirectUrl, params));
    }

    @Override
    public Payment clientProcessPayment(String encryptedData) throws PaymentGatewayError {
        LOG.debug("Client processing of the payment.");

        Long paymentId = decryptPaymentId(encryptedData);
        
        if (paymentId == null) {
            throw new PaymentGatewayError("Invalid Payment Id: " + paymentId);
        }
        
        Payment payment = paymentDao.findById(paymentId);
        if (payment == null) {
            throw new PaymentGatewayError("Payment with id: " + paymentId + " not found");
        }
        
        if (!payment.gatewayProcessed) {
            throw new PaymentGatewayError("Payment with id: " + paymentId + " is in invalid state. Gateway has not marked the payment as processed.");
        }

        if (payment.clientProcessed) {
            throw new PaymentGatewayError("Payment with id: " + paymentId + " is in invalid state. Client has already processed this payment.");
        }
        
        payment.clientProcessed = true;
        payment.completionTime = new Date();
        
        paymentDao.save(payment);

        return payment;
    }

    @Override
    public Result initiateFromClient(Invoice invoice, String redirectUrl) throws PaymentGatewayError {
        if (invoice.paid) {
            throw new PaymentGatewayError("Invoice bearing Invoice # " + invoice.invoiceNumber + " is already paid!");
        }

        // Create a payment within the system.
        Payment payment = createAndSavePayment(invoice, redirectUrl);
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("secret", encryptPaymentId(payment));
        
        return Controller.ok(views.html.payments.redirect.render(routes.PaymentController.redirectToBank().url(), params));
    }

    private String encryptPaymentId(Payment payment) {
        String salted = salt(String.valueOf(payment.id));
        String encrypted = encrypt(salted);
        
        LOG.debug("Payment Id: " + payment.id + " salted: " + salted + " encrypted: " + encrypted);
         
        return encrypted;
    }

    private Long decryptPaymentId(String secret) throws PaymentGatewayError {
        String decrypted = decrypt(secret);
        String id = desalt(decrypt(secret));

        LOG.debug("Payment secret: " + secret + " decrypted: " + decrypted + " desalted: " + id);
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException nfe) {
            throw new PaymentGatewayError("Invalid Payment ID: " + id);
        }
    }


    public Payment createAndSavePayment(Invoice invoice, String redirectUrl) {
        Payment payment = new Payment();
        payment.invoice = invoice;
        payment.startTime = new Date(); // current time
        payment.transactionId = idgen.generateTxnId();
        payment.redirectUrl = redirectUrl;
        payment.clientProcessed = false;
        // TODO: Introduce status and set here.
        
        paymentDao.save(payment);

        return payment;
    }

    @Override
    public PaymentStatus processPayment(Map<String, String> data) throws PaymentGatewayError {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Parameters received from CCAvenue: " + data);
        }

        String responseCipher = data.get(CCAV_PARAM_ENC_RESPONSE);
        
        if (responseCipher == null) {
            throw new PaymentGatewayError("Missing encrypted data parameter.");
        }
        
        String response = decryptCCAvenueResponse(responseCipher);
        
        if(LOG.isDebugEnabled()) {
            LOG.debug("Decrypted response: " + response);
        }
        
        Map<String, String> params = decodeParams(response);
        
        String orderId = params.get(CCAV_PARAM_ORDER_ID);
        String amount = params.get(CCAV_PARAM_AMOUNT);
        String checksum = params.get(CCAV_PARAM_CHECKSUM);
        String authDesc = params.get(CCAV_PARAM_AUTH_DESC);

        if (isTrivial(orderId) || isTrivial(amount) || isTrivial(checksum) || isTrivial(authDesc)) {
            throw new PaymentGatewayError("Missing mandatory parameters.");
        }
        
        if (!checksumValid(merchantId, orderId, amount, authDesc, key, checksum)) {
            throw new PaymentGatewayError("Invalid Checksum");
        }

        Payment payment = paymentDao.findByMerchantTxnId(orderId);
        if (payment == null) {
            throw new PaymentGatewayError("Payment with payment id: " + orderId + " does not exist.");
        }
        
        if (value(payment.clientProcessed) || value(payment.gatewayProcessed) || value(payment.invoice.paid)) {
            throw new PaymentGatewayError("Payment with payment id: " + orderId
                    + " found in invalid state. Client Processed: " + payment.clientProcessed
                    + " Gateway Processed: " + payment.gatewayProcessed + " Paid: " + payment.invoice.paid, payment);
        }

        payment.authDesc = authDesc;
        payment.gatewayProcessed = true;

        PaymentStatus.Status status = null;
        
        if (CCAV_VAL_AUTHDESC_SUCCESS.equals(authDesc)) {
        	
            payment.invoice.paid = true;
            payment.bankName = params.get(CCAV_PARAM_BANK_NAME);
            payment.nbBid = params.get(CCAV_PARAM_NB_BID);
            payment.paymentMode = params.get(CCAV_PARAM_CARD_CATEGORY);
            payment.nbOrderNo = params.get(CCAV_PARAM_NB_ORDER_NO);
            payment.success = true;
            status = Status.SUCCESS;
            
        } else {
            payment.invoice.paid = false;
            payment.success = false;
            status = Status.FAILURE;
        }

        LOG.info("Payment with payment id: " + payment.id + " towards invoice bearing invoice number: "
                + payment.invoice.invoiceNumber + " was: " + status);

        paymentDao.save(payment);
        // Invoice needs to be separately saved as we marked invoice as paid.
        invoiceDao.save(payment.invoice);
        
        // TODO: Read the address parameters, make provision in payment to store it, and save them in database.
            
        return new PaymentStatus(status, payment);
    }
    
    protected static boolean value(Boolean value) {
        return value == null ? false : value.booleanValue();
    }
     
    public String composeRedirectionUrl(String url) {
        return redirectionDomain + url;
    }

    private Map<String, String> decodeParams(String response) {
        StringTokenizer tokenizer = new StringTokenizer(response, "&");
        HashMap<String, String> params = new HashMap<String, String>();
        String pair=null, key=null, value=null;
        while (tokenizer.hasMoreTokens()) {
            pair = tokenizer.nextToken();
            if(pair!=null) {
                StringTokenizer strTok=new StringTokenizer(pair, "=");
                key=""; value="";
                if(strTok.hasMoreTokens()) {
                    key = strTok.nextToken();
                    if(strTok.hasMoreTokens())
                        value = strTok.nextToken();
                    params.put(key, value);
                }
            }
        }
        
        return params;
    }
 
    private String decryptCCAvenueResponse(String cipher) {
        return new AesCryptUtil(key).decrypt(cipher);     
    }

    private boolean checksumValid(String merchantId , String orderId, String amount, String authDesc, String workingKey, String checkSum) {
        String str = merchantId + "|" + orderId + "|" + amount + "|" + authDesc + "|" + workingKey;
        Adler32 adl = new Adler32();
        adl.update(str.getBytes());
        String newChecksum = String.valueOf(adl.getValue());
        return newChecksum.equals(checkSum);
    }

    private String getChecksum(String merchantId, String orderId, String amount, String redirectUrl, String workingKey) {
        String str = merchantId + "|" + orderId + "|" + amount + "|" + redirectUrl + "|" + workingKey;
        Adler32  adl = new Adler32();
        adl.update(str.getBytes());
        return String.valueOf(adl.getValue());
    }
}
