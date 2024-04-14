package com.ezone.devops.pipeline.web.request;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.enums.OperationType;
import lombok.Data;

@Data
public class OperationPayload {

    private OperationType operationType = OperationType.UPDATE_PARAM;
    private JSONObject params;

}
