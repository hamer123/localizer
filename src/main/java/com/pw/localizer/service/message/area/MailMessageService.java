package com.pw.localizer.service.message.area;

import javax.ejb.Local;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.pw.localizer.model.entity.AreaEvent;
import com.pw.localizer.model.utilities.MailMessage;

@Local
public interface MailMessageService {
	void send(MailMessage mailMessage) throws MessagingException;
}
