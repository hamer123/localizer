package com.pw.localizer.controller;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.logging.Logger;
import com.pw.localizer.jsf.utilitis.JsfMessageBuilder;
import com.pw.localizer.model.session.LocalizerSession;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.repository.user.UserRepository;

@ViewScoped
@Named(value="localizerSecurityController")
public class LocalizerSecurityController implements Serializable{
	@Inject
	private UserRepository userRepository;
	@Inject
	private LocalizerSession localizerSession;

	private String login;
	private String password;

	Logger logger = Logger.getLogger(LocalizerSession.class);
	
	public String login(){
		try{
			User user = userRepository.findByLoginAndPassword(login,password);
			localizerSession.setUser(user);
			return "/app/location.xhtml?faces-redirect=true";
		} catch (Exception e){
			JsfMessageBuilder.errorMessage("Invalid or unknown credentials");
			logger.info("[LocalizerSecurityController] Nie udana proba logowania na konto " + login);
			return null;
		}
	}
	
	
	public String logout(){
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		logger.debug("[LocalizerSecurityController] logout successful for " + login);
		return "/login.xhtml?faces-redirect=true";
	}
	
	public String redirectIfAlreadyLogged(){
		if(localizerSession.isLogged())
			return "/app/logout.xhtml?faces-redirect=true";
		return null;
	}

	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


}
