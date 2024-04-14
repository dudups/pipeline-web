package com.ezone.devops.migrate.service;

import com.ezone.galaxy.framework.common.bean.BaseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MigrateServiceFactory {

    private final Map<String, MigrateService> migrateFactory = new HashMap<>();
    private final Map<String, String> infos = new HashMap<>();

    @Autowired
    public void init(List<MigrateService> migrateServices) {
        for (MigrateService migrateService : migrateServices) {
            String migrateType = migrateService.getMigrateType();
            if (StringUtils.isBlank(migrateType)) {
                throw new RuntimeException("migrate " + migrateService.getClass().getSimpleName() + " type is null");
            }
            if (migrateFactory.containsKey(migrateType)) {
                throw new RuntimeException("migrate service [" + migrateType + "] already exist");
            }
            infos.put(migrateType, migrateService.getMigrateDesc());
            migrateFactory.put(migrateType, migrateService);
        }
    }

    public MigrateService getMigrateService(String migrateType) {
        MigrateService migrateService = migrateFactory.get(migrateType);
        if (migrateService == null) {
            throw new BaseException(HttpStatus.NOT_FOUND.value(), "migrate type not exist");
        }
        return migrateService;
    }

    public Map<String, String> listMigrates() {
        return infos;
    }

}
