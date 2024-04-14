package com.ezone.devops.pipeline.sender.webhook;

import com.ezone.ezbase.iam.bean.enums.HookPlatform;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class WebHookNoticeProcessFactory {

    private Map<HookPlatform, WebHookNoticeProcess> processMap = Maps.newConcurrentMap();

    @Autowired
    private void init(List<WebHookNoticeProcess> processes) {
        processes.forEach(handler -> processMap.put(handler.getHookPlatform(), handler));
    }

    public WebHookNoticeProcess getProcess(HookPlatform platform) {
        return processMap.get(platform);
    }

}
