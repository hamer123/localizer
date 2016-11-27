package com.pw.localizer.exception;

/**
 * Created by Patryk on 2016-09-22.
 */
public class LocationServiceNotSupportedException extends RuntimeException {

    public LocationServiceNotSupportedException(String msg){
        super(msg);
    }
}
