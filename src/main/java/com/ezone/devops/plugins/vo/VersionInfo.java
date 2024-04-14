package com.ezone.devops.plugins.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionInfo {

    @ApiModelProperty(name = "上次发版的版本")
    private String lastVersion;
    @ApiModelProperty(name = "用户设置的版本的版本")
    private String version;
    @ApiModelProperty(name = "发版说明")
    private String message;
}
