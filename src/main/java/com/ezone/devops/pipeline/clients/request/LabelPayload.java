package com.ezone.devops.pipeline.clients.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelPayload {

    private String key;
    private String value;

}
