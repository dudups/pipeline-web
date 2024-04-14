package com.ezone.devops.pipeline.util;

import com.ezone.devops.pipeline.common.BuildStatus;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

public class BuildStatusUtil {

    private static final Set<BuildStatus> successStatus;
    private static final Set<BuildStatus> failedStatus;
    private static final Set<BuildStatus> runningStatus;
    private static final Set<BuildStatus> waitingStatus;

    static {
        successStatus = new HashSet<>();
        successStatus.add(BuildStatus.SUCCESS);
        successStatus.add(BuildStatus.SKIP);

        failedStatus = new HashSet<>();
        failedStatus.add(BuildStatus.FAIL);
        failedStatus.add(BuildStatus.ABORT);
        failedStatus.add(BuildStatus.CANCEL);

        runningStatus = new HashSet<>();
        runningStatus.add(BuildStatus.SKIP);
        runningStatus.add(BuildStatus.SUCCESS);
        runningStatus.add(BuildStatus.RUNNING);
        runningStatus.add(BuildStatus.WAITING);

        waitingStatus = new HashSet<>();
        waitingStatus.add(BuildStatus.SKIP);
        waitingStatus.add(BuildStatus.PENDING);
        waitingStatus.add(BuildStatus.WAITING);
    }

    private static BuildStatus getStatus(Set<BuildStatus> jobStatuses) {
        if (CollectionUtils.containsAny(failedStatus, jobStatuses)) {
            return BuildStatus.FAIL;
        }

        if (CollectionUtils.containsAll(successStatus, jobStatuses)) {
            return BuildStatus.SUCCESS;
        }

        if (CollectionUtils.containsAll(waitingStatus, jobStatuses)) {
            return BuildStatus.WAITING;
        }

        return BuildStatus.RUNNING;
    }

    public static BuildStatus getStageStatus(Set<BuildStatus> jobStatuses) {
        return getStatus(jobStatuses);
    }

    public static BuildStatus getPipelineStatus(Set<BuildStatus> stageStatuses) {
        return getStatus(stageStatuses);
    }

}
