package com.ezone.devops.plugins.service.impl;

import com.ezone.devops.pipeline.config.SystemConfig;
import com.ezone.devops.plugins.dao.BuildImageDao;
import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.model.BuildImage;
import com.ezone.devops.plugins.service.BuildImageService;
import com.ezone.devops.plugins.service.PluginService;
import com.ezone.devops.plugins.web.request.BuildImagePayload;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.config.I18nContextHolder;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class BuildImageServiceImpl implements BuildImageService {

    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private BuildImageDao buildImageDao;
    @Autowired
    private PluginService pluginService;

    @Override
    public List<BuildImagePayload> listBuildImage(String jobType) {
        List<BuildImage> buildImages = buildImageDao.findByJobType(jobType);
        if (CollectionUtils.isEmpty(buildImages)) {
            return null;
        }

        List<BuildImagePayload> images = Lists.newArrayListWithCapacity(buildImages.size());
        for (BuildImage buildImage : buildImages) {
            BuildImagePayload payload = new BuildImagePayload(buildImage, systemConfig);
            images.add(payload);
        }

        return images;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BuildImage createBuildImage(String job, BuildImagePayload payload) {
        Plugin plugin = pluginService.getPlugin(job);
        if (plugin == null) {
            throw new BaseException(HttpStatus.NOT_FOUND.value(), "插件\"" + job + "\"不存在");
        }

        BuildImage buildImage = buildImageDao.findByName(job, payload.getDisplayName());
        if (buildImage != null) {
            throw new BaseException(HttpStatus.CONTINUE.value(), "构建的镜像已经存在");
        }
        buildImage = newBuildImage(job, payload);
        buildImageDao.save(buildImage);
        return buildImage;
    }

    private BuildImage newBuildImage(String job, BuildImagePayload payload) {
        BuildImage buildImage = new BuildImage();
        buildImage.setJobType(job);
        buildImage.setCreator(payload.getCreator());
        buildImage.setImage(payload.getImage());
        buildImage.setDisplayName(payload.getDisplayName());
        buildImage.setTag(payload.getTag());
        buildImage.setDescription(payload.getDescription());
        Date date = new Date();
        buildImage.setCreateTime(date);
        buildImage.setModifyTime(date);
        return buildImage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BuildImage updateBuildImage(String job, Long id, BuildImagePayload payload) {
        Plugin plugin = pluginService.getPlugin(job);
        if (plugin == null) {
            throw new BaseException(HttpStatus.NOT_FOUND.value(), "插件\"" + job + "\"不存在");
        }

        String displayName = payload.getDisplayName();
        BuildImage buildImage = getByIdIfPresent(id);

        BuildImage byName = buildImageDao.findByName(job, displayName);
        if (byName != null && !byName.getId().equals(buildImage.getId())) {
            throw new BaseException(HttpStatus.CONFLICT.value(), "同名镜像已经存在");
        }

        buildImage.setCreator(payload.getCreator());
        buildImage.setDisplayName(payload.getDisplayName());
        buildImage.setImage(payload.getImage());
        buildImage.setTag(payload.getTag());
        buildImage.setDescription(payload.getDescription());
        buildImage.setModifyTime(new Date());
        buildImageDao.update(buildImage);
        return buildImage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteBuildImage(String job, Long id) {
        return buildImageDao.deleteBuildImage(job, id);
    }

    @Override
    public BuildImage getByIdIfPresent(Long id) {
        BuildImage buildImage = buildImageDao.get(id);
        if (buildImage == null) {
            throw new BaseException(HttpStatus.NOT_FOUND.value(), "该插件设置的镜像已被管理员删除，请重新配置插件");
        }
        return buildImage;
    }
}
