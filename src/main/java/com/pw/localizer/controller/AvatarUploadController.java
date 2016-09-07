package com.pw.localizer.controller;

import com.pw.localizer.jsf.utilitis.JsfMessageBuilder;
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
    private UploadedFile uploadedFile;
    private byte[] avatarContent;

    public byte[] getAvatarContent() {
        return avatarContent;
    }

    public String getContentType() {
        return this.uploadedFile.getContentType();
    }

    public String getName() {
        return this.uploadedFile.getFileName();
    }

    public Long getSize() {
        return this.uploadedFile.getSize();
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public void handleFileUpload(FileUploadEvent event){
        try{
            this.uploadedFile = event.getFile();
            InputStream inputStream = this.uploadedFile.getInputstream();
            this.avatarContent = IOUtils.toByteArray(inputStream);
            inputStream.close();
            JsfMessageBuilder.infoMessage("Avatar zostal wybrany...");
        } catch (IOException e) {
            JsfMessageBuilder.errorMessage("Nie udalo sie dodac avatara!");
            e.printStackTrace();
        }
    }

    public StreamedContent getAvatarStreamedContent(){
        if(this.avatarContent == null)
            return null;
        return new DefaultStreamedContent(new ByteArrayInputStream(this.avatarContent), this.uploadedFile.getContentType());
    }
}
