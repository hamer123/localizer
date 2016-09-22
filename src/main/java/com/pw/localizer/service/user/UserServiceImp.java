package com.pw.localizer.service.user;

import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.model.enums.Roles;
import com.pw.localizer.repository.area.AreaRepository;
import com.pw.localizer.repository.user.UserRepository;
import com.pw.localizer.service.resource.image.ImageService;
import org.jboss.logging.Logger;

import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patryk on 2016-09-07.
 */

@Stateless
public class UserServiceImp implements UserService, Serializable{
    @Inject
    private UserRepository userRepository;
    @Inject
    private AreaRepository areaRepository;
    @Inject
    private ImageService avatarService;
    @Inject
    private Logger logger;

    @Override
    public User getUserFetchAreas(String login) {
        User user = userRepository.findByLogin(login);
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

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public User create(User user) {
        user.setRoles(createUserRoles());
        userRepository.create(user);
        return user;
    }

    @Override
    public User create(User user, InputStream avatarStream) {
        try {
            user.setRoles(createUserRoles());
            avatarService.create(user.getAvatar(),avatarStream);
            userRepository.create(user);
        } catch(Exception e){
            try{
                avatarService.remove(user.getAvatar());
            } catch(Exception e2){
                logger.error("Nie udalo sie usunac avatara " +
                             user.getAvatar().getUuid() +
                             " po nie udanej probie stworzenia uzytkownika");
            }
            logger.error("Blad przy probie utworzenia uzytkownika " + e);
            throw new EJBTransactionRolledbackException("Nie mozna utworzyc uzytkownika " + e);
        }
        return user;
    }

    private List<Roles> createUserRoles(){
        List<Roles>roles = new ArrayList<>();
        roles.add(Roles.USER);
        return roles;
    }
}
