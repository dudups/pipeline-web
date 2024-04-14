package com.ezone.devops.plugins.job.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum VersionType {

    DEFAULT_VERSION("default_version"),
    SNAPSHOT("snapshot"),
    RELEASE("release"),
    CUSTOM_VERSION("custom_version");

    @Getter
    private String type;

}
