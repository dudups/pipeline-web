# 刷helm部署的历史数据
curl
-X POST "http://127.0.0.1:8201/internal/migrates/history_data?migrateType=HELM_DEPLOY_CLUSTER_KEY"

# 刷helm package历史数据
curl -X POST "http://127.0.0.1:8201/internal/migrates/history_data?migrateType=MIGRATE_HELM_PACKAGE_JOB_TYPE"

# 刷统计相关的数据
curl -X POST "http://127.0.0.1:8201/internal/migrates/history_data?migrateType=INIT_MEASURE"

ALTER TABLE `job_record`
    ADD INDEX `idx_plugin_type` (`plugin_type`) USING BTREE,
    ADD INDEX `idx_job_type` (`job_type`) USING BTREE,
    ADD INDEX `idx_create_time` (`create_time`) USING BTREE,
    ADD INDEX `idx_modify_time` (`modify_time`) USING BTREE;