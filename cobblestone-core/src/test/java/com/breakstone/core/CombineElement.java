package com.breakstone.core;

import com.sun.javafx.scene.control.skin.VirtualFlow;

import java.util.ArrayList;
import java.util.List;

public class CombineElement {

    private Integer index;

    /**
     * 元素下标
     */
    private int[] elementIndexList;
    /**
     * 下标之和，用于排序
     */
    private Integer indexSum;

    public CombineElement(int elementSize) {
        this.elementIndexList = new int[elementSize];
    }

    public int[] getElementIndexList() {
        return elementIndexList;
    }

    public void setElementIndexList(int[] elementIndexList) {
        this.elementIndexList = elementIndexList;
    }

    public Integer getIndexSum() {
        return indexSum;
    }

    public void setIndexSum(Integer indexSum) {
        this.indexSum = indexSum;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
