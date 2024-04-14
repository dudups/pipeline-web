# 删除代码库表，增加repo_key字段

ALTER TABLE `plugin_artifact_release_build`
    ADD COLUMN `card_keys` varchar(500) NOT NULL DEFAULT '' AFTER `push_tag`;

ALTER TABLE `pipeline`
    ADD COLUMN `repo_key` varchar(255) NOT NULL DEFAULT '' AFTER `repo_id`,
    ADD INDEX `idx_repo_key` (`repo_key`) USING BTREE;

ALTER TABLE `pipeline_record`
    ADD COLUMN `repo_key` varchar(255) NOT NULL DEFAULT '' AFTER `repo_id`,
    ADD INDEX `idx_repo_key` (`repo_key`) USING BTREE;

ALTER TABLE `release_version`
    ADD COLUMN `repo_key` varchar(255) NOT NULL DEFAULT '' AFTER `repo_id`,
    ADD INDEX `idx_repo_key` (`repo_key`) USING BTREE;

ALTER TABLE `snapshot_version`
    ADD COLUMN `repo_key` varchar(255) NOT NULL DEFAULT '' AFTER `repo_id`,
    ADD INDEX `idx_repo_key` (`repo_key`) USING BTREE;

ALTER TABLE `job_record`
    ADD COLUMN `repo_key` varchar(255) NOT NULL DEFAULT '' AFTER `repo_id`,
    ADD INDEX `idx_repo_key` (`repo_key`) USING BTREE;

ALTER TABLE `web_hook_notice`
    ADD COLUMN `repo_key` varchar(255) NOT NULL DEFAULT '' AFTER `repo_id`,
    ADD INDEX `idx_repo_key` (`repo_key`) USING BTREE;

ALTER TABLE `web_hook_notice_event`
    ADD COLUMN `repo_key` varchar(255) NOT NULL DEFAULT '' AFTER `repo_id`,
    ADD INDEX `idx_repo_key` (`repo_key`) USING BTREE;

ALTER TABLE `plugin_artifact_release_build`
    ADD COLUMN `repo_key` varchar(255) NOT NULL DEFAULT '' AFTER `repo_id`,
    ADD INDEX `idx_repo_key` (`repo_key`) USING BTREE;
