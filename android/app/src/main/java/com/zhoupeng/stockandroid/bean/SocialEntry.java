package com.zhoupeng.stockandroid.bean;

public class SocialEntry {
    private Integer mention;
    private Integer positiveMention;
    private Integer negativeMention;

    public SocialEntry() {
    }

    public SocialEntry(Integer mention, Integer positiveMention, Integer negativeMention) {
        this.mention = mention;
        this.positiveMention = positiveMention;
        this.negativeMention = negativeMention;
    }

    public Integer getMention() {
        return mention;
    }

    public void setMention(Integer mention) {
        this.mention = mention;
    }

    public Integer getPositiveMention() {
        return positiveMention;
    }

    public void setPositiveMention(Integer positiveMention) {
        this.positiveMention = positiveMention;
    }

    public Integer getNegativeMention() {
        return negativeMention;
    }

    public void setNegativeMention(Integer negativeMention) {
        this.negativeMention = negativeMention;
    }

    @Override
    public String toString() {
        return "SocialEntry{" +
                "mention=" + mention +
                ", positiveMention=" + positiveMention +
                ", negativeMention=" + negativeMention +
                '}';
    }
}