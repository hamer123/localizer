package com.pw.localizer.security.restful;

import com.pw.localizer.model.entity.User;
import javax.ejb.Local;

/**
 * Created by wereckip on 18.08.2016.
 */

@Local
public interface AuthenticateService {
    User authenticate(String login, String password) throws AuthenticateExcpetion;
}
