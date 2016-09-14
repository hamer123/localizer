package com.pw.localizer.model.upload;

/**
 * Created by wereckip on 14.09.2016.
 */
public class ImageFileUpload {

    private byte[] content;
    private String contentType;
    private long size;
    private String fileName;

    public ImageFileUpload() {
    }

    public ImageFileUpload(byte[] content, String contentType, long size, String fileName) {
        this.content = content;
        this.contentType = contentType;
        this.size = size;
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
