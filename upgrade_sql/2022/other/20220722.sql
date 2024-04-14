#
发版支持传sql
ALTER TABLE `plugin_artifact_release_build`
    ADD COLUMN `sql_script` text NULL;