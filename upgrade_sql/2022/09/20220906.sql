# sonar支持安全热点
ALTER TABLE `plugin_sonarqube_build`
    ADD COLUMN `security_hotspots`          int(10)    NULL,
    ADD COLUMN `supported_security_hotspot` tinyint(1) NOT NULL DEFAULT '0';