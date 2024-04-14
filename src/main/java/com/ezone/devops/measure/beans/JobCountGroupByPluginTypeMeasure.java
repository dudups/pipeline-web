package com.ezone.devops.measure.beans;

import com.ezone.devops.plugins.enums.PluginType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobCountGroupByPluginTypeMeasure {

    private PluginType pluginType;
    private int success;
    private long successAverageSecond;
    private int failed;
    private long failedAverageSecond;
}
