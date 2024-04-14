package com.ezone.devops.measure.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobCountGroupByRepoMeasure {

    private int success;
    private int failed;
}
