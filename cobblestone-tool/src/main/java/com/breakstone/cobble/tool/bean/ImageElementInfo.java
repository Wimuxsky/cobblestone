package com.breakstone.cobble.tool.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: siqigang
 * @Date: 2020/8/10 下午3:05
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageElementInfo extends BaseElementInfo{

    private int width;

    private int height;

    private LocationInfo location;

    // 透明度

}
