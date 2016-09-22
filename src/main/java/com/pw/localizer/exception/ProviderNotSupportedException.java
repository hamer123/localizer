package com.pw.localizer.exception;

import javax.ejb.ApplicationException;

@ApplicationException()
public class ProviderNotSupportedException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public ProviderNotSupportedException(String msg) {
		super(msg);
	}
}
