# 支持clone的策略
ALTER TABLE `plugin_host_compile_config`
    ADD COLUMN `clone_mode` varchar(255) NOT NULL DEFAULT 'SINGLE_COMMIT';
ALTER TABLE `plugin_docker_executor_config`
    ADD COLUMN `clone_mode` varchar(255) NOT NULL DEFAULT 'SINGLE_COMMIT';
ALTER TABLE `plugin_shell_executor_config`
    ADD COLUMN `clone_mode` varchar(255) NOT NULL DEFAULT 'SINGLE_COMMIT';
ALTER TABLE `plugin_cobertura_test_config`
    ADD COLUMN `clone_mode` varchar(255) NOT NULL DEFAULT 'SINGLE_COMMIT';
ALTER TABLE `plugin_jacoco_test_config`
    ADD COLUMN `clone_mode` varchar(255) NOT NULL DEFAULT 'SINGLE_COMMIT';
ALTER TABLE `plugin_junit_test_config`
    ADD COLUMN `clone_mode` varchar(255) NOT NULL DEFAULT 'SINGLE_COMMIT';
ALTER TABLE `plugin_maven_test_config`
    ADD COLUMN `clone_mode` varchar(255) NOT NULL DEFAULT 'SINGLE_COMMIT';
ALTER TABLE `plugin_helm_package_config`
    ADD COLUMN `clone_mode` varchar(255) NOT NULL DEFAULT 'SINGLE_COMMIT';
