package com.pw.localizer.security.restful;

import com.pw.localizer.model.entity.User;
import com.pw.localizer.repository.UserRepository;

import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

/**
 * Created by wereckip on 18.08.2016.
 */

@Stateless
@DatabaseAuthenticat
public class DatabaseAuthenticateService implements AuthenticateService {
    @Inject
    private UserRepository userRepository;

    @Override
    public User authenticate(String login, String password) throws AuthenticateExcpetion {
        try{
            User user = userRepository.findByLoginAndPassword(login, password);
            return user;
        } catch(EJBTransactionRolledbackException e){
            if(e.getCause() instanceof NoResultException)
                throw new AuthenticateExcpetion("Invalid login or password");

            throw new RuntimeException(e.getCause());
        }
    }
}
