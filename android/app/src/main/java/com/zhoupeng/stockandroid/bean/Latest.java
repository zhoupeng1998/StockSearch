package com.zhoupeng.stockandroid.bean;

public class Latest {
    private Double c;
    private Double d;
    private Double dp;
    private Double h;
    private Double l;
    private Double o;
    private Double pc;
    private Long t;

    public Latest() {
    }

    public Latest(Double c, Double d, Double dp, Double h, Double l, Double o, Double pc, Long t) {
        this.c = c;
        this.d = d;
        this.dp = dp;
        this.h = h;
        this.l = l;
        this.o = o;
        this.pc = pc;
        this.t = t;
    }

    public Double getC() {
        return c;
    }

    public void setC(Double c) {
        this.c = c;
    }

    public Double getD() {
        return d;
    }

    public void setD(Double d) {
        this.d = d;
    }

    public Double getDp() {
        return dp;
    }

    public void setDp(Double dp) {
        this.dp = dp;
    }

    public Double getH() {
        return h;
    }

    public void setH(Double h) {
        this.h = h;
    }

    public Double getL() {
        return l;
    }

    public void setL(Double l) {
        this.l = l;
    }

    public Double getO() {
        return o;
    }

    public void setO(Double o) {
        this.o = o;
    }

    public Double getPc() {
        return pc;
    }

    public void setPc(Double pc) {
        this.pc = pc;
    }

    public Long getT() {
        return t;
    }

    public void setT(Long t) {
        this.t = t * 1000;
    }

    @Override
    public String toString() {
        return "Latest{" +
                "c=" + c +
                ", d=" + d +
                ", dp=" + dp +
                ", h=" + h +
                ", l=" + l +
                ", o=" + o +
                ", pc=" + pc +
                ", t=" + t +
                '}';
    }
}