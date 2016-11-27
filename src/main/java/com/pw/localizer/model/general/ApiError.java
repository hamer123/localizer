package com.pw.localizer.model.general;

import java.util.Arrays;
import java.util.List;
import static javax.ws.rs.core.Response.Status;

/**
 * Created by Patryk on 2016-10-30.
 */

public class ApiError {
    private String message;
    private Status status;
    private List<String> errors;

    public ApiError(Status status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(Status status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }

    public ApiError(){}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
