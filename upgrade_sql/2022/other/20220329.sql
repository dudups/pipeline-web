# 支持ant
CREATE TABLE `plugin_ant_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_ant_docker_config`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `data_json` text DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO `build_image` (`job_type`, `display_name`, `image`, `tag`, `creator`, `description`)
VALUES ('ANT-COMPILE', 'ant-1.9.16-jdk7', 'ezone-public/ant', '1.9.16-jdk7', 'system', '');
INSERT INTO `build_image` (`job_type`, `display_name`, `image`, `tag`, `creator`, `description`)
VALUES ('ANT-COMPILE', 'ant-1.10.12-jdk8', 'ezone-public/ant', '1.10.12-jdk8', 'system', '');
INSERT INTO `build_image` (`job_type`, `display_name`, `image`, `tag`, `creator`, `description`)
VALUES ('ANT-COMPILE-DOCKER', 'ant-1.9.16-jdk7', 'ezone-public/ant', '1.9.16-jdk7', 'system', '');
INSERT INTO `build_image` (`job_type`, `display_name`, `image`, `tag`, `creator`, `description`)
VALUES ('ANT-COMPILE-DOCKER', 'ant-1.10.12-jdk8', 'ezone-public/ant', '1.10.12-jdk8', 'system', '');