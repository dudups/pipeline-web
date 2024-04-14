package com.ezone.devops.pipeline.mq.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WebsocketEvent {
    RECORDS("records"),
    JOBS("jobs");

    @Getter
    private String type;

}
