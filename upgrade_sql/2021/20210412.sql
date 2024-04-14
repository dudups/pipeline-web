# 支持用户组
ALTER TABLE `notice_config`
    CHANGE COLUMN `username` `name`    varchar(255) NOT NULL DEFAULT '' AFTER `pipeline_id`,
    CHANGE COLUMN `finished` `success` tinyint(1)   NOT NULL DEFAULT 0 AFTER `start`,
    ADD COLUMN `member_type`           varchar(255) NOT NULL DEFAULT 'USER' AFTER `pipeline_id`;
ALTER TABLE `notice_config_1`
    CHANGE COLUMN `username` `name`    varchar(255) NOT NULL DEFAULT '' AFTER `pipeline_id`,
    CHANGE COLUMN `finished` `success` tinyint(1)   NOT NULL DEFAULT 0 AFTER `start`,
    ADD COLUMN `member_type`           varchar(255) NOT NULL DEFAULT 'USER' AFTER `pipeline_id`;
ALTER TABLE `notice_config_2`
    CHANGE COLUMN `username` `name`    varchar(255) NOT NULL DEFAULT '' AFTER `pipeline_id`,
    CHANGE COLUMN `finished` `success` tinyint(1)   NOT NULL DEFAULT 0 AFTER `start`,
    ADD COLUMN `member_type`           varchar(255) NOT NULL DEFAULT 'USER' AFTER `pipeline_id`;
ALTER TABLE `notice_config_3`
    CHANGE COLUMN `username` `name`    varchar(255) NOT NULL DEFAULT '' AFTER `pipeline_id`,
    CHANGE COLUMN `finished` `success` tinyint(1)   NOT NULL DEFAULT 0 AFTER `start`,
    ADD COLUMN `member_type`           varchar(255) NOT NULL DEFAULT 'USER' AFTER `pipeline_id`;
ALTER TABLE `notice_config_4`
    CHANGE COLUMN `username` `name`    varchar(255) NOT NULL DEFAULT '' AFTER `pipeline_id`,
    CHANGE COLUMN `finished` `success` tinyint(1)   NOT NULL DEFAULT 0 AFTER `start`,
    ADD COLUMN `member_type`           varchar(255) NOT NULL DEFAULT 'USER' AFTER `pipeline_id`;
ALTER TABLE `notice_config_5`
    CHANGE COLUMN `username` `name`    varchar(255) NOT NULL DEFAULT '' AFTER `pipeline_id`,
    CHANGE COLUMN `finished` `success` tinyint(1)   NOT NULL DEFAULT 0 AFTER `start`,
    ADD COLUMN `member_type`           varchar(255) NOT NULL DEFAULT 'USER' AFTER `pipeline_id`;
ALTER TABLE `notice_config_6`
    CHANGE COLUMN `username` `name`    varchar(255) NOT NULL DEFAULT '' AFTER `pipeline_id`,
    CHANGE COLUMN `finished` `success` tinyint(1)   NOT NULL DEFAULT 0 AFTER `start`,
    ADD COLUMN `member_type`           varchar(255) NOT NULL DEFAULT 'USER' AFTER `pipeline_id`;
ALTER TABLE `notice_config_7`
    CHANGE COLUMN `username` `name`    varchar(255) NOT NULL DEFAULT '' AFTER `pipeline_id`,
    CHANGE COLUMN `finished` `success` tinyint(1)   NOT NULL DEFAULT 0 AFTER `start`,
    ADD COLUMN `member_type`           varchar(255) NOT NULL DEFAULT 'USER' AFTER `pipeline_id`;
ALTER TABLE `notice_config_8`
    CHANGE COLUMN `username` `name`    varchar(255) NOT NULL DEFAULT '' AFTER `pipeline_id`,
    CHANGE COLUMN `finished` `success` tinyint(1)   NOT NULL DEFAULT 0 AFTER `start`,
    ADD COLUMN `member_type`           varchar(255) NOT NULL DEFAULT 'USER' AFTER `pipeline_id`;
ALTER TABLE `notice_config_9`
    CHANGE COLUMN `username` `name`    varchar(255) NOT NULL DEFAULT '' AFTER `pipeline_id`,
    CHANGE COLUMN `finished` `success` tinyint(1)   NOT NULL DEFAULT 0 AFTER `start`,
    ADD COLUMN `member_type`           varchar(255) NOT NULL DEFAULT 'USER' AFTER `pipeline_id`;
ALTER TABLE `notice_config_10`
    CHANGE COLUMN `username` `name`    varchar(255) NOT NULL DEFAULT '' AFTER `pipeline_id`,
    CHANGE COLUMN `finished` `success` tinyint(1)   NOT NULL DEFAULT 0 AFTER `start`,
    ADD COLUMN `member_type`           varchar(255) NOT NULL DEFAULT 'USER' AFTER `pipeline_id`;