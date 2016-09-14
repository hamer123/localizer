package com.pw.localizer.service.impl;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.pw.localizer.model.entity.Avatar;
import com.pw.localizer.repository.AvatarRepository;
import com.pw.localizer.service.ImageService;
import com.pw.localizer.singleton.ResourceDirectionStartup;
import org.apache.commons.io.IOUtils;

@Stateless
public class AvatarService implements ImageService, Serializable{
	@Inject
	AvatarRepository avatarRepository;

	@Override
	public byte[] content(String uuid) {
		String path = path(uuid);
		try {
			return Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String path(String uuid){
		return  ResourceDirectionStartup.ResourceDirectionURI.AVATAR
				+ "/"
				+ uuid ;
	}

	@Override
	public void create(Avatar avatar, InputStream inputStream) throws IOException {
		avatar.setUuid(UUID.randomUUID().toString());
		this.avatarRepository.create(avatar);
		OutputStream outputStream = new FileOutputStream(new File(path(avatar.getUuid())));
		IOUtils.copy(inputStream,outputStream);
		inputStream.close();
		outputStream.close();
	}

	@Override
	public void update(Avatar avatar, InputStream inputStream) throws IOException {
		OutputStream outputStream = new FileOutputStream(new File(path(avatar.getUuid())));
		IOUtils.copy(inputStream,outputStream);
		inputStream.close();
		outputStream.close();
	}

	@Override
	public void remove(Avatar avatar) {
		this.avatarRepository.delete(this.avatarRepository.findById(avatar.getId()));
		File avatarFile = new File(path(avatar.getUuid()));
		avatarFile.delete();
	}

	@Override
	public InputStream find(Avatar avatar) throws FileNotFoundException {
		return new FileInputStream(new File(path(avatar.getUuid())));
	}
}
