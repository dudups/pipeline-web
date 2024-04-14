package com.ezone.devops.measure.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Measure {

    private JobCountGroupByRepoMeasure jobCountMeasure;
    private Collection<JobCountGroupByJobMeasure> pluginCountMeasures;
    private List<JobTimeMeasure> jobTimeMeasures;
}
