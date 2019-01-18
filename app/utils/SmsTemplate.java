package utils;

public enum SmsTemplate {
    SEND_AUTHCODE("15162"),
    APPLICANT_PASSWORD("26767"),
    REFER_A_FRIEND("26903");

    private final String templateId;
    
    private SmsTemplate(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateId() {
        return templateId;
    }
}
