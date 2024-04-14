package com.ezone.devops.pipeline.common;

import com.ezone.devops.ezcode.sdk.bean.enums.CardRelateType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 构建的触发方式
 */
public enum TriggerMode {

    API,

    /**
     * 代码库触发
     */
    UPSTREAM,
    /**
     * 代码库分支添加和推送
     */
    PUSH,
    /**
     * 代码库发起及更新MR
     */
    MR,
    /**
     * 代码库发起及更新DCR
     */
    DCR,
    /**
     * 代码库评审触发模拟预合入
     */
    VM,

    /**
     * 手工触发
     */
    CRONTAB,
    MANUAL;

    private static final TriggerMode[] CODE_REVIEW_MODES = {MR, DCR, VM};

    public static TriggerMode convert(String modeName) {
        if (StringUtils.isEmpty(modeName)) {
            return null;
        }
        Predicate<TriggerMode> match = mode -> mode.name().equals(modeName);
        return Stream.of(TriggerMode.values()).filter(match).findAny().orElse(null);
    }

    public static CardRelateType toCardRelateType(TriggerMode triggerMode) {
        if (PUSH == triggerMode) {
            return CardRelateType.PUSH;
        }
        if (isCodeReviewMode(triggerMode)) {
            return CardRelateType.REVIEW;
        }
        if (CRONTAB == triggerMode || MANUAL == triggerMode) {
            return CardRelateType.COMMIT;
        }
        return null;
    }

    public static boolean isCodeReviewMode(TriggerMode triggerMode) {
        return triggerMode != null && ArrayUtils.contains(CODE_REVIEW_MODES, triggerMode);
    }

}
