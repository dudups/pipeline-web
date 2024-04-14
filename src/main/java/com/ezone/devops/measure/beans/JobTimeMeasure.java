package com.ezone.devops.measure.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobTimeMeasure {

    private String jobType;
    private Date start;
    private Date end;

}
