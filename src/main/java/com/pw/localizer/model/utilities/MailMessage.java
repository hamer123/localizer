package com.pw.localizer.model.utilities;

/**
 * Created by Patryk on 2016-10-16.
 */
public class MailMessage {
    private String address;
    private String topic;
    private String context;

    public MailMessage(String address, String topic, String context) {
        this.address = address;
        this.topic = topic;
        this.context = context;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

}
