package com.ezone.devops.plugins.web.request;

import com.ezone.devops.pipeline.config.SystemConfig;
import com.ezone.devops.plugins.model.BuildImage;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class BuildImagePayload {

    private Long id;
    @NotBlank(message = "创建人不允许为空")
    private String creator;
    @NotBlank(message = "显示名称不允许为空")
    private String displayName;
    @NotBlank(message = "镜像不允许为空")
    private String image;
    @NotBlank(message = "版本允许为空")
    private String tag;
    private String description = StringUtils.EMPTY;
    private String imageTag;

    public BuildImagePayload(BuildImage buildImage, SystemConfig systemConfig) {
        setId(buildImage.getId());
        setCreator(buildImage.getCreator());
        setDisplayName(buildImage.getDisplayName());
        setDescription(buildImage.getDescription());

        String image = buildImage.getImage();
        String tag = buildImage.getTag();
        setImage(image);
        setTag(tag);

        String prefix = systemConfig.getBuildImagePrefix();
        String fullTag = image + ":" + tag;
        if (StringUtils.isNotBlank(prefix)) {
            fullTag = prefix + "/" + fullTag;
        }

        setImageTag(fullTag);
    }
}