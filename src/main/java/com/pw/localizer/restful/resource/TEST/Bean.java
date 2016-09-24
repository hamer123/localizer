package com.pw.localizer.restful.resource.TEST;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by Patryk on 2016-09-24.
 */

@JsonSerialize(using = BeanSerializer.class)
public class Bean {

    private String name;
    @JsonIgnore
    private String name2;
    private String name3;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }
}
