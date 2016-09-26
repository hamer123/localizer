package com.pw.localizer.model.dto;

import com.pw.localizer.model.entity.Avatar;

import java.util.Set;

/**
 * Created by Patryk on 2016-09-26.
 */
public class UserDTO {

    private long id;
    private String login;
    private String email;
    private String phone;
    private Avatar avatar;
    private UserLastLocationsDTO userLastLocationsDTO;
    private Set<AreaDTO> areaDTOSet;
}
