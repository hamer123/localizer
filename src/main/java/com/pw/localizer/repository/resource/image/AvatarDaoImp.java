package com.pw.localizer.repository.resource.image;

import com.pw.localizer.inceptor.DurationLogging;
import com.pw.localizer.singleton.ResourceDirectionStartup;
import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Patryk on 2016-10-03.
 */
public class AvatarDaoImp implements AvatarDao{
    @Inject
    private Logger logger;

    @Override
    public void create(InputStream inputStream, String uuid) throws IOException {
        OutputStream outputStream = new FileOutputStream(new File(path(uuid)));
        IOUtils.copy(inputStream,outputStream);
        inputStream.close();
        outputStream.close();
    }

    @Override
    public void update(InputStream inputStream, String uuid) throws IOException {
        OutputStream outputStream = new FileOutputStream(new File(path(uuid)));
        IOUtils.copy(inputStream,outputStream);
        inputStream.close();
        outputStream.close();
    }

    @Override
    public void remove(String uuid) {
        File avatarFile = new File(path(uuid));
        avatarFile.delete();
    }

    @Override
    @DurationLogging
    public InputStream find(String uuid) throws FileNotFoundException {
        return new FileInputStream(new File(path(uuid)));
    }

    @Override
    @DurationLogging
    public byte[] content(String uuid) {
        if(uuid == null || uuid == "") {
            return null; //throw new IllegalArgumentException("Nie poprawny format uuid " + uuid);
        }
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
}
