package com.pw.localizer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicUserDTO {
    protected long id;
    protected String login;
    protected String email;
    protected String phone;
}
