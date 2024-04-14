# 调整所有shell脚本的长度为30000
ALTER TABLE `plugin_maven_test_config` MODIFY COLUMN `command` text NULL;
ALTER TABLE `plugin_junit_test_config` MODIFY COLUMN `command` text NULL;
ALTER TABLE `plugin_jacoco_test_config` MODIFY COLUMN `command` text NULL;
ALTER TABLE `plugin_cobertura_test_config` MODIFY COLUMN `command` text NULL;
ALTER TABLE `plugin_shell_executor_config` MODIFY COLUMN `command` text NULL;
ALTER TABLE `plugin_docker_executor_config` MODIFY COLUMN `command` text NULL;
ALTER TABLE `plugin_helm_package_config` MODIFY COLUMN `command` text NULL;
ALTER TABLE `plugin_host_compile_config` MODIFY COLUMN `build_command` text NULL;

