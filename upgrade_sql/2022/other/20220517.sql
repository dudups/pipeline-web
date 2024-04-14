# 支持运行时环境变量

CREATE TABLE `runtime_variable`
(
    `id`        bigint(20)   NOT NULL AUTO_INCREMENT,
    `repo_key`  varchar(255) NOT NULL DEFAULT '',
    `build_id`  bigint(20)   NOT NULL DEFAULT '0',
    `env_key`   varchar(255) NOT NULL DEFAULT '',
    `env_value` varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_build_id` (`build_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `runtime_variable_1`
(
    `id`        bigint(20)   NOT NULL AUTO_INCREMENT,
    `repo_key`  varchar(255) NOT NULL DEFAULT '',
    `build_id`  bigint(20)   NOT NULL DEFAULT '0',
    `env_key`   varchar(255) NOT NULL DEFAULT '',
    `env_value` varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_build_id` (`build_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `runtime_variable_2`
(
    `id`        bigint(20)   NOT NULL AUTO_INCREMENT,
    `repo_key`  varchar(255) NOT NULL DEFAULT '',
    `build_id`  bigint(20)   NOT NULL DEFAULT '0',
    `env_key`   varchar(255) NOT NULL DEFAULT '',
    `env_value` varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_build_id` (`build_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `runtime_variable_3`
(
    `id`        bigint(20)   NOT NULL AUTO_INCREMENT,
    `repo_key`  varchar(255) NOT NULL DEFAULT '',
    `build_id`  bigint(20)   NOT NULL DEFAULT '0',
    `env_key`   varchar(255) NOT NULL DEFAULT '',
    `env_value` varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_build_id` (`build_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `runtime_variable_4`
(
    `id`        bigint(20)   NOT NULL AUTO_INCREMENT,
    `repo_key`  varchar(255) NOT NULL DEFAULT '',
    `build_id`  bigint(20)   NOT NULL DEFAULT '0',
    `env_key`   varchar(255) NOT NULL DEFAULT '',
    `env_value` varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_build_id` (`build_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `runtime_variable_5`
(
    `id`        bigint(20)   NOT NULL AUTO_INCREMENT,
    `repo_key`  varchar(255) NOT NULL DEFAULT '',
    `build_id`  bigint(20)   NOT NULL DEFAULT '0',
    `env_key`   varchar(255) NOT NULL DEFAULT '',
    `env_value` varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_build_id` (`build_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `runtime_variable_6`
(
    `id`        bigint(20)   NOT NULL AUTO_INCREMENT,
    `repo_key`  varchar(255) NOT NULL DEFAULT '',
    `build_id`  bigint(20)   NOT NULL DEFAULT '0',
    `env_key`   varchar(255) NOT NULL DEFAULT '',
    `env_value` varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_build_id` (`build_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `runtime_variable_7`
(
    `id`        bigint(20)   NOT NULL AUTO_INCREMENT,
    `repo_key`  varchar(255) NOT NULL DEFAULT '',
    `build_id`  bigint(20)   NOT NULL DEFAULT '0',
    `env_key`   varchar(255) NOT NULL DEFAULT '',
    `env_value` varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_build_id` (`build_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `runtime_variable_8`
(
    `id`        bigint(20)   NOT NULL AUTO_INCREMENT,
    `repo_key`  varchar(255) NOT NULL DEFAULT '',
    `build_id`  bigint(20)   NOT NULL DEFAULT '0',
    `env_key`   varchar(255) NOT NULL DEFAULT '',
    `env_value` varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_build_id` (`build_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `runtime_variable_9`
(
    `id`        bigint(20)   NOT NULL AUTO_INCREMENT,
    `repo_key`  varchar(255) NOT NULL DEFAULT '',
    `build_id`  bigint(20)   NOT NULL DEFAULT '0',
    `env_key`   varchar(255) NOT NULL DEFAULT '',
    `env_value` varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_build_id` (`build_id`) USING BTREE
) ENGINE = InnoDB;

CREATE TABLE `runtime_variable_10`
(
    `id`        bigint(20)   NOT NULL AUTO_INCREMENT,
    `repo_key`  varchar(255) NOT NULL DEFAULT '',
    `build_id`  bigint(20)   NOT NULL DEFAULT '0',
    `env_key`   varchar(255) NOT NULL DEFAULT '',
    `env_value` varchar(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_build_id` (`build_id`) USING BTREE
) ENGINE = InnoDB;
