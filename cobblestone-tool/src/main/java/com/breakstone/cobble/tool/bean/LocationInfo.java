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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationInfo {

    private int x;

    private int y;
}
