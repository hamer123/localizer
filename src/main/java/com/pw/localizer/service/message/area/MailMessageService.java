package com.pw.localizer.service.message.area;

import javax.ejb.Local;
import javax.mail.MessagingException;

import com.pw.localizer.model.general.MailMessage;

@Local
public interface MailMessageService {
	void send(MailMessage mailMessage) throws MessagingException;
}
