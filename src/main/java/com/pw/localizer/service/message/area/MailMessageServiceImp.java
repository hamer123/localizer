package com.pw.localizer.service.message.area;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Session;

import com.pw.localizer.model.entity.AreaEvent;

@Stateless
public class MailMessageServiceImp implements MailMessageService {
	@Resource(name = "java:jboss/mail/localizer")
	private Session session;

	@Override
	public void send(AreaEvent areaEvent) {}

//	@Override
//	public void send(AreaEvent areaEvent) {
//		try {
//			sendMail(areaEvent);
//		} catch (MessagingException e) {
//			throw new AreaMialMessageException(e);
//		}
//	}
//
//	private String createSubject(AreaEvent areaEvent){
//		StringBuilder builder = new StringBuilder();
//		builder.append("Uzytkownik ")
//		       .append(areaEvent.getArea().getTarget().getLogin())
//		       .append( getSubjectPartFollowType(areaEvent.getArea()) );
//
//		return builder.toString();
//	}
//
//	private String getSubjectPartFollowType(Area area){
//		if(area.getAreaFollowType() == AreaFollow.INSIDE)
//			return " opuscil obszar sledzenia "
//			       + area.getName();
//		else
//			return " wszedl do obszaru sledzenia "
//		           + area.getName();
//	}
//
//	private String createMsg(AreaEvent areaEvent){
//		return   areaEvent.getMessage()
//		       + "\n"
//		       + areaEvent.getUrl();
//	}
//
//	private void sendMail(AreaEvent areaEvent) throws AddressException, MessagingException{
//		Message message = new MimeMessage(session);
//
//		String email = areaEvent.getArea().getProvider().getEmail();
//		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
//
//			message.setSubject( createSubject(areaEvent) );
//			message.setText( createMsg(areaEvent) );
//
//			Transport.send(message);
//
//	}

}
 