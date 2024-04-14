package com.ezone.devops.migrate.service;

public interface MigrateService {

    String getMigrateType();

    String getMigrateDesc();

    boolean migrate(boolean deleteHistory);

}
