package com.ezone.devops.scheduler.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContainerVolume {

    private String name;
    private String source;
    private String destination;

}
