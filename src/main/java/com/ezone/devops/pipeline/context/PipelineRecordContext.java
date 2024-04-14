package com.ezone.devops.pipeline.context;

import com.ezone.devops.pipeline.model.PipelineRecord;

public class PipelineRecordContext {

    private static final ThreadLocal<PipelineRecord> CTX_HOLDER = new ThreadLocal<>();

    public static void set(PipelineRecord pipelineRecord) {
        CTX_HOLDER.set(pipelineRecord);
    }

    public static PipelineRecord get() {
        return CTX_HOLDER.get();
    }

    public static void remove() {
        CTX_HOLDER.remove();
    }
}
