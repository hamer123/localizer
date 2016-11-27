package com.pw.localizer.controller;

import com.pw.localizer.jsf.JsfMessageBuilder;
import com.pw.localizer.model.general.ImageFileUpload;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by wereckip on 07.09.2016.
 */

@Named
@SessionScoped
public class AvatarUploadController implements Serializable{
    private ImageFileUpload imageFileUpload;

    public void handleFileUpload(FileUploadEvent event){
        try{
            UploadedFile uploadedFile = event.getFile();
            InputStream inputStream = uploadedFile.getInputstream();
            this.imageFileUpload = new ImageFileUpload();
            this.imageFileUpload.setContent(IOUtils.toByteArray(inputStream));
            this.imageFileUpload.setContentType(uploadedFile.getContentType());
            this.imageFileUpload.setSize(uploadedFile.getSize());
            this.imageFileUpload.setFileName(uploadedFile.getFileName());
            inputStream.close();
            JsfMessageBuilder.infoMessage("Avatar zostal wybrany...");
        } catch (IOException e) {
            JsfMessageBuilder.errorMessage("Nie udalo sie dodac avatara!");
            e.printStackTrace();
        }
    }

    public StreamedContent getAvatarStreamedContent(){
        if(this.imageFileUpload == null)
            return null;
        return new DefaultStreamedContent(new ByteArrayInputStream(this.imageFileUpload.getContent()), this.imageFileUpload.getContentType());
    }

    public ImageFileUpload getImageFileUpload() {
        return imageFileUpload;
    }

    public void setImageFileUpload(ImageFileUpload imageFileUpload) {
        this.imageFileUpload = imageFileUpload;
    }
}

