package com.pw.localizer.service.user;

import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.User;
import javax.ejb.Local;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Patryk on 2016-09-07.
 */

@Local
public interface UserService {

    User getUserFetchAreas(String login);
    List<Area> getUserAreasFetchAreaPoints(long userId);
    User update(User user);
    User create(User user);
    User create(User user, InputStream avatarStream);
}
