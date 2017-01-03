package com.pw.localizer.service.user;

import com.pw.localizer.model.entity.User;
import javax.ejb.Local;
import java.io.InputStream;

@Local
public interface UserService {

    User getUserFetchArea(String login);
    User update(User user);
    User create(User user);
    User create(User user, InputStream avatarStream);
}
