# helm package支持构建版本和发版版本
ALTER TABLE `plugin_helm_package_config`
    CHANGE COLUMN `app_version_type` `app_custom_version` varchar(255) NULL,
    ADD COLUMN `chart_version_type` varchar(255) NOT NULL DEFAULT 'DEFAULT_VERSION',
    ADD COLUMN `app_version_type`   varchar(255) NOT NULL DEFAULT 'DEFAULT_VERSION';

# 刷历史数据
curl -X POST "http://127.0.0.1:8201/internal/migrates/history_data?migrateType=MIGRATE_HELM_PACKAGE_JOB_VERSION"