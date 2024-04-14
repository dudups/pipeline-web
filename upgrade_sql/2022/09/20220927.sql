# 主机部署插件增加主机组id和模板id
ALTER TABLE `plugin_host_deploy_build`
    ADD COLUMN `group_id`    bigint(20) NOT NULL DEFAULT '0',
    ADD COLUMN `template_id` bigint(1)  NOT NULL DEFAULT '0';