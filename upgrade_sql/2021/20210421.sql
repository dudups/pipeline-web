#  添加webhook通知
CREATE TABLE `web_hook_notice`
(
    `id`         bigint(20) NOT NULL AUTO_INCREMENT,
    `company_id` bigint(20) NOT NULL DEFAULT '0',
    `repo_id`    bigint(20) NOT NULL DEFAULT '0',
    `hook_id`    bigint(20) NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_company_repo_hook` (`company_id`, `repo_id`, `hook_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `web_hook_notice_event`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `company_id`  bigint(20) NOT NULL DEFAULT '0',
    `repo_id`     bigint(20) NOT NULL DEFAULT '0',
    `hook_id`     bigint(20) NOT NULL DEFAULT '0',
    `pipeline_id` bigint(20) NOT NULL DEFAULT '0',
    `start`       tinyint(1) NOT NULL DEFAULT '0',
    `success`     tinyint(1) NOT NULL DEFAULT '0',
    `failed`      tinyint(1) NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_company_repo_hook_pipeline` (`company_id`, `repo_id`, `hook_id`, `pipeline_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;