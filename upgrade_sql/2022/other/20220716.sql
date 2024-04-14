# go docker
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('GO-COMPILE-DOCKER', 'go-1.14.15', 'ezone-public/golang', '1.14.15-alpine3.13');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('GO-COMPILE-DOCKER', 'go-1.15.15', 'ezone-public/golang', '1.15.15-alpine3.14');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('GO-COMPILE-DOCKER', 'go-1.16.14', 'ezone-public/golang', '1.16.14-alpine3.15');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('GO-COMPILE-DOCKER', 'go-1.17.11', 'ezone-public/golang', '1.17.11-alpine3.15');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('GO-COMPILE-DOCKER', 'go-1.18.3', 'ezone-public/golang', '1.18.3-alpine3.15');

# composer
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('PHP-COMPILE', 'composer-1.10.26', 'ezone-public/composer', '1.10.26');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('PHP-COMPILE', 'composer-2', 'ezone-public/composer', '2');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('PHP-COMPILE', 'composer-2.2', 'ezone-public/composer', '2.2');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('PHP-COMPILE', 'composer-2.3', 'ezone-public/composer', '2.3');

# composer docker
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('PHP-COMPILE-DOCKER', 'composer-1.10.26', 'ezone-public/composer', '1.10.26');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('PHP-COMPILE-DOCKER', 'composer-2', 'ezone-public/composer', '2');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('PHP-COMPILE-DOCKER', 'composer-2.2', 'ezone-public/composer', '2.2');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('PHP-COMPILE-DOCKER', 'composer-2.3', 'ezone-public/composer', '2.3');

# helm package
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('HELM-PACKAGE', 'helm-3.8.2', 'ezone-public/helm', '3.8.2');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('HELM-PACKAGE', 'helm-3.9.0', 'ezone-public/helm', '3.9.0');

# python
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('PYTHON-COMPILE', 'python3.8', 'ezone-public/python', '3.8-alpine3.16');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('PYTHON-COMPILE', 'python3.9', 'ezone-public/python', '3.9-alpine3.16');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('PYTHON-COMPILE', 'python3.10', 'ezone-public/python', '3.10-alpine3.16');

# python docker
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('PYTHON-COMPILE-DOCKER', 'python3.8', 'ezone-public/python', '3.8-alpine3.16');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('PYTHON-COMPILE-DOCKER', 'python3.9', 'ezone-public/python', '3.9-alpine3.16');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('PYTHON-COMPILE-DOCKER', 'python3.10', 'ezone-public/python', '3.10-alpine3.16');

# maven
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('MAVEN-COMPILE', 'maven3.8.5-jdk11', 'ezone-public/maven', '3.8.5-jdk-11');

# maven docker
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('MAVEN-COMPILE-DOCKER', 'maven3.8.5-jdk11', 'ezone-public/maven', '3.8.5-jdk-11');

# gradle
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('GRADLE-COMPILE', 'gradle5.4.1-jdk8', 'ezone-public/gradle', '5.4.1-jdk8-alpine');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('GRADLE-COMPILE', 'gradle6.9.2-jdk8', 'ezone-public/gradle', '6.9.2-jdk8');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('GRADLE-COMPILE', 'gradle7.5-jdk8', 'ezone-public/gradle', '7.5-jdk8');

# gradle docker
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('GRADLE-COMPILE-DOCKER', 'gradle5.4.1-jdk8', 'ezone-public/gradle', '5.4.1-jdk8-alpine');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('GRADLE-COMPILE-DOCKER', 'gradle6.9.2-jdk8', 'ezone-public/gradle', '6.9.2-jdk8');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('GRADLE-COMPILE-DOCKER', 'gradle7.5-jdk8', 'ezone-public/gradle', '7.5-jdk8');

# cmake
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('CMAKE-COMPILE', 'gradle9.5.0', 'ezone-public/gcc', '9.5.0');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('CMAKE-COMPILE', 'gradle10.4.0', 'ezone-public/gcc', '10.4.0');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('CMAKE-COMPILE', 'gradle11.3.0', 'ezone-public/gcc', '11.3.0');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('CMAKE-COMPILE', 'gradle12.1.0', 'ezone-public/gcc', '12.1.0');

# cmake docker
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('CMAKE-COMPILE-DOCKER', 'gradle9.5.0', 'ezone-public/gcc', '9.5.0');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('CMAKE-COMPILE-DOCKER', 'gradle10.4.0', 'ezone-public/gcc', '10.4.0');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('CMAKE-COMPILE-DOCKER', 'gradle11.3.0', 'ezone-public/gcc', '11.3.0');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('CMAKE-COMPILE-DOCKER', 'gradle12.1.0', 'ezone-public/gcc', '12.1.0');
