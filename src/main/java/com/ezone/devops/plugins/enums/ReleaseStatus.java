package com.ezone.devops.plugins.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ReleaseStatus {

    NONE("none"),
    FAILED("failed"),
    SUCCESS("success"),
    OVERRIDE("override");

    @Getter
    private String status;
}
