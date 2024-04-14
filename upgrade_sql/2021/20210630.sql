# 增加eztest插件
CREATE TABLE `plugin_eztest_config`
(
    `id`         bigint(20) NOT NULL AUTO_INCREMENT,
    `env_id`     bigint(20) DEFAULT NULL,
    `suite_id`   bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_eztest_build`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `plan_id`   bigint(20)   DEFAULT NULL,
    `space_key` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;