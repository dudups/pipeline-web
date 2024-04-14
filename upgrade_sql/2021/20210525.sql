# docker执行器支持上传报告，上传文件制品，制作docker镜像
ALTER TABLE `plugin_docker_executor_build`
    ADD COLUMN `report_dashboard_url` varchar(500) NOT NULL DEFAULT '' AFTER `id`,
    ADD COLUMN `report_id`            varchar(255) NOT NULL DEFAULT '' AFTER `report_dashboard_url`,
    ADD COLUMN `version`              varchar(255) NOT NULL DEFAULT '' AFTER `report_id`;

ALTER TABLE `plugin_docker_executor_config`
    ADD COLUMN `upload_report`       tinyint(1)   NOT NULL DEFAULT 0 AFTER `command`,
    ADD COLUMN `report_dir`          varchar(255) NULL AFTER `upload_report`,
    ADD COLUMN `report_index`        varchar(255) NULL AFTER `report_dir`,
    ADD COLUMN `upload_artifact`     tinyint(1)   NOT NULL DEFAULT 0 AFTER `report_index`,
    ADD COLUMN `pkg_repo`            varchar(255) NULL AFTER `upload_artifact`,
    ADD COLUMN `pkg_name`            varchar(255) NULL AFTER `pkg_repo`,
    ADD COLUMN `artifact_path`       varchar(500) NULL AFTER `pkg_name`,
    ADD COLUMN `push_image`          tinyint(1)   NOT NULL DEFAULT 0 AFTER `artifact_path`,
    ADD COLUMN `push_registry_type`  varchar(255) NULL AFTER `push_image`,
    ADD COLUMN `dockerfile`          varchar(255) NULL AFTER `push_registry_type`,
    ADD COLUMN `docker_context`      varchar(255) NULL AFTER `dockerfile`,
    ADD COLUMN `docker_repo`         varchar(255) NULL AFTER `docker_context`,
    ADD COLUMN `docker_image_name`   varchar(255) NULL AFTER `docker_repo`,
    ADD COLUMN `docker_version_type` varchar(255) NULL AFTER `docker_image_name`,
    ADD COLUMN `docker_tag`          varchar(255) NULL AFTER `docker_version_type`;

CREATE TABLE `plugin_docker_executor_config`
(
    `id`                  bigint(20)   NOT NULL AUTO_INCREMENT,
    `cluster_id`          bigint(20)   NOT NULL DEFAULT '0',
    `cluster_name`        varchar(255) NOT NULL DEFAULT '',
    `registry_type`       varchar(255) NOT NULL DEFAULT '',
    `provider_name`       varchar(255)          DEFAULT NULL,
    `registry_id`         bigint(20)            DEFAULT NULL,
    `image_name`          varchar(255) NOT NULL DEFAULT '',
    `version_type`        varchar(255)          DEFAULT NULL,
    `version`             varchar(255)          DEFAULT NULL,
    `need_clone`          tinyint(1)   NOT NULL DEFAULT '0',
    `command`             varchar(10000)        DEFAULT NULL,
    `upload_report`       tinyint(1)   NOT NULL DEFAULT '0',
    `report_dir`          varchar(255)          DEFAULT NULL,
    `report_index`        varchar(255)          DEFAULT NULL,
    `upload_artifact`     tinyint(1)   NOT NULL DEFAULT '0',
    `pkg_repo`            varchar(255)          DEFAULT NULL,
    `pkg_name`            varchar(255)          DEFAULT NULL,
    `artifact_path`       varchar(500)          DEFAULT NULL,
    `push_image`          tinyint(1)   NOT NULL DEFAULT '0',
    `push_registry_type`  varchar(255)          DEFAULT NULL,
    `dockerfile`          varchar(255)          DEFAULT NULL,
    `docker_context`      varchar(255)          DEFAULT NULL,
    `docker_repo`         varchar(255)          DEFAULT NULL,
    `docker_image_name`   varchar(255)          DEFAULT NULL,
    `docker_version_type` varchar(255)          DEFAULT NULL,
    `docker_tag`          varchar(255)          DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8