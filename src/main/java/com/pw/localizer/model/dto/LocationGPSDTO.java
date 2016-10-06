package com.pw.localizer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Patryk on 2016-10-02.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationGPSDTO extends LocationDTO {}
