package com.ezone.devops.pipeline.common;

import lombok.Data;

@Data
public class BlockInfo {

    private boolean blocked;
    private String reason;

}
