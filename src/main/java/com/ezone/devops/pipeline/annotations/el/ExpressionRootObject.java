package com.ezone.devops.pipeline.annotations.el;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExpressionRootObject {

    private final Object object;
    private final Object[] args;

}
