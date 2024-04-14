package com.ezone.devops.plugins.job.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ImageVersionType {

    FIXED("fixed"),
    LATEST("latest");

    @Getter
    private String type;

}
