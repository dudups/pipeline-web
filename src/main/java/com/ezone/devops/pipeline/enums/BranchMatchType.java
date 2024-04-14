package com.ezone.devops.pipeline.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * pipelineBranch的匹配规则
 */
public enum BranchMatchType {

    // 精准匹配
    PRECISE() {
        @Override
        public boolean match(String match, String branch) {
            return StringUtils.defaultString(branch).equals(StringUtils.defaultString(match));
        }
    },
    // 前缀匹配
    PREFIX() {
        @Override
        public boolean match(String match, String branch) {
            return StringUtils.defaultString(branch).startsWith(StringUtils.defaultString(match));
        }
    },
    // 后缀匹配
    SUFFIX() {
        @Override
        public boolean match(String match, String branch) {
            return StringUtils.defaultString(branch).endsWith(StringUtils.defaultString(match));
        }
    },
    // 匹配所有
    ALL() {
        @Override
        public boolean match(String match, String branch) {
            return true;
        }
    };

    public boolean match(String match, String branch) {
        return StringUtils.equals(match, branch);
    }
}

