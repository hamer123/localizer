package com.pw.localizer.service.impl;

import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.repository.AreaRepository;
import com.pw.localizer.repository.UserRepository;
import com.pw.localizer.service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by Patryk on 2016-09-07.
 */

@Stateless
public class UserServiceBean implements UserService{
    @Inject
    private UserRepository userRepository;
    @Inject
    private AreaRepository areaRepository;

    @Override
    public User getUserFetchAreas(String login) {
        User user = userRepository.findByLogin(login);
        //fetch areas
        List<Area> areas = user.getAreas();
        for(Area area : areas) area.getPoints().size();
        return user;
    }

    @Override
    public List<Area> getUserAreasFetchAreaPoints(long userId) {
        List<Area>areas = areaRepository.findByProviderId(userId);
        for(Area area : areas)
            area.getPoints().size();
        return areas;
    }
}
