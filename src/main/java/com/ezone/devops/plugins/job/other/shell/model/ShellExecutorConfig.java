package com.ezone.devops.plugins.job.other.shell.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.enums.CloneMode;
import com.ezone.devops.plugins.job.other.shell.dao.ShellExecutorConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;

@Data
@Table(name = "plugin_shell_executor_config")
public class ShellExecutorConfig extends LongID {

    @Column(ShellExecutorConfigDao.ID)
    private Long id;
    @Column(ShellExecutorConfigDao.CLONE_MODE)
    private CloneMode cloneMode = CloneMode.SINGLE_COMMIT;
    @JSONField(serializeUsing = ToStringSerializer.class)
    @Column(ShellExecutorConfigDao.BUILD_IMAGE_ID)
    private Long buildImageId;
    @Column(ShellExecutorConfigDao.COMMAND)
    private String command;
}