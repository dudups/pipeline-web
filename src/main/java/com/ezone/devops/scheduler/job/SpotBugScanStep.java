package com.ezone.devops.scheduler.job;

import com.ezone.devops.plugins.enums.ScanLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SpotBugScanStep {

    private boolean enableScan;
    private String outputPath;
    private Long rulesetId;
    private ScanLevel scanLevel;
    private Set<String> filterDirs;
    private String scanPkgNames;

}
