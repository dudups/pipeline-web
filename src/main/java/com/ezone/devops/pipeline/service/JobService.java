package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.Stage;
import com.ezone.devops.pipeline.web.request.GroupJob;

import java.util.List;

public interface JobService {

    boolean saveJobs(Pipeline pipeline, Stage stage, List<GroupJob> groupJobs);

    boolean deleteJobs(Pipeline pipeline, List<Stage> stages);

    List<List<Job>> getOrderedJobsByStage(Pipeline pipeline, Stage stage);

    List<GroupJob> getOrderedJobPayloads(Pipeline pipeline, Stage stage);

}
