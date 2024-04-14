package com.ezone.devops.pipeline.context;

import com.ezone.devops.pipeline.model.JobRecord;

public class JobRecordContext {

    private static final ThreadLocal<JobRecord> CTX_HOLDER = new ThreadLocal<>();

    public static void set(JobRecord jobRecord) {
        CTX_HOLDER.set(jobRecord);
    }

    public static JobRecord get() {
        return CTX_HOLDER.get();
    }

    public static void remove() {
        CTX_HOLDER.remove();
    }
}
