# 支持helm package插件

CREATE TABLE `plugin_helm_package_config`
(
    `id`                      bigint(20)   NOT NULL AUTO_INCREMENT,
    `build_image_id`          bigint(20)   NOT NULL DEFAULT '0',
    `use_default_version`     tinyint(1)   NOT NULL DEFAULT '0',
    `custom_version`          varchar(255)          DEFAULT NULL,
    `use_default_app_version` tinyint(1)   NOT NULL DEFAULT '0',
    `app_version_type`        varchar(255)          DEFAULT NULL,
    `pkg_repo_type`           varchar(255)          DEFAULT NULL,
    `pkg_repo_name`           varchar(255)          DEFAULT NULL,
    `chart_resource_path`     varchar(500) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `plugin_helm_package_build`
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('HELM-PACKAGE', 'helm3.7.0', 'helm', '3.7.0');
