package com.ezone.devops.pipeline.vo;

import com.ezone.devops.pipeline.enums.UserType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class UserInfo {

    private String name;
    private UserType type;

}
