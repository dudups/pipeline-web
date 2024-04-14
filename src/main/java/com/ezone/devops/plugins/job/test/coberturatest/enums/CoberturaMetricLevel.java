package com.ezone.devops.plugins.job.test.coberturatest.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CoberturaMetricLevel {

    LINE_COVERAGE("行覆盖率"),
    BRANCH_COVERAGE("分支覆盖率");

    private String type;
}
