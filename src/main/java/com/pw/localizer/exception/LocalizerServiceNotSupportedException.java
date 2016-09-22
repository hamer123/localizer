package com.pw.localizer.exception;

/**
 * Created by Patryk on 2016-09-22.
 */
public class LocalizerServiceNotSupportedException extends RuntimeException {

    public LocalizerServiceNotSupportedException(String msg){
        super(msg);
    }
}
