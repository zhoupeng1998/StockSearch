package com.zhoupeng.stockandroid.bean;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class Candle {
    String s;
    List<Double> c;
    List<Double> h;
    List<Double> l;
    List<Double> o;
    List<Double> t;
    List<Double> v;

    public Candle() {
    }

    public Candle(String s, List<Double> c, List<Double> h, List<Double> l, List<Double> o, List<Double> t, List<Double> v) {
        this.s = s;
        this.c = c;
        this.h = h;
        this.l = l;
        this.o = o;
        this.t = t;
        this.v = v;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public List<Double> getC() {
        return c;
    }

    /*
    public void setC(List<Double> c) {
        this.c = c;
    }

     */

    public List<Double> getH() {
        return h;
    }

    /*
    public void setH(List<Double> h) {
        this.h = h;
    }

     */

    public List<Double> getL() {
        return l;
    }

    /*
    public void setL(List<Double> l) {
        this.l = l;
    }

     */

    public List<Double> getO() {
        return o;
    }

    /*
    public void setO(List<Double> o) {
        this.o = o;
    }

     */

    public List<Double> getT() {
        return t;
    }

    /*
    public void setT(List<Double> t) {
        this.t = t;
    }

     */

    public List<Double> getV() {
        return v;
    }

    /*
    public void setV(List<Double> v) {
        this.v = v;
    }

     */

    public void setC(String c) {
        this.c = JSON.parseArray(c, Double.class);
    }

    public void setH(String h) {
        this.h = JSON.parseArray(h, Double.class);
    }

    public void setL(String l) {
        this.l = JSON.parseArray(l, Double.class);
    }

    public void setO(String o) {
        this.o = JSON.parseArray(o, Double.class);
    }

    public void setT(String t) {
        this.t = JSON.parseArray(t, Double.class);
    }

    public void setV(String v) {
        this.v = JSON.parseArray(v, Double.class);
    }

    @Override
    public String toString() {
        return "Candle{" +
                "s='" + s + '\'' +
                ", c=" + c +
                ", h=" + h +
                ", l=" + l +
                ", o=" + o +
                ", t=" + t +
                ", v=" + v +
                '}';
    }
}