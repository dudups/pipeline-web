package com.ezone.devops.pipeline.common;

public enum BuildStatus {

    RUNNING,
    SUCCESS,
    FAIL,
    ABORT,
    PENDING,
    WAITING,
    SKIP,
    CANCEL;

    public static boolean isEnd(BuildStatus status) {
        return SUCCESS == status || FAIL == status || CANCEL == status || ABORT == status || SKIP == status;
    }

    public static boolean isFailed(BuildStatus status) {
        return FAIL == status;
    }

    public static boolean isRunning(BuildStatus status) {
        return RUNNING == status;
    }
}
