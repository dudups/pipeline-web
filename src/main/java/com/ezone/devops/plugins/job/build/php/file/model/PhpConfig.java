package com.ezone.devops.plugins.job.build.php.file.model;

import com.ezone.devops.plugins.job.build.php.file.dao.PhpConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "plugin_php_config")
public class PhpConfig extends LongID {

    @Column(PhpConfigDao.ID)
    private Long id;
    @Column(PhpConfigDao.DATA_JSON)
    private String dataJson;
}