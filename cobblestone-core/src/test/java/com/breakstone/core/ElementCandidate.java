package com.breakstone.core;

import org.assertj.core.util.Lists;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ElementCandidate {

    private int elementIndex;

    private List<Integer> candidateList;

    public ElementCandidate(int elementIndex, int candiateset_size) {
        this.elementIndex = elementIndex;
        this.candidateList =IntStream.range(0,candiateset_size).boxed().collect(Collectors.toList());
    }

    public int getElementIndex() {
        return elementIndex;
    }

    public void setElementIndex(int elementIndex) {
        this.elementIndex = elementIndex;
    }

    public List<Integer> getCandidateList() {
        return candidateList;
    }

    public void setCandidateList(List<Integer> candidateList) {
        this.candidateList = candidateList;
    }
}
