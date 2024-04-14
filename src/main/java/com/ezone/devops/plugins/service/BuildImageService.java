package com.ezone.devops.plugins.service;

import com.ezone.devops.plugins.model.BuildImage;
import com.ezone.devops.plugins.web.request.BuildImagePayload;

import java.util.List;

public interface BuildImageService {

    List<BuildImagePayload> listBuildImage(String jobType);

    BuildImage createBuildImage(String job, BuildImagePayload payload);

    BuildImage updateBuildImage(String job, Long id, BuildImagePayload payload);

    boolean deleteBuildImage(String job, Long id);

    BuildImage getByIdIfPresent(Long id);

}