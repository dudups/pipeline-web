# 增加jenkins插件
CREATE TABLE `plugin_jenkins_job_config`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT,
    `jenkins_name`     varchar(255) DEFAULT NULL,
    `jenkins_job_name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_jenkins_job_build`
(
    `id`                   bigint(20) NOT NULL AUTO_INCREMENT,
    `dashboard_url`        varchar(500) DEFAULT NULL,
    `jenkins_build_status` varchar(255) DEFAULT NULL,
    `jenkins_build_number` bigint(20)   DEFAULT NULL,
    `start_time`           bigint(20)   DEFAULT NULL,
    `end_time`             bigint(20)   DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;