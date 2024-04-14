package com.ezone.devops.plugins.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ArtifactType {
    @JsonProperty("raw")
    RAW("raw"),
    @JsonProperty("docker")
    DOCKER("docker"),
    @JsonProperty("ipa")
    IPA("ipa"),
    @JsonProperty("apk")
    APK("apk");

    @Getter
    private String type;
}
