package com.pw.localizer.service;

import com.pw.localizer.model.entity.User;
import javax.ejb.Local;

/**
 * Created by Patryk on 2016-09-07.
 */

@Local
public interface UserService {

    User getUserFetchAreas(String login);
}
