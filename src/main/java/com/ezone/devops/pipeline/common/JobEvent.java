package com.ezone.devops.pipeline.common;

public enum JobEvent {
    START,
    UPDATE,
    SUCCESS,
    FAIL,
    ABORT,
    CANCEL;

    public static boolean isEnd(JobEvent jobEvent) {
        return SUCCESS == jobEvent || FAIL == jobEvent || CANCEL == jobEvent || ABORT == jobEvent;
    }

}
