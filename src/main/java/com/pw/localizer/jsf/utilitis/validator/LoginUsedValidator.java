package com.pw.localizer.jsf.utilitis.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

import com.pw.localizer.repository.user.UserRepository;

@FacesValidator(value = "loginUsedValidator")
public class LoginUsedValidator implements Validator{
	@Inject
	private UserRepository userRepository;
	
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String login = (String)value;
		boolean exist = userRepository.isLoginExist(login);
		if(exist){
			FacesMessage msg = new FacesMessage("Login: Login jest juz zajety");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
}
