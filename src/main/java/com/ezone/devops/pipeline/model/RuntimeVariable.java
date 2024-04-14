package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.dao.RuntimeVariableDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "runtime_variable", shardCount = 10)
public class RuntimeVariable extends LongID {

    @JsonIgnore
    @Column(RuntimeVariableDao.ID)
    private Long id;
    @Column(RuntimeVariableDao.REPO_KEY)
    private String repoKey;
    @JsonIgnore
    @Column(RuntimeVariableDao.BUILD_ID)
    private Long buildId;
    @Column(RuntimeVariableDao.ENV_KEY)
    private String envKey;
    @Column(RuntimeVariableDao.ENV_VALUE)
    private String envValue;

}


