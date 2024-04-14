package com.ezone.devops.pipeline.clients.request;

import com.ezone.devops.plugins.job.enums.RepoType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class HelmReleasePayload {

    @ApiModelProperty("仓库的名称")
    @NotBlank(message = "仓库名称不允许为空")
    private String repoName;
    @NotNull(message = "仓库类型不允许为空")
    private RepoType repoType;

    @ApiModelProperty("chart的名称，例如：mysql")
    @NotBlank(message = "chart名称不允许为空")
    private String chartName;
    @ApiModelProperty("chart的版本，例如：0.0.0-default")
    @NotBlank(message = "版本不允许为空")
    private String version;

    private String release;
    @ApiModelProperty("chart的values内容，必须是yaml格式的")
    private String values;
}
