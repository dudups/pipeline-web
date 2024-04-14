package com.ezone.devops.plugins.job.test.junittest.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JunitMetricLevel {

    PASS_RATE("通过率");

    private String type;
}
