package com.pw.localizer.service.message.area;

import javax.ejb.Local;

import com.pw.localizer.model.entity.AreaEvent;
import com.pw.localizer.model.utilities.MailMessage;

@Local
public interface MailMessageService {
	void send(MailMessage mailMessage);
}
