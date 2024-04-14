package com.ezone.devops.pipeline.mq.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ResultType {

    START("开始"),
    SUCCESS("成功"),
    FAIL("失败");

    @Getter
    private String text;
}
