# 改造统计数据从redis迁移到mysql中

CREATE TABLE `job_measure`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `repo_key`    varchar(255) NOT NULL DEFAULT '',
    `job_type`    varchar(255) NOT NULL DEFAULT '',
    `plugin_type` varchar(255) NOT NULL DEFAULT '',
    `start_time`  datetime              DEFAULT NULL,
    `end_time`    datetime              DEFAULT NULL,
    `success`     tinyint(1)   NOT NULL DEFAULT '1',
    `cost_time`   bigint(20)   NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_repo_key` (`repo_key`) USING BTREE,
    KEY `idx_job_type` (`job_type`) USING BTREE,
    KEY `idx_plugin_type` (`plugin_type`) USING BTREE,
    KEY `idx_start_time` (`start_time`) USING BTREE,
    KEY `idx_end_time` (`end_time`) USING BTREE
) ENGINE = InnoDB;

# 刷历史数据
curl -X POST "http://127.0.0.1:8201/internal/migrates/history_data?migrateType=INIT_MEASURE"
