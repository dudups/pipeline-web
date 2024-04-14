package com.ezone.devops.pipeline.web.response;

import com.ezone.devops.pipeline.model.Stage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StageVo {

    private Long id;
    private String stageName;

    public StageVo(Stage stage) {
        setId(stage.getId());
        setStageName(stage.getName());
    }
}
