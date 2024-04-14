package com.ezone.devops.pipeline.common;

import com.ezone.devops.pipeline.config.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ExecutorTaskPool {

    @Autowired
    private SystemConfig systemConfig;
    private ExecutorService executor;

    @PostConstruct
    private void init() {
        executor = Executors.newFixedThreadPool(systemConfig.getThreadCount());
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }
}
