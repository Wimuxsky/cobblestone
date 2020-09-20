package com.breakstone.core;

import com.alibaba.fastjson.JSON;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CombineTest {


    @Test
    public void combineTest() {
        // 候选集数量
        int candiateset_size = 20;
        // 有效元素数量
        int orderelement_size = 4;
        List<CombineElement> combineList = combineElement(candiateset_size, orderelement_size);
        Collections.sort(combineList, Comparator.comparingInt(x -> x.getIndexSum()));
        for (int i = 0; i < combineList.size(); i++) {
            combineList.get(i).setIndex(i);
        }

        List<Integer> aList = Lists.newArrayList(10, 10, 10, 10);
        List<CombineElement> combineAlist = getTopList(aList,combineList);
        print(combineAlist);
    }

    private void print(List<CombineElement> combineList) {
        combineList.forEach(x-> System.out.println(x.getIndex()+":\t"+x.getIndexSum()+"\t:\t"+JSON.toJSONString(x.getElementIndexList())));
    }

    public List<CombineElement> getTopList(List<Integer> aList,List<CombineElement> combineList) {
        List<CombineElement> combineElementList = Lists.newArrayList();
        for (CombineElement combineElement:combineList) {
            int[] arr = combineElement.getElementIndexList();
            int count = 0;
            for (int i=0;i<aList.size();i++) {
                if (arr[i]<aList.get(i)) {
                    count++;
                }
            }
            if (count == 0) {
                break;
            }
            if (count == aList.size()) {
                combineElementList.add(combineElement);
            }
        }
        return combineElementList;
    }

    public List<CombineElement> combineElement(int candiateset_size, int orderelement_size) {

        List<ElementCandidate> candiateList =
                IntStream.range(0, orderelement_size)
                        .mapToObj(index -> new ElementCandidate(index, candiateset_size))
                        .collect(Collectors.toList());


        List<CombineElement> aList = combineCandidate(candiateList.get(0), candiateList.get(1), orderelement_size);
        List<CombineElement> bList = combineCandidate(candiateList.get(2), candiateList.get(3), orderelement_size);
        return combineCombineElement(aList, bList);
    }

    public List<CombineElement> combineCandidate(ElementCandidate candiateA, ElementCandidate candiateB, int orderelement_size) {
        List<CombineElement> combineElementList = Lists.newArrayList();
        for (int i = 0; i < candiateA.getCandidateList().size(); i++) {
            for (int j = 0; j < candiateB.getCandidateList().size(); j++) {
                CombineElement combineElement = new CombineElement(orderelement_size);
                combineElement.getElementIndexList()[candiateA.getElementIndex()] = i;
                combineElement.getElementIndexList()[candiateB.getElementIndex()] = j;
                combineElement.setIndexSum(i + j);
                combineElementList.add(combineElement);
            }
        }
        return combineElementList;
    }

    public List<CombineElement> combineCombineElement(List<CombineElement> aList, List<CombineElement> bList) {
        List<CombineElement> combineElementList = Lists.newArrayList();
        for (CombineElement combineA : aList) {
            for (CombineElement combineB : bList) {
                CombineElement combineElement = new CombineElement(combineA.getElementIndexList().length);
                for (int i = 0; i < combineElement.getElementIndexList().length; i++) {
                    combineElement.getElementIndexList()[i] = combineA.getElementIndexList()[i] + combineB.getElementIndexList()[i];
                    combineElement.setIndexSum(combineA.getIndexSum() + combineB.getIndexSum());
                }
                combineElementList.add(combineElement);
            }
        }
        return combineElementList;
    }


}
