package com.ezone.devops.plugins.job.scan.ezscan.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.ezone.devops.plugins.enums.ScanLevel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

@Data
public class EzScanConfigBean {

    // 规则集id
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long rulesetId;
    // 过滤路径：目录或文件的根目录相对路径
    private Set<String> filterDirs;
    // 代码问题扫描红线：BLOCK-仅扫描阻塞问题 HIGH-仅扫描严重以上问题 MIDDLE-仅扫描中风险以上问题 LOW-扫描所有问题
    private ScanLevel scanLevel;

    private boolean incrementScan;
    // 是否启动门禁质量
    private boolean enableQos;
    // 门禁质量级别
    private ScanLevel qosLevel;
    // 门禁质量数量
    private Integer qosCount;
    // 是否是 .Net项目
    private boolean dotNetFramework;
    // 是否是xcode项目
    private Boolean isXcodeApp;
    // 是否使用自定义资源
    private boolean useSelfCiPool = false;
    // 集群名称
    private String clusterName = StringUtils.EMPTY;
}