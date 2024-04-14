package com.ezone.devops.plugins.dao;

import com.ezone.devops.plugins.model.BuildImage;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.List;

public interface BuildImageDao extends LongKeyBaseDao<BuildImage> {

    String ID = "id";
    String CREATOR = "creator";
    String JOB_TYPE = "job_type";
    String DISPLAY_NAME = "display_name";
    String IMAGE = "image";
    String TAG = "tag";
    String DESCRIPTION = "description";
    String CREATE_TIME = "create_time";
    String MODIFY_TIME = "modify_time";

    List<BuildImage> findByJobType(String jobType);

    BuildImage findByName(String jobType, String displayName);

    boolean deleteBuildImage(String job, Long id);
}
