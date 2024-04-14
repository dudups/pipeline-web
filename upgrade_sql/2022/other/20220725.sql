# 支持按照tag和commit触发流水线
ALTER TABLE `pipeline_record`
    CHANGE COLUMN `branch_name` `external_name` varchar(255) NOT NULL DEFAULT '',
    ADD COLUMN `scm_trigger_type` varchar(255) NOT NULL DEFAULT 'BRANCH',
    ADD INDEX `idx_scm_trigger_type` (`scm_trigger_type`) USING BTREE;