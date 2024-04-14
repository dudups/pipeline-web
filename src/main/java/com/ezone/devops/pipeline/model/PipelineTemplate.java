package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.dao.PipelineTemplateDao;
import com.ezone.devops.pipeline.web.request.PipelineTemplatePayload;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Table(name = "pipeline_template")
public class PipelineTemplate extends LongID {

    @Column(PipelineTemplateDao.ID)
    private Long id;
    @Column(PipelineTemplateDao.COMPANY_ID)
    private Long companyId;
    @Column(PipelineTemplateDao.NAME)
    private String name;
    @Column(PipelineTemplateDao.CREATE_USER)
    private String createUser;
    @Column(value = PipelineTemplateDao.CREATE_TIME)
    private Date createTime;
    @Column(PipelineTemplateDao.MODIFY_TIME)
    private Date modifyTime;

    public PipelineTemplate(Long companyId, String createUser, PipelineTemplatePayload payload) {
        setCompanyId(companyId);
        setName(payload.getName());
        setCreateUser(createUser);
        Date date = new Date();
        setCreateTime(date);
        setModifyTime(date);
    }
}
