package com.ezone.devops.pipeline.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.ezone.devops.pipeline.dao.VariableDao;
import com.ezone.devops.pipeline.web.request.EnvConfigPayload;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@Table(name = "variable")
public class Variable extends LongID {

    @JSONField(serialize = false)
    @Column(VariableDao.ID)
    private Long id;
    @JSONField(serialize = false)
    @Column(VariableDao.PIPELINE_ID)
    private Long pipelineId;
    @Column(VariableDao.ENV_KEY)
    private String envKey;
    @Column(VariableDao.ENV_VALUE)
    private String envValue;
    @Column(VariableDao.SECRET)
    private boolean secret;
    @Column(VariableDao.DESCRIPTION)
    private String description = StringUtils.EMPTY;

    public Variable(Pipeline pipeline, EnvConfigPayload envConfigPayload) {
        this.pipelineId = pipeline.getId();
        this.envKey = envConfigPayload.getEnvKey();
        this.envValue = envConfigPayload.getEnvValue();
        this.secret = envConfigPayload.isSecret();
        this.description = StringUtils.defaultIfBlank(envConfigPayload.getDescription(), StringUtils.EMPTY);
    }
}


