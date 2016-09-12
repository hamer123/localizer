package com.pw.localizer.singleton;

import com.pw.localizer.model.entity.Avatar;
import com.pw.localizer.service.ImageService;
import com.pw.localizer.service.impl.AvatarService;
import org.primefaces.model.StreamedContent;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Patryk on 2016-09-11.
 */

@Named
@ApplicationScoped
public class AvatarApplicationService {
    @Inject
    private ImageService avatarService;

    public StreamedContent streamedContent(Avatar avatar){
        return null;
    }

    public byte[] content(String uuid){
        return avatarService.content(uuid);
    }
}
