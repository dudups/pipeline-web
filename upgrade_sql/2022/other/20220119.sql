# 触发流水线插件支持回传状态的功能
ALTER TABLE `plugin_call_pipeline_config`
    ADD COLUMN `need_callback` tinyint(1) NOT NULL DEFAULT 0;

ALTER TABLE `pipeline_record`
    ADD COLUMN `callback_url` varchar(500) NOT NULL DEFAULT '';

# 支持helm自定义chart名称
ALTER TABLE `plugin_helm_package_config`
    ADD COLUMN `command` varchar(10000) NOT NULL DEFAULT '',
    ADD COLUMN `use_default_name` tinyint(1)   NOT NULL DEFAULT '1',
    ADD COLUMN `chart_name`       varchar(255) NOT NULL DEFAULT '';
