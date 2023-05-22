package com.wyu.data;

import java.io.Serializable;

public class DataConf implements Serializable {
    public String xh;
    public String xq;
    public String path;

    public DataConf(){}
    public DataConf(String xh,String xq,String path){
        this.xh = xh;
        this.xq = xq;
        this.path = path;
    }
    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getXq() {
        return xq;
    }

    public void setTerm(String xq) {
        this.xq = xq;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "DataConf[xh="+xh
                +",xq="+xq
                +",path="+path
                +"]";
    }
}
