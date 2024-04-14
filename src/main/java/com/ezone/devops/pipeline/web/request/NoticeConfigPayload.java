package com.ezone.devops.pipeline.web.request;

import com.ezone.devops.pipeline.enums.MemberType;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NoticeConfigPayload {

    @NotNull(message = "成员类型不允许为空")
    private MemberType memberType;
    @NotBlank(message = "名称不能为空")
    @Length(min = 1, max = 100, message = "名称长度在1到100个字符之间")
    private String name;
    private boolean start;
    private boolean success;
    private boolean failed;
}
