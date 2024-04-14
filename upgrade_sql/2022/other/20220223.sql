# 支持ezscan插件
CREATE TABLE `plugin_ezscan_build`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_ezscan_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;