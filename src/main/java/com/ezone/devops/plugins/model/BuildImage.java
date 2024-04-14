package com.ezone.devops.plugins.model;

import com.ezone.devops.plugins.dao.BuildImageDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;

import java.util.Date;

@Data
@Table(name = "build_image")
public class BuildImage extends LongID {

    @Column(BuildImageDao.ID)
    private Long id;
    @Column(BuildImageDao.CREATOR)
    private String creator;
    @Column(BuildImageDao.JOB_TYPE)
    private String jobType;
    @Column(BuildImageDao.DISPLAY_NAME)
    private String displayName;
    @Column(BuildImageDao.IMAGE)
    private String image;
    @Column(BuildImageDao.TAG)
    private String tag;
    @Column(BuildImageDao.DESCRIPTION)
    private String description;
    @Column(BuildImageDao.CREATE_TIME)
    private Date createTime;
    @Column(BuildImageDao.MODIFY_TIME)
    private Date modifyTime;
}