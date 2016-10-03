package com.pw.localizer.service.resource.image;
import java.io.*;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.pw.localizer.repository.resource.image.AvatarDaoImp;
import com.pw.localizer.model.entity.Avatar;
import com.pw.localizer.repository.avatar.AvatarRepository;
import org.jboss.logging.Logger;

@Stateless
public class AvatarService implements ImageService, Serializable{
	@Inject
	private AvatarRepository avatarRepository;
	@Inject
	private AvatarDaoImp avatarDao;
	@Inject
	private Logger logger;

	@Override
	public void create(Avatar avatar, InputStream inputStream) throws IOException {
		avatar.setUuid(UUID.randomUUID().toString());
		avatarRepository.create(avatar);
		avatarDao.create(inputStream, avatar.getUuid());
	}

	@Override
	public void update(Avatar avatar, InputStream inputStream) throws IOException {
		avatarRepository.save(avatar);
		avatarDao.update(inputStream,avatar.getUuid());
	}

	@Override
	public void remove(Avatar avatar) {
		this.avatarRepository.delete(this.avatarRepository.findById(avatar.getId()));
		avatarDao.remove(avatar.getUuid());
	}
}
