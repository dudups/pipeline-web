package com.ezone.devops.plugins.job;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.annotation.ManualField;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.util.JsonUtils;

import java.lang.reflect.Field;

public interface PluginDataOperator<CONFIG, BUILD> {

    default boolean checkJob(Job job, JSONObject json) {
        return true;
    }

    default Long saveRealJob(String jobType, JSONObject json) {
        return 0L;
    }

    default boolean deleteRealJob(Long id) {
        return true;
    }

    default boolean updateRealJobRecord(Long realJobRecordId, JSONObject buildJson) {
        return true;
    }

    default boolean updateRealJobBuildWithFields(Long realJobRecordId, JSONObject updateFields) throws BaseException {
        if (null == updateFields) {
            return false;
        }
        BUILD build = getRealJobRecord(realJobRecordId);
        if (null == build) {
            return false;
        }
        JSONObject buildJson = JSONObject.parseObject(JsonUtils.toJson(build));
        if (null == buildJson) {
            return false;
        }
        Class buildClass = build.getClass();
        Field[] fields = buildClass.getDeclaredFields();

        boolean update = false;
        for (Field field : fields) {
            if (field.isAnnotationPresent(ManualField.class) && updateFields.containsKey(field.getName())) {
                buildJson.put(field.getName(), updateFields.get(field.getName()));
                update = true;
            }
        }

        if (update) {
            updateRealJobRecord(realJobRecordId, buildJson);
        }
        return true;
    }

    default CONFIG getRealJob(Long id) {
        return null;
    }

    default BUILD getRealJobRecord(Long id) {
        return null;
    }

    default Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        return 0L;
    }

}
