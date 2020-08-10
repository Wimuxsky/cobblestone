package com.breakstone.cobble.tool.bean;

import com.breakstone.cobble.tool.enums.ElementTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: siqigang
 * @Date: 2020/8/10 下午3:01
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseElementInfo {
    private int index;

    private String content;

    private ElementTypeEnum type;
}
