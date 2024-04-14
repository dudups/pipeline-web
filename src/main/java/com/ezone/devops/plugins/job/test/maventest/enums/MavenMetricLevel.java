package com.ezone.devops.plugins.job.test.maventest.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MavenMetricLevel {

    SUCCESS_RATE("成功率");

    private String type;
}
