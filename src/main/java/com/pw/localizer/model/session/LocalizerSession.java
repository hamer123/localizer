package com.pw.localizer.model.session;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.model.enums.Roles;

@Named
@SessionScoped
public class LocalizerSession implements Serializable{
	private User user;

	public boolean isInRole(Roles role){
		for(Roles _role : user.getRoles())
			if(_role.equals(role))
				return true;
		return false;
	}

	public boolean isLogged(){
		return user != null;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
