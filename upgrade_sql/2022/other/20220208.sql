CREATE TABLE `plugin_maven_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_maven_docker_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_dotnet_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_dotnet_docker_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_custom_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_custom_docker_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_cmake_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_cmake_docker_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_go_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_go_docker_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_gradle_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_gradle_docker_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_npm_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_npm_docker_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_python_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_python_docker_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_php_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_php_docker_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text       NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_helm_deploy_v2_build`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_helm_deploy_v2_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

ALTER TABLE `plugin_docker_executor_config`
    ADD COLUMN `platform` varchar(255) NULL,
    ADD COLUMN `arch`     varchar(255) NULL;

ALTER TABLE `plugin_helm_deploy_build`
    ADD COLUMN `data_json` text NULL;