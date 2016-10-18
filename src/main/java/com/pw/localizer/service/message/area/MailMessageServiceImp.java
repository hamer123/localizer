package com.pw.localizer.service.message.area;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.pw.localizer.model.utilities.MailMessage;

@Stateless
public class MailMessageServiceImp implements MailMessageService {

	@Resource(name = "java:jboss/mail/lokalizator")
	private Session session;

	@Override
	public void send(MailMessage mailMessage) throws MessagingException {
		Message message = new MimeMessage(session);
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailMessage.getAddress()));
		message.setSubject(mailMessage.getSubject());
		message.setText(mailMessage.getContext());
		Transport.send(message);
	}
}
 