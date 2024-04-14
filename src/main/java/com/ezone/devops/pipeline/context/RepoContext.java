package com.ezone.devops.pipeline.context;

import com.ezone.devops.pipeline.vo.RepoVo;

public class RepoContext {

    private static final ThreadLocal<RepoVo> CTX_HOLDER = new ThreadLocal<>();

    public static void set(RepoVo repo) {
        CTX_HOLDER.set(repo);
    }

    public static RepoVo get() {
        return CTX_HOLDER.get();
    }

    public static void remove() {
        CTX_HOLDER.remove();
    }
}
