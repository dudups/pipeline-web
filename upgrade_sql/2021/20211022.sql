# 删除代码库表和代码库repo_id的字段

ALTER TABLE `pipeline`
    DROP COLUMN `repo_id`;

ALTER TABLE `pipeline_record`
    DROP COLUMN `repo_id`;

ALTER TABLE `release_version`
    DROP COLUMN `repo_id`;

ALTER TABLE `snapshot_version`
    DROP COLUMN `repo_id`;

ALTER TABLE `job_record`
    DROP COLUMN `repo_id`;

ALTER TABLE `web_hook_notice`
    DROP COLUMN `repo_id`;

ALTER TABLE `web_hook_notice_event`
    DROP COLUMN `repo_id`;

ALTER TABLE `plugin_artifact_release_build`
    DROP COLUMN `repo_id`;

DROP TABLE `repo`;
