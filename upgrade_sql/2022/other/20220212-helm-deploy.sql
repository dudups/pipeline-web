# 支持helm deploy插件
CREATE TABLE `plugin_helm_deploy_build`
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_helm_deploy_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;