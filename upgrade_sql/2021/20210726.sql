# 增加sonarqube的门禁
ALTER TABLE `plugin_sonarqube_config`
    ADD COLUMN `enable_quality_control` tinyint(1)   NOT NULL DEFAULT 0,
    ADD COLUMN `metric_level`           varchar(255) NULL,
    ADD COLUMN `greater_than`           int(10)      NULL;

ALTER TABLE `pipeline_config`
    ADD COLUMN `company_id` bigint(20) NOT NULL DEFAULT 0 AFTER `id`,
    ADD INDEX `idx_company` (`company_id`) USING BTREE;