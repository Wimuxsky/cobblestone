package com.breakstone.cobble.core.test.combine;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CombineTest {

    @Test
    public void test() {
        // 2图1文
        List<CombineElement> pic2List = getCandidateList(0, 2, 30, 1);
        List<CombineElement> txt1List = getCandidateList(2, 1, 30, 2);


        List<CombineElement> aList = combineCandidate(pic2List.get(0), pic2List.get(1), 3);
        List<CombineElement> bList = combineCandidate(txt1List.get(0), null, 3);

        List<CombineElement> combineList = combineCombineList(aList, bList);
        Collections.sort(combineList, Comparator.comparingInt(x -> x.getElementIndexSum()));
        for (int i = 0; i < combineList.size(); i++) {
            combineList.get(i).setIndex(i);
        }
        print(combineList);
    }


    private void print(List<CombineElement> combineList) {
        combineList.forEach(x -> System.out.println(x.getIndex() + ":\t" + x.getElementIndexSum() + "\t:\t" + JSON.toJSONString(x.getElementIndexArr())));
    }

    private List<CombineElement> getCandidateList(int startIndex, int elementCount, int candidateSize, int factor) {
        List<CombineElement> candidateList = Lists.newArrayList();
        for (int i = 0; i < elementCount; i++) {
            candidateList.add(CombineElement.generateCandidate(startIndex + i, candidateSize, factor));
        }
        return candidateList;
    }


    public List<CombineElement> combineCandidate(CombineElement candidateA, CombineElement candidateB, int elementCount) {
        List<CombineElement> combineElementList = Lists.newArrayList();
        if (null != candidateA && null != candidateB) {
            for (int i = 0; i < candidateA.getElementIndexArr().length; i++) {
                for (int j = 0; j < candidateB.getElementIndexArr().length; j++) {
                    CombineElement combineElement = CombineElement.getInitCombineElement(elementCount);
                    combineElement.getElementIndexArr()[candidateA.getIndex()] = candidateA.getElementIndexArr()[i];
                    combineElement.getElementIndexArr()[candidateB.getIndex()] = candidateB.getElementIndexArr()[j];
                    combineElement.setElementIndexSum(candidateA.getElementIndexArr()[i] + candidateB.getElementIndexArr()[j]);
                    combineElementList.add(combineElement);
                }
            }
        } else if (null != candidateA) {
            for (int i = 0; i < candidateA.getElementIndexArr().length; i++) {
                CombineElement combineElement = CombineElement.getInitCombineElement(elementCount);
                combineElement.getElementIndexArr()[candidateA.getIndex()] = candidateA.getElementIndexArr()[i];
                combineElement.setElementIndexSum(candidateA.getElementIndexArr()[i]);
                combineElementList.add(combineElement);
            }
        }

        return combineElementList;
    }


    /**
     * 组合 组合列表
     *
     * @param aList
     * @param bList
     * @return
     */
    public List<CombineElement> combineCombineList(List<CombineElement> aList, List<CombineElement> bList) {
        if (CollectionUtils.isEmpty(bList)) {
            return aList;
        }
        List<CombineElement> combineElementList = Lists.newArrayList();
        for (CombineElement combineA : aList) {
            for (CombineElement combineB : bList) {
                CombineElement combineElement = CombineElement.getInitCombineElement(combineA.getElementIndexArr().length);
                for (int i = 0; i < combineElement.getElementIndexArr().length; i++) {
                    combineElement.getElementIndexArr()[i] = combineA.getElementIndexArr()[i] + combineB.getElementIndexArr()[i];
                    CombineElement.calculateIndexSum(combineElement);
                    combineElement.setElementIndexSum(combineA.getElementIndexSum() + combineB.getElementIndexSum());
                }
                combineElementList.add(combineElement);
            }
        }
        return combineElementList;
    }


}
