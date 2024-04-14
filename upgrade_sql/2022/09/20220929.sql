# 增加长度
ALTER TABLE `plugin_artifact_release_build`
    MODIFY COLUMN `message` text,
    MODIFY COLUMN `card_keys` text;

ALTER TABLE `release_version`
    MODIFY COLUMN `message` text;