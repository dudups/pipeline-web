package com.ezone.devops.pipeline.clients.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class EzScanCancelPayload {

    private String companyName;
    private String userName;
    private Long projectId;

}
