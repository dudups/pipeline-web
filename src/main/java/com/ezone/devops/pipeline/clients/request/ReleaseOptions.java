package com.ezone.devops.pipeline.clients.request;

import lombok.Data;

@Data
public class ReleaseOptions extends ChartPathOptions {
    private boolean dryRun;
    private boolean disableHooks;
    private boolean disableOpenAPIValidation;
    private boolean wait;
    private boolean devel;
    private String description;
    private boolean atomic;
    private boolean skipCRDs;
    private boolean subNotes;
    private int timeout = 120;
    private String values;
    private String[] setValues;
    private String[] setStringValues;
    private boolean createNamespace;
    private boolean dependencyUpdate;
    private boolean install;
    private Integer maxHistory;
    private boolean force;
    private boolean recreate;
    private boolean cleanupOnFail;
}
