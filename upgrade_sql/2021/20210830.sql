# 增加镜像管理
CREATE TABLE `build_image`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT,
    `creator`      varchar(255) NOT NULL DEFAULT 'system',
    `job_type`     varchar(255) NOT NULL DEFAULT '',
    `display_name` varchar(255) NOT NULL DEFAULT '',
    `image`        varchar(255) NOT NULL DEFAULT '',
    `tag`          varchar(255) NOT NULL DEFAULT '',
    `description`  varchar(500) NOT NULL DEFAULT '',
    `create_time`  datetime(0)  NULL DEFAULT now(),
    `modify_time`  datetime(0)  NULL DEFAULT now() ON UPDATE CURRENT_TIMESTAMP(0),
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_job_type` (`job_type`) USING BTREE,
    KEY `idx_display_name` (`display_name`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `build_image` SELECT * FROM `plugin_build_image`;

ALTER TABLE `build_image`
    DROP COLUMN `plugin_type`,
    ADD COLUMN `creator`     varchar(255) NOT NULL DEFAULT 'system',
    ADD COLUMN `description` varchar(500) NOT NULL DEFAULT '',
    ADD COLUMN `create_time` datetime(0) NULL DEFAULT now(),
    ADD COLUMN `modify_time` datetime(0) NULL DEFAULT now() ON UPDATE CURRENT_TIMESTAMP(0),
    MODIFY COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT FIRST,
    ADD PRIMARY KEY (`id`),
    ADD INDEX `idx_job_type` (`job_type`) USING BTREE;

update build_image set image=replace(image,'hub.kce.ksyun.com/ezone-public/','');

INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (1, 'CMAKE-COMPILE', 'gcc4', 'gcc', '4');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (2, 'CMAKE-COMPILE', 'gcc5.4.0', 'gcc', '5.4.0');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (3, 'CMAKE-COMPILE', 'gcc7.5.0', 'gcc', '7.5.0');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (4, 'CMAKE-COMPILE', 'gcc8.4.0', 'gcc', '8.4.0');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (5, 'CMAKE-COMPILE', 'gcc9.3.0', 'gcc', '9.3.0');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (6, 'MAVEN-COMPILE', 'maven3.2.5-jdk-6', 'maven', '3.2.5-jdk-6');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (7, 'MAVEN-COMPILE', 'maven3.6.1-jdk-7', 'maven', '3.6.1-jdk-7-alpine');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (8, 'MAVEN-COMPILE', 'maven3.6.1-jdk-8', 'maven', '3.6.1-jdk-8-alpine');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (9, 'MAVEN-COMPILE', 'maven3.5.4-jdk-9', 'maven', '3.5.4-jdk-9');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (10, 'MAVEN-COMPILE', 'maven3.6.0-jdk-10', 'maven', '3.6.0-jdk-10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (11, 'MAVEN-COMPILE', 'maven3.6.3-jdk-11', 'maven', '3.6.3-jdk-11');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (12, 'COBERTURA-TEST', 'maven3.2.5-jdk-6', 'maven', '3.2.5-jdk-6');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (13, 'COBERTURA-TEST', 'maven3.6.1-jdk-7', 'maven', '3.6.1-jdk-7-alpine');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (14, 'COBERTURA-TEST', 'maven3.6.1-jdk-8', 'maven', '3.6.1-jdk-8-alpine');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (15, 'COBERTURA-TEST', 'maven3.5.4-jdk-9', 'maven', '3.5.4-jdk-9');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (16, 'COBERTURA-TEST', 'maven3.6.0-jdk-10', 'maven', '3.6.0-jdk-10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (17, 'COBERTURA-TEST', 'maven3.6.3-jdk-11', 'maven', '3.6.3-jdk-11');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (18, 'JACOCO-TEST', 'maven3.2.5-jdk-6', 'maven', '3.2.5-jdk-6');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (19, 'JACOCO-TEST', 'maven3.6.1-jdk-7', 'maven', '3.6.1-jdk-7-alpine');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (20, 'JACOCO-TEST', 'maven3.6.1-jdk-8', 'maven', '3.6.1-jdk-8-alpine');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (21, 'JACOCO-TEST', 'maven3.5.4-jdk-9', 'maven', '3.5.4-jdk-9');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (22, 'JACOCO-TEST', 'maven3.6.0-jdk-10', 'maven', '3.6.0-jdk-10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (23, 'JACOCO-TEST', 'maven3.6.3-jdk-11', 'maven', '3.6.3-jdk-11');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (24, 'JUNIT-TEST', 'maven3.2.5-jdk-6', 'maven', '3.2.5-jdk-6');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (25, 'JUNIT-TEST', 'maven3.6.1-jdk-7', 'maven', '3.6.1-jdk-7-alpine');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (26, 'JUNIT-TEST', 'maven3.6.1-jdk-8', 'maven', '3.6.1-jdk-8-alpine');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (27, 'JUNIT-TEST', 'maven3.5.4-jdk-9', 'maven', '3.5.4-jdk-9');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (28, 'JUNIT-TEST', 'maven3.6.0-jdk-10', 'maven', '3.6.0-jdk-10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (29, 'JUNIT-TEST', 'maven3.6.3-jdk-11', 'maven', '3.6.3-jdk-11');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (30, 'MAVEN-TEST', 'maven3.2.5-jdk-6', 'maven', '3.2.5-jdk-6');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (31, 'MAVEN-TEST', 'maven3.6.1-jdk-7', 'maven', '3.6.1-jdk-7-alpine');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (32, 'MAVEN-TEST', 'maven3.6.1-jdk-8', 'maven', '3.6.1-jdk-8-alpine');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (33, 'MAVEN-TEST', 'maven3.5.4-jdk-9', 'maven', '3.5.4-jdk-9');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (34, 'MAVEN-TEST', 'maven3.6.0-jdk-10', 'maven', '3.6.0-jdk-10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (35, 'MAVEN-TEST', 'maven3.6.3-jdk-11', 'maven', '3.6.3-jdk-11');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (36, 'GRADLE-COMPILE', 'gradle4.10-jdk7', 'ant', '4.10-jdk7');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (37, 'GRADLE-COMPILE', 'gradle4.10-jdk8', 'ant', '4.10-jdk8');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (38, 'GRADLE-COMPILE', 'gradle4.8-jdk9', 'ant', '4.8-jdk9');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (39, 'GRADLE-COMPILE', 'gradle4.10-jdk10', 'ant', '4.10-jdk10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (40, 'GRADLE-COMPILE', 'gradle6.0-jdk13', 'ant', '6.0-jdk13');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (41, 'PHP-COMPILE', 'composer-1.5.6', 'composer', '1.5.6');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (42, 'PHP-COMPILE', 'composer-1.6.5', 'composer', '1.6.5');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (43, 'PHP-COMPILE', 'composer-1.7.0', 'composer', '1.7.0');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (44, 'PHP-COMPILE', 'composer-1.7.3', 'composer', '1.7.3');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (45, 'PHP-COMPILE', 'composer-1.8.6', 'composer', '1.8.6');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (46, 'PHP-COMPILE', 'composer-1.9.3', 'composer', '1.9.3');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (47, 'PHP-COMPILE', 'composer-1.10', 'composer', '1.10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (48, 'GO-COMPILE', 'go-1', 'golang', '1-alpine3.10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (49, 'GO-COMPILE', 'go-1.11.13', 'golang', '1.11.13-alpine3.10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (50, 'GO-COMPILE', 'go-1.12.17', 'golang', '1.12.17-alpine3.10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (51, 'GO-COMPILE', 'go-1.13.9', 'golang', '1.13.9-alpine3.10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (52, 'NPM-COMPILE', 'node-8.16', 'node', '8.16-alpine3.10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (53, 'NPM-COMPILE', 'node-10.17', 'node', '10.17-alpine3.10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (54, 'NPM-COMPILE', 'node-12.13', 'node', '12.13-alpine3.10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (55, 'NPM-COMPILE', 'node-13.1', 'node', '13.1-alpine3.10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (56, 'PYTHON-COMPILE', 'python2.7', 'python', '2.7-alpine3.10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (57, 'PYTHON-COMPILE', 'python3.5', 'python', '3.5-alpine3.10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (58, 'PYTHON-COMPILE', 'python3.6', 'python', '3.6-alpine3.10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (59, 'PYTHON-COMPILE', 'python3.7', 'python', '3.7-alpine3.10');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (60, 'SONARQUBE', 'sonar-scanner-cli4.6', 'sonarsource/sonar-scanner-cli', '4.6');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (61, 'SHELL-EXECUTOR', 'alpine linux', 'alpine', '3.12.0');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (62, 'CUSTOM-COMPILE', 'alpine linux', 'alpine', '3.12.0');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (63, 'DOTNET-COMPILE', 'dotnet3.1', 'mcr.microsoft.com/dotnet/sdk', '3.1');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (64, 'DOTNET-COMPILE', 'dotnet5.0', 'mcr.microsoft.com/dotnet/sdk', '5.0');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (65, 'DOTNET-COMPILE-DOCKER', 'dotnet3.1', 'mcr.microsoft.com/dotnet/sdk', '3.1');
INSERT INTO `build_image`(`id`, `job_type`, `display_name`, `image`, `tag`) VALUES (66, 'DOTNET-COMPILE-DOCKER', 'dotnet5.0', 'mcr.microsoft.com/dotnet/sdk', '5.0');

# 重构流水线，阶段，job，环境变量，通知的表结构
CREATE TABLE `pipeline`
(
    `id`                  bigint(20)   NOT NULL AUTO_INCREMENT,
    `company_id`          bigint(20)   NOT NULL DEFAULT '0',
    `repo_id`             bigint(20)   NOT NULL DEFAULT '0',
    `name`                varchar(255) NOT NULL DEFAULT '',
    `job_timeout_minute`  int(10)      NOT NULL DEFAULT '0',
    `match_all_branch`    tinyint(1)   NOT NULL DEFAULT '0',
    `use_default_cluster` tinyint(1)   NOT NULL DEFAULT '0',
    `cluster_name`        varchar(255) NOT NULL DEFAULT '',
    `create_user`         varchar(255) NOT NULL DEFAULT '',
    `create_time`         datetime     NOT NULL,
    `modify_time`         datetime     NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    `resource_type`       varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_company` (`company_id`) USING BTREE,
    KEY `idx_repo_id` (`repo_id`) USING BTREE,
    KEY `idx_name` (`name`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `stage`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `pipeline_id` bigint(20)   NOT NULL DEFAULT '0',
    `upstream_id` bigint(20)   NOT NULL DEFAULT '0',
    `name`        varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `job`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `stage_id`    bigint(20)   NOT NULL DEFAULT '0',
    `name`        varchar(255) NOT NULL DEFAULT '0',
    `auto_build`  tinyint(255) NOT NULL DEFAULT '1',
    `job_type`    varchar(255) NOT NULL DEFAULT '',
    `plugin_type` varchar(255) NOT NULL DEFAULT '',
    `plugin_id`   bigint(20)   NOT NULL DEFAULT '0',
    `upstream_id` bigint(20)   NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_stage_id` (`stage_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `variable`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `pipeline_id` bigint(20)   NOT NULL DEFAULT '0',
    `env_key`     varchar(255) NOT NULL DEFAULT '',
    `env_value`   varchar(255) NOT NULL DEFAULT '',
    `description` varchar(255) NOT NULL DEFAULT '',
    `secret`      tinyint(1)   NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `pipeline_record`
(
    `id`                 bigint(20)   NOT NULL AUTO_INCREMENT,
    `repo_id`            bigint(20)   NOT NULL DEFAULT '0',
    `pipeline_id`        bigint(20)   NOT NULL DEFAULT '0',
    `pipeline_name`      varchar(255) NOT NULL DEFAULT '',
    `job_timeout_minute` bigint(20)   NOT NULL DEFAULT '30',
    `branch_name`        varchar(255) NOT NULL DEFAULT '',
    `commit_id`          varchar(255) NOT NULL DEFAULT '',
    `build_number`       bigint(20)   NOT NULL DEFAULT '0',
    `snapshot_version`   varchar(255) NOT NULL DEFAULT '',
    `release_version`    varchar(255) NOT NULL DEFAULT '',
    `trigger_mode`       varchar(255) NOT NULL DEFAULT '',
    `trigger_user`       varchar(255) NOT NULL DEFAULT '',
    `status`             varchar(255) NOT NULL DEFAULT '',
    `external_key`       varchar(255) NOT NULL DEFAULT '',
    `dashboard_url`      varchar(255) NOT NULL DEFAULT '',
    `create_time`        datetime              DEFAULT NULL,
    `modify_time`        datetime              DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE,
    KEY `idx_repo_id` (`repo_id`) USING BTREE,
    KEY `idx_branch` (`branch_name`) USING BTREE,
    KEY `idx_status` (`status`) USING BTREE,
    KEY `idx_trigger_mode` (`trigger_mode`) USING BTREE,
    KEY `idx_external_key` (`external_key`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `notice`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `pipeline_id` bigint(20)   NOT NULL DEFAULT '0',
    `member_type` varchar(255) NOT NULL DEFAULT 'USER',
    `name`        varchar(255) NOT NULL DEFAULT '',
    `start`       tinyint(1)   NOT NULL DEFAULT '0',
    `success`     tinyint(1)   NOT NULL DEFAULT '0',
    `failed`      tinyint(1)   NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `stage_record`
(
    `id`                 bigint(20)   NOT NULL AUTO_INCREMENT,
    `name`               varchar(255) NOT NULL DEFAULT '',
    `pipeline_id`        bigint(20)   NOT NULL DEFAULT '0',
    `pipeline_record_id` bigint(20)   NOT NULL DEFAULT '0',
    `upstream_id`        bigint(20)   NOT NULL DEFAULT '0',
    `status`             varchar(255) NOT NULL DEFAULT '',
    `create_time`        datetime              DEFAULT NULL,
    `modify_time`        datetime              DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE,
    KEY `idx_pipeline_record_id` (`pipeline_record_id`) USING BTREE,
    KEY `idx_upstream_id` (`upstream_id`) USING BTREE,
    KEY `idx_status` (`status`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `job_record`
(
    `id`                 bigint(20)   NOT NULL AUTO_INCREMENT,
    `repo_id`            bigint(20)   NOT NULL DEFAULT '0',
    `pipeline_id`        bigint(20)   NOT NULL DEFAULT '0',
    `external_job_id`    bigint(20)   NOT NULL DEFAULT '0',
    `plugin_id`          bigint(20)   NOT NULL DEFAULT '0',
    `pipeline_record_id` bigint(20)   NOT NULL DEFAULT '0',
    `stage_record_id`    bigint(20)   NOT NULL DEFAULT '0',
    `plugin_record_id`   bigint(20)   NOT NULL DEFAULT '0',
    `upstream_id`        bigint(20)   NOT NULL DEFAULT '0',
    `name`               varchar(255) NOT NULL DEFAULT '',
    `auto_build`         tinyint(1)   NOT NULL DEFAULT '1',
    `job_type`           varchar(255) NOT NULL DEFAULT '',
    `plugin_type`        varchar(255) NOT NULL DEFAULT '',
    `status`             varchar(255) NOT NULL DEFAULT '',
    `has_log`            tinyint(1)   NOT NULL DEFAULT '0',
    `task_id`            bigint(20)   NOT NULL DEFAULT '0',
    `log_name`           varchar(255) NOT NULL DEFAULT '',
    `log_token`          varchar(255) NOT NULL DEFAULT '',
    `trigger_user`       varchar(255) NOT NULL DEFAULT '',
    `trigger_mode`       varchar(255) NOT NULL DEFAULT '',
    `message`            varchar(500) NOT NULL DEFAULT '',
    `detail_url`         varchar(255) NOT NULL DEFAULT '',
    `create_time`        datetime              DEFAULT NULL,
    `modify_time`        datetime              DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_repo_id` (`repo_id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE,
    KEY `idx_upstream_id` (`upstream_id`) USING BTREE,
    KEY `idx_pipeline_record_id` (`pipeline_record_id`) USING BTREE,
    KEY `idx_stage_record_id` (`stage_record_id`) USING BTREE,
    KEY `idx_external_job_id` (`external_job_id`) USING BTREE,
    KEY `idx_status` (`status`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;