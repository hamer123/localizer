package com.pw.localizer.singleton;

import com.pw.localizer.repository.resource.image.AvatarDao;
import com.pw.localizer.repository.resource.image.AvatarDaoImp;
import com.pw.localizer.model.entity.Avatar;
import org.primefaces.model.StreamedContent;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Patryk on 2016-09-11.
 */

@Named
@ApplicationScoped
public class AvatarApplicationService {
    @Inject
    private AvatarDao avatarDao;

    public StreamedContent streamedContent(Avatar avatar){
        return null;
    }

    public byte[] content(String uuid){
        return avatarDao.content(uuid);
    }
}
