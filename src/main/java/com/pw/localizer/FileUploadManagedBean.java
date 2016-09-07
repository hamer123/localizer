package com.pw.localizer;

import com.pw.localizer.service.ImageService;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;


import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by wereckip on 06.09.2016.
 */

@Named
@ApplicationScoped
public class FileUploadManagedBean implements Serializable{
    @Inject
    private ImageService imageService;

    UploadedFile file;
    byte[] content;
    String contentType;

    @PostConstruct
    public void post() throws IOException {
        this.content = this.imageService.content("51a87d8b-2520-49eb-84e7-f1f27bdb8869");//IOUtils.toByteArray(imageService.find("51a87d8b-2520-49eb-84e7-f1f27bdb8869"));
    }

    public StreamedContent getStreamedContent() throws IOException {
//        if(content != null) {
//            InputStream inputStream = new ByteArrayInputStream(content);
//            return new DefaultStreamedContent(inputStream, contentType);
//        }
//        return null;
        return new DefaultStreamedContent(new ByteArrayInputStream(this.imageService.content("51a87d8b-2520-49eb-84e7-f1f27bdb8869")), "image/jpeg");
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void fileUploadListener(FileUploadEvent e) throws IOException {
        // Get uploaded file from the FileUploadEvent
        InputStream inputStream = e.getFile().getInputstream();
        content = IOUtils.toByteArray(inputStream);
        contentType = e.getFile().getContentType();
        inputStream.close();
    }
}