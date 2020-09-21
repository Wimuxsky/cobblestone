package com.breakstone.cobble.core.test.combine;

import java.util.stream.IntStream;

public class CombineElement {

    private int index;

    private int elementIndexSum;

    private int[] elementIndexArr;


    public static CombineElement generateCandidate(int elementIndex, int candidateSize, int factor) {
        if (elementIndex < 0 || candidateSize <= 0 || factor <= 0) {
            throw new IllegalArgumentException("参数异常！");
        }

        CombineElement element = new CombineElement();
        element.setIndex(elementIndex);
        element.setElementIndexArr(IntStream.range(1, candidateSize + 1).map(x -> x * factor).toArray());
        return element;
    }

    public static CombineElement getInitCombineElement(int elementCount) {
        if (elementCount <= 0) {
            throw new IllegalArgumentException("参数异常！");
        }
        CombineElement element = new CombineElement();
        element.setElementIndexArr(new int[elementCount]);
        return element;
    }


    /**
     * 计算元素Index之和，用于排序
     *
     * @param element
     */
    public static void calculateIndexSum(CombineElement element) {
        if (null != element && null != element.elementIndexArr) {
            element.setElementIndexSum(IntStream.of(element.getElementIndexArr()).boxed().reduce(0, Integer::sum));
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getElementIndexSum() {
        return elementIndexSum;
    }

    public void setElementIndexSum(int elementIndexSum) {
        this.elementIndexSum = elementIndexSum;
    }

    public int[] getElementIndexArr() {
        return elementIndexArr;
    }

    public void setElementIndexArr(int[] elementIndexArr) {
        this.elementIndexArr = elementIndexArr;
    }
}
