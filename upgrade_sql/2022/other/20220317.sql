# 支持单根流水线权限的功能
CREATE TABLE `pipeline_permission`
(
    `id`              bigint(20)   NOT NULL AUTO_INCREMENT,
    `company_id`      bigint(20)   NOT NULL DEFAULT 0,
    `repo_key`        varchar(255) NOT NULL DEFAULT '',
    `pipeline_id`     bigint(20)   NOT NULL DEFAULT 0,
    `permission_type` varchar(255) NOT NULL DEFAULT '',
    `name`            varchar(255) NOT NULL DEFAULT '',
    `type`            varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_company_id` (`company_id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE,
    KEY `idx_repo_key` (`repo_key`) USING BTREE,
    KEY `idx_permission_type` (`permission_type`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

ALTER TABLE `pipeline`
    ADD COLUMN `inherit_repo_permission` tinyint(1) NOT NULL DEFAULT 1;