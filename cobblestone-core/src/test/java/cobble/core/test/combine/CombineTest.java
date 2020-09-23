package cobble.core.test.combine;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CombineTest {

    @Test
    public void testA() {
        // 2图1文
        pic2_txt1_1();

        // 1图2文
//        pic1_txt2_1();
    }

    @Test
    public void testB() {
        // 2图1文
        pic2_txt1_2();

        // 1图2文
//        pic1_txt2_2();
    }

    /**
     * 2图1文-方案1
     */
    public void pic2_txt1_1() {
        List<CombineBean> pic1List = getCandidateList(0, 3, 30, 1.0d);
        List<CombineBean> pic2List = getCandidateList(1, 3, 30, 1.0d);
        List<CombineBean> txt1List = getCandidateList(2, 3, 30, 2.0d);

        List<CombineBean> aList = combineCombine(pic1List, pic2List, 3);
        List<CombineBean> combineList = combineCombine(aList, txt1List, 3);
        Collections.shuffle(combineList);
        sort(combineList);
        combineList = combineList.subList(0, 100);
        print(combineList);
    }

    /**
     * 2图1文-方案2
     */
    public void pic2_txt1_2() {
        List<CombineBean> pic1List = getCandidateList(0, 3, 30, 1.0d);
        List<CombineBean> pic2List = getCandidateList(1, 3, 30, 1.0d);
        List<CombineBean> txt1List = getCandidateList(2, 3, 30, 2.0d);

        List<CombineBean> aList = combineCombine(pic1List, pic2List, 3);
        Collections.shuffle(aList);
        sort(aList);
        reset(aList, 1.0d);
//        List<CombineBean> picList = aList.subList(0, 30);


        List<CombineBean> combineList = combineCombine(aList, txt1List, 3);
        Collections.shuffle(combineList);
        sort(combineList);
        combineList = combineList.subList(0, 100);
        combineList.forEach(combineBean -> combineBean.setScoreSum(combineBean.getCandidateIndexArr()[0] + combineBean.getCandidateIndexArr()[1] + combineBean.getCandidateIndexArr()[2] * 2));
        print(combineList);
    }


    public void pic1_txt2_1() {
        List<CombineBean> pic1List = getCandidateList(0, 3, 30, 1.0d);
        List<CombineBean> txt1List = getCandidateList(1, 3, 30, 2.0d);
        List<CombineBean> txt2List = getCandidateList(2, 3, 30, 2.0d);

        List<CombineBean> aList = combineCombine(txt1List, txt2List, 3);
        List<CombineBean> combineList = combineCombine(pic1List, aList, 3);
        Collections.shuffle(combineList);
        sort(combineList);
        combineList = combineList.subList(0, 100);
        print(combineList);
    }

    public void pic1_txt2_2() {
        List<CombineBean> pic1List = getCandidateList(0, 3, 30, 1.0d);
        List<CombineBean> txt1List = getCandidateList(1, 3, 30, 2.0d);
        List<CombineBean> txt2List = getCandidateList(2, 3, 30, 2.0d);

        List<CombineBean> aList = combineCombine(txt1List, txt2List, 3);
        Collections.shuffle(aList);
        sort(aList);
        reset(aList, 2.0d);
//        List<CombineBean> txtList = aList.subList(0, 30);

        List<CombineBean> combineList = combineCombine(pic1List, aList, 3);
        Collections.shuffle(combineList);
        sort(combineList);
        combineList = combineList.subList(0, 100);
        combineList.forEach(combineBean -> combineBean.setScoreSum(combineBean.getCandidateIndexArr()[0] + combineBean.getCandidateIndexArr()[1] * 2 + combineBean.getCandidateIndexArr()[2] * 2));
        print(combineList);
    }

    private void sort(List<CombineBean> combineList) {
        Collections.sort(combineList, Comparator.comparingDouble(x -> x.getScoreSum()));
        for (int i = 0; i < combineList.size(); i++) {
            combineList.get(i).setIndex(i);
        }
    }

    private void reset(List<CombineBean> aList, double factor) {
        for (int i = 0; i < aList.size(); i++) {
            aList.get(i).setScoreSum((i + 1) * factor);
        }
    }


    private void print(List<CombineBean> combineList) {

        combineList.forEach(x -> System.out.println(x.getIndex() + ":\t" + x.getScoreSum() + "\t:\t" + JSON.toJSONString(x.getCandidateIndexArr())));
//        combineList.forEach(x -> System.out.println(x.getIndex() + ":\t" + JSON.toJSONString(x.getCandidateIndexArr())));

    }


    private List<CombineBean> getCandidateList(int elementIndex, int elementCount, int candidateSize, double factor) {
        List<CombineBean> candidateList = Lists.newArrayList();
        for (int i = 0; i < candidateSize; i++) {
            CombineBean combineBean = CombineBean.builder()
                    .scoreSum((i + 1) * factor)
                    .candidateIndexArr(new int[elementCount])
                    .build();
            combineBean.getCandidateIndexArr()[elementIndex] = i + 1;
            candidateList.add(combineBean);
        }
        return candidateList;
    }

    private List<CombineBean> combineCombine(List<CombineBean> aList, List<CombineBean> bList, int elementCount) {
        if (CollectionUtils.isEmpty(aList) && CollectionUtils.isEmpty(bList)) {
            throw new IllegalArgumentException("CombineCombine 参数异常");
        }
        List<CombineBean> combineList = Lists.newArrayList();
        for (CombineBean combineA : aList) {
            for (CombineBean combineB : bList) {
                CombineBean combineBean = CombineBean.builder()
                        .scoreSum(combineA.getScoreSum() + combineB.getScoreSum())
                        .candidateIndexArr(new int[elementCount])
                        .build();
                for (int i = 0; i < elementCount; i++) {
                    combineBean.getCandidateIndexArr()[i] = combineA.getCandidateIndexArr()[i] + combineB.getCandidateIndexArr()[i];
                }
                combineList.add(combineBean);
            }
        }

        return combineList;
    }
}
