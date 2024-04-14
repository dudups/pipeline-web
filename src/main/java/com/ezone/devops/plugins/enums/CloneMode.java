package com.ezone.devops.plugins.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CloneMode {

    SINGLE_COMMIT("SINGLE_COMMIT"),
    SINGLE_BRANCH("SINGLE_BRANCH"),
    FULL_CLONE("FULL_CLONE"),
    NOT_CLONE("NOT_CLONE");

    @Getter
    private String mode;
}
