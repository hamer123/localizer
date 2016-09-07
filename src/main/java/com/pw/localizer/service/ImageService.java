package com.pw.localizer.service;

import com.pw.localizer.model.entity.Avatar;

import java.io.InputStream;

import javax.ejb.Local;

@Local
public interface ImageService extends ResourceService<Avatar, InputStream> {
	byte[] content(String uuid);
}
