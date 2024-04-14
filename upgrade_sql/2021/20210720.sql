# 增加模板功能
CREATE TABLE `pipeline_template`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `company_id`  bigint(20)   NOT NULL DEFAULT '0',
    `name`        varchar(255) NOT NULL DEFAULT '',
    `create_user` varchar(255) NOT NULL DEFAULT '',
    `create_time` datetime     NOT NULL DEFAULT now(),
    `modify_time` datetime     NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_company_name` (`company_id`, `name`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `stage_template`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `template_id` bigint(20)   NOT NULL DEFAULT '0',
    `upstream_id` bigint(20)   NOT NULL DEFAULT '0',
    `stage_name`  varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_template_id` (`template_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `job_template`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `template_id` bigint(20)   NOT NULL DEFAULT '0',
    `stage_id`    bigint(20)   NOT NULL DEFAULT '0',
    `job_name`    varchar(255) NOT NULL DEFAULT '',
    `auto_build`  tinyint(255) NOT NULL DEFAULT '1',
    `job_type`    varchar(255) NOT NULL DEFAULT '',
    `plugin_type` varchar(255) NOT NULL DEFAULT '',
    `upstream_id` bigint(20)   NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_template_id` (`template_id`) USING BTREE,
    KEY `idx_stage_id` (`stage_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;