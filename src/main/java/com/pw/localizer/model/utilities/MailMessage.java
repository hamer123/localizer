package com.pw.localizer.model.utilities;

/**
 * Created by Patryk on 2016-10-16.
 */
public class MailMessage {
    private String address;
    private String subject;
    private String context;

    public MailMessage(String address, String subject, String context) {
        this.address = address;
        this.subject = subject;
        this.context = context;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

}
