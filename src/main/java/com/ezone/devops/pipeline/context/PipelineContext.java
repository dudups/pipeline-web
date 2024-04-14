package com.ezone.devops.pipeline.context;

import com.ezone.devops.pipeline.model.Pipeline;

public class PipelineContext {

    private static final ThreadLocal<Pipeline> CTX_HOLDER = new ThreadLocal<>();

    public static void set(Pipeline pipeline) {
        CTX_HOLDER.set(pipeline);
    }

    public static Pipeline get() {
        return CTX_HOLDER.get();
    }

    public static void remove() {
        CTX_HOLDER.remove();
    }

}
