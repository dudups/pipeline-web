package com.ezone.devops.migrate.web;

import com.ezone.devops.migrate.service.MigrateService;
import com.ezone.devops.migrate.service.MigrateServiceFactory;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "迁移历史数据api")
@Slf4j
@RestController
@RequestMapping("/internal/migrates")
public class MigrateController {

    @Autowired
    private MigrateServiceFactory migrateServiceFactory;

    @ApiOperation("列出所有可迁移类型")
    @GetMapping("/list_migrate_type")
    public BaseResponse<?> listMigrateType() {
        return new BaseResponse<>(HttpCode.SUCCESS, migrateServiceFactory.listMigrates());
    }

    @ApiOperation("迁移历史数据")
    @PostMapping("/history_data")
    public BaseResponse<?> migratePluginData(@RequestParam String migrateType,
                                             @RequestParam(defaultValue = "false") boolean deleteHistory) {
        MigrateService migrateService = migrateServiceFactory.getMigrateService(migrateType);
        return new BaseResponse<>(HttpCode.SUCCESS, migrateService.migrate(deleteHistory));
    }

}

