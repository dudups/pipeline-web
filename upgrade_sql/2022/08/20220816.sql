# 构建类的支持自定义版本
ALTER TABLE `plugin_host_compile_config`
    ADD COLUMN `version_type`   varchar(255) NOT NULL DEFAULT 'SNAPSHOT',
    ADD COLUMN `custom_version` varchar(255) NOT NULL DEFAULT '';