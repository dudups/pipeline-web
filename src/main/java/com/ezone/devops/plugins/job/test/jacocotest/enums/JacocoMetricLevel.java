package com.ezone.devops.plugins.job.test.jacocotest.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JacocoMetricLevel {

    INSTRUCTIONS_COVERAGE("指令覆盖率"),
    LINE_COVERAGE("行覆盖率"),
    BRANCH_COVERAGE("分支覆盖率"),
    METHOD_COVERAGE("方法覆盖率"),
    CLASS_COVERAGE("类覆盖率");

    private String type;
}
