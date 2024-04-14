package com.ezone.devops.pipeline.clients.response;

import com.ezone.galaxy.framework.common.bean.BaseResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class EzScanResponse extends BaseResponse<EzScanResult> {

}
