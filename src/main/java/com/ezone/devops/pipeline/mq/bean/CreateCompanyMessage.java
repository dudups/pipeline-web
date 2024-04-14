package com.ezone.devops.pipeline.mq.bean;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class CreateCompanyMessage {

    private Long id;
    private String nickName;
    private String name;
    private String ip;
    private Long userId;
    private Date gmtCreate;
    private Date gmtModified;

}
