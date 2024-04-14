# 主机部署插件增加报告相关字段
ALTER TABLE `plugin_host_compile_config`
    ADD COLUMN `upload_report` tinyint(1)   NOT NULL DEFAULT 0,
    ADD COLUMN `report_dir`    varchar(255) NOT NULL DEFAULT '',
    ADD COLUMN `report_index`  varchar(255) NOT NULL DEFAULT '';

ALTER TABLE `plugin_host_compile_build`
    ADD COLUMN `report_dashboard_url` varchar(500) NOT NULL DEFAULT '',
    ADD COLUMN `report_token`         varchar(255) NOT NULL DEFAULT '',
    ADD COLUMN `report_id`            varchar(255) NOT NULL DEFAULT '';

CREATE TABLE `report_setting`
(
    `id`         bigint(20)  NOT NULL AUTO_INCREMENT,
    `company_id` bigint(20)  NOT NULL DEFAULT '0',
    `expire_day` tinyint(10) NOT NULL DEFAULT '30',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_company_id` (`company_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;