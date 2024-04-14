package com.ezone.devops.pipeline.mq.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsocketEventMessage {

    private String appName;
    private String eventName;
    private Object key;
    private Object data;
}
