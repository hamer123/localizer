package com.pw.localizer.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=false)
public class GeocodeResponseException extends RuntimeException{
	public GeocodeResponseException(String msg){
		super(msg);
	}
}
