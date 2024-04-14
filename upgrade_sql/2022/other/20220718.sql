# 支持自动清理流水线记录

ALTER TABLE `pipeline_record`
    ADD COLUMN `company_id` bigint(20) NOT NULL DEFAULT '0';

CREATE TABLE `pipeline_setting`
(
    `id`                bigint(20) NOT NULL AUTO_INCREMENT,
    `company_id`        bigint(20) NOT NULL DEFAULT '0',
    `record_expire_day` int(10)    NOT NULL DEFAULT '180',
    `report_expire_day` int(10)    NOT NULL DEFAULT '30',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_company_id` (`company_id`) USING BTREE
) ENGINE = InnoDB;

# 刷历史数据
curl -X POST "http://127.0.0.1:8201/internal/migrates/history_data?migrateType=add_company_id"
curl -X POST "http://127.0.0.1:8201/internal/migrates/history_data?migrateType=init_pipeline_setting"
