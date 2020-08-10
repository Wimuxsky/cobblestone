package com.breakstone.cobble.tool.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: siqigang
 * @Date: 2020/8/10 下午3:06
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextElementInfo {

    private LocationInfo locationInfo;

    // 字体
    private String font;

    // 文字大小
    private Integer fontSize;

    // 文案颜色
    private String color;

}
