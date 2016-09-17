package com.pw.localizer.service.resource.image;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.pw.localizer.inceptor.DurationLogging;
import com.pw.localizer.model.entity.Avatar;
import com.pw.localizer.repository.AvatarRepository;
import com.pw.localizer.service.resource.image.ImageService;
import com.pw.localizer.singleton.ResourceDirectionStartup;
import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;

@Stateless
public class AvatarService implements ImageService, Serializable{
	@Inject
	private AvatarRepository avatarRepository;
	@Inject
	private Logger logger;

	@Override
	@DurationLogging
	public byte[] content(String uuid) {
		if(uuid == null || uuid == "") return null;
//			throw new IllegalArgumentException("Nie poprawny format uuid " + uuid);

		byte[] content = null;
		try {
			String path = path(uuid);
			content = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			logger.error(e);
		}
		return content;
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
	@DurationLogging
	public InputStream find(Avatar avatar) throws FileNotFoundException {
		return new FileInputStream(new File(path(avatar.getUuid())));
	}
}
