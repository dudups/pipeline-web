# 改造rbac的权限
ALTER TABLE `report_info`
    ADD COLUMN `pipeline_id` bigint(20) NOT NULL DEFAULT 0,
    DROP COLUMN `repo_name`;