package com.wyu.model;


import java.util.List;

/**
 * @author zwx
 * @date 2022-11-24 15:24
 */
public class ScoreVO {
    private List<Score> scoreList;

    private Integer scoreCount;

    public ScoreVO() {
    }

    public ScoreVO(List<Score> scoreList, Integer scoreCount) {
        this.scoreList = scoreList;
        this.scoreCount = scoreCount;
    }

    public List<Score> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Score> scoreList) {
        this.scoreList = scoreList;
    }

    public Integer getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(Integer scoreCount) {
        this.scoreCount = scoreCount;
    }
}
