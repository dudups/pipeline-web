package com.ezone.devops.scheduler.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ImageInfo {

    public static final String BUILD_NAME = "build";

    private String name;
    private String alias;
    private List<String> command;
    private List<String> entrypoint;
    private List<Port> ports;

    @Data
    static class Port {
        private Integer number;
        private String protocol;
        private String name;
    }

}
