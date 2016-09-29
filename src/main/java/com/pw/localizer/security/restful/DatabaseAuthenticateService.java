package com.pw.localizer.security.restful;

import com.pw.localizer.repository.user.UserRepository;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

/**
 * Created by wereckip on 18.08.2016.
 */

@Stateless
@DatabaseAuthenticat
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class DatabaseAuthenticateService implements AuthenticateService {
    @Inject
    private UserRepository userRepository;

    @Override
    public boolean authenticate(String login, String password){
        try{
            userRepository.findByLoginAndPassword(login, password);
            return true;
        } catch(Exception e){
            return false;
        }
    }
}
