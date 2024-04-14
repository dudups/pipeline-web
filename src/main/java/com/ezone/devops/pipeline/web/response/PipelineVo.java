package com.ezone.devops.pipeline.web.response;

import com.ezone.devops.pipeline.model.Pipeline;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PipelineVo {

    private Long id;
    private String name;
    private String createUser;
    private Date createTime;
    private Date modifyTime;

    public PipelineVo(Pipeline pipeline) {
        setId(pipeline.getId());
        setName(pipeline.getName());
        setCreateUser(pipeline.getCreateUser());
        setCreateTime(pipeline.getCreateTime());
        setModifyTime(pipeline.getModifyTime());
    }
}
