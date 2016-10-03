package com.pw.localizer.service.resource.image;

import com.pw.localizer.model.entity.Avatar;
import com.pw.localizer.service.resource.ResourceService;

import java.io.InputStream;

import javax.ejb.Local;

@Local
public interface ImageService extends ResourceService<Avatar, InputStream> {}
