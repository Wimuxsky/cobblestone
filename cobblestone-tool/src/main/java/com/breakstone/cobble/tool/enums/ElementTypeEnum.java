package com.breakstone.cobble.tool.enums;

import lombok.Getter;

/**
 * @Author: siqigang
 * @Date: 2020/8/10 下午3:03
 **/
public enum ElementTypeEnum {
    IMAGE("image", "图片元素"),
    TEXT("text", "文案元素");


    @Getter
    private String type;

    @Getter
    private String desc;

    private ElementTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
