package com.ezone.devops.pipeline.context;

import com.ezone.devops.pipeline.model.StageRecord;

public class StageRecordContext {

    private static final ThreadLocal<StageRecord> CTX_HOLDER = new ThreadLocal<>();

    public static void set(StageRecord jobRecord) {
        CTX_HOLDER.set(jobRecord);
    }

    public static StageRecord get() {
        return CTX_HOLDER.get();
    }

    public static void remove() {
        CTX_HOLDER.remove();
    }
}
