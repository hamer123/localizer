package com.pw.localizer.service;

import java.awt.image.BufferedImage;

import javax.ejb.Local;

@Local
public interface ImageService extends ResourceService<String, BufferedImage> {
	byte[] content(String uuid);
}
