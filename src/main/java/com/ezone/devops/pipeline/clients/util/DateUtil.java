package com.ezone.devops.pipeline.clients.util;


import org.apache.commons.lang3.time.DateFormatUtils;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class DateUtil {

    public static final String str = "yyyy-MM-dd";

    public static final String STANDARD_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String start = "00:00:00";

    private static final String end = "23:59:59";

    public static final String BIG_DATA_TIMESTAMP = "yyyyMMddHHmmss";

    /**
     * 将时间戳转为标准时间格式
     * @param timestamp
     * @return
     */
    public static String getDateStr(@NotNull Long timestamp) {
        return DateFormatUtils.format(new Date(timestamp), STANDARD_TIME_FORMAT);
    }

    /**
     * 将时间戳转为标准时间格式
     * @param timestamp
     * @return
     */
    public static String getBigDataTimestamp(@NotNull Long timestamp) {
        return DateFormatUtils.format(new Date(timestamp), BIG_DATA_TIMESTAMP);
    }
}
