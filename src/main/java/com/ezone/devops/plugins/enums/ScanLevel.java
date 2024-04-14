package com.ezone.devops.plugins.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ScanLevel {
    BLOCK("阻塞"),
    HIGH("严重"),
    MIDDLE("中等"),
    LOW("提示");

    @Getter
    private String desc;
}
