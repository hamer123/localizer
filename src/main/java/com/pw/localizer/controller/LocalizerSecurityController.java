package com.pw.localizer.controller;

import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import com.pw.localizer.jsf.JsfMessageBuilder;
import com.pw.localizer.model.session.LocalizerSession;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.repository.user.UserRepository;

@Getter
@Setter
@ViewScoped
@Named(value="localizerSecurityController")
public class LocalizerSecurityController implements Serializable{
	@Inject
	private UserRepository userRepository;
	@Inject
	private LocalizerSession localizerSession;

	private String login;

	private String password;
	
	public String login(){
		try{
			User user = userRepository.findByLoginAndPassword(login,password);
			localizerSession.setUser(user);
			return "/app/location.xhtml?faces-redirect=true";
		} catch (Exception e){
			JsfMessageBuilder.errorMessage("Invalid or unknown credentials");
			return null;
		}
	}
	
	public String logout(){
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/login.xhtml?faces-redirect=true";
	}
	
	public String redirectIfAlreadyLogged(){
		if(localizerSession.isLogged())
			return "/app/logout.xhtml?faces-redirect=true";
		return null;
	}
}
