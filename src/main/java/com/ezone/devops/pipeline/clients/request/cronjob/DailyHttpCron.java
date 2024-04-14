package com.ezone.devops.pipeline.clients.request.cronjob;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DailyHttpCron extends HttpCron<DailyTrigger> {

}
