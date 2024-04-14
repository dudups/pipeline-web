package com.ezone.devops.pipeline.mq.producer;

import com.ezone.devops.pipeline.mq.bean.WebsocketEvent;
import com.ezone.devops.pipeline.mq.bean.WebsocketEventMessage;
import com.ezone.galaxy.framework.mq.annotation.producer.CommonMessage;
import com.ezone.galaxy.framework.mq.annotation.producer.RocketMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RocketMessage(groupName = "ezpipeline")
public class WebsocketProducer {

    private static final String APP_NAME = "ezpipeline";

    @CommonMessage(topic = "WEBSOCKET_EVENT", tag = "UPDATE")
    public WebsocketEventMessage sendEvent(WebsocketEvent event, Object key, Object data) {
        if (event == null || key == null || data == null) {
            return null;
        }
        WebsocketEventMessage websocketEventMessage = new WebsocketEventMessage();
        websocketEventMessage.setAppName(APP_NAME);
        websocketEventMessage.setEventName(event.getType());
        websocketEventMessage.setKey(key);
        websocketEventMessage.setData(data);
        return websocketEventMessage;
    }

}
