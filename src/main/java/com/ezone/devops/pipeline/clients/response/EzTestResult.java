package com.ezone.devops.pipeline.clients.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class EzTestResult {

    private Long planId;
    private Long planSeqNum;
    private String spaceKey;
}
