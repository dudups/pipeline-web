# 支持job的程序控制

ALTER TABLE `job`
    ADD COLUMN `condition_type`         varchar(255) NOT NULL DEFAULT '',
    ADD COLUMN `condition_trigger_type` varchar(255) NOT NULL DEFAULT '';

ALTER TABLE `job_record`
    ADD COLUMN `job_id`                 bigint(20)   NOT NULL DEFAULT '0',
    ADD COLUMN `condition_type`         varchar(255) NOT NULL DEFAULT '',
    ADD COLUMN `condition_trigger_type` varchar(255) NOT NULL DEFAULT '';

CREATE TABLE `job_condition_variable`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `pipeline_id` bigint(20)   NOT NULL DEFAULT '0',
    `job_id`      bigint(20)   NOT NULL DEFAULT '0',
    `env_key`     varchar(255) NOT NULL DEFAULT '',
    `env_value`   varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE,
    KEY `idx_job_id` (`job_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `job_condition_variable_1`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `pipeline_id` bigint(20)   NOT NULL DEFAULT '0',
    `job_id`      bigint(20)   NOT NULL DEFAULT '0',
    `env_key`     varchar(255) NOT NULL DEFAULT '',
    `env_value`   varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE,
    KEY `idx_job_id` (`job_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `job_condition_variable_2`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `pipeline_id` bigint(20)   NOT NULL DEFAULT '0',
    `job_id`      bigint(20)   NOT NULL DEFAULT '0',
    `env_key`     varchar(255) NOT NULL DEFAULT '',
    `env_value`   varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE,
    KEY `idx_job_id` (`job_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `job_condition_variable_3`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `pipeline_id` bigint(20)   NOT NULL DEFAULT '0',
    `job_id`      bigint(20)   NOT NULL DEFAULT '0',
    `env_key`     varchar(255) NOT NULL DEFAULT '',
    `env_value`   varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE,
    KEY `idx_job_id` (`job_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `job_condition_variable_4`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `pipeline_id` bigint(20)   NOT NULL DEFAULT '0',
    `job_id`      bigint(20)   NOT NULL DEFAULT '0',
    `env_key`     varchar(255) NOT NULL DEFAULT '',
    `env_value`   varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE,
    KEY `idx_job_id` (`job_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `job_condition_variable_5`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `pipeline_id` bigint(20)   NOT NULL DEFAULT '0',
    `job_id`      bigint(20)   NOT NULL DEFAULT '0',
    `env_key`     varchar(255) NOT NULL DEFAULT '',
    `env_value`   varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE,
    KEY `idx_job_id` (`job_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `job_condition_variable_6`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `pipeline_id` bigint(20)   NOT NULL DEFAULT '0',
    `job_id`      bigint(20)   NOT NULL DEFAULT '0',
    `env_key`     varchar(255) NOT NULL DEFAULT '',
    `env_value`   varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE,
    KEY `idx_job_id` (`job_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `job_condition_variable_7`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `pipeline_id` bigint(20)   NOT NULL DEFAULT '0',
    `job_id`      bigint(20)   NOT NULL DEFAULT '0',
    `env_key`     varchar(255) NOT NULL DEFAULT '',
    `env_value`   varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE,
    KEY `idx_job_id` (`job_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `job_condition_variable_8`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `pipeline_id` bigint(20)   NOT NULL DEFAULT '0',
    `job_id`      bigint(20)   NOT NULL DEFAULT '0',
    `env_key`     varchar(255) NOT NULL DEFAULT '',
    `env_value`   varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE,
    KEY `idx_job_id` (`job_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `job_condition_variable_9`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `pipeline_id` bigint(20)   NOT NULL DEFAULT '0',
    `job_id`      bigint(20)   NOT NULL DEFAULT '0',
    `env_key`     varchar(255) NOT NULL DEFAULT '',
    `env_value`   varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE,
    KEY `idx_job_id` (`job_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `job_condition_variable_10`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `pipeline_id` bigint(20)   NOT NULL DEFAULT '0',
    `job_id`      bigint(20)   NOT NULL DEFAULT '0',
    `env_key`     varchar(255) NOT NULL DEFAULT '',
    `env_value`   varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_pipeline_id` (`pipeline_id`) USING BTREE,
    KEY `idx_job_id` (`job_id`) USING BTREE
) ENGINE = InnoDB;


# 刷历史数据
curl -X POST "http://127.0.0.1:8201/internal/migrates/history_data?migrateType=add_job_condition"