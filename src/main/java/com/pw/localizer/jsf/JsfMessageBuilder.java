package com.pw.localizer.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class JsfMessageBuilder {

	public static void errorMessage(String msg){		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public static void infoMessage(String msg){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public static void warnMessage(String msg){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
}
