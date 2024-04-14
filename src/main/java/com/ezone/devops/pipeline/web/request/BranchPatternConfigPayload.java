package com.ezone.devops.pipeline.web.request;

import com.ezone.devops.pipeline.enums.BranchMatchType;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class BranchPatternConfigPayload {

    @NotNull(message = "匹配类型不允许为空")
    private BranchMatchType matchType;
    @Length(min = 1, max = 100, message = "分支匹配规则最多介于1-100个字符之间")
    private String pattern = "*";

}
