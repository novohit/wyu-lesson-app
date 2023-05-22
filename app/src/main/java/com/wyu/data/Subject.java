package com.wyu.data;
public class Subject implements Cloneable{
    public String mingcheng;
    public String chengji;
    public String biaozhi;
    public String kechengxz;
    public String leibie;
    public String xueshi;
    public String xuefen;
    public String kaoshixz;
    public String buchongxq;

    public String getMingcheng() {
        return mingcheng;
    }

    public void setMingcheng(String mingcheng) {
        this.mingcheng = mingcheng;
    }

    public String getChengji() {
        return chengji;
    }

    public void setChengji(String chengji) {
        this.chengji = chengji;
    }

    public String getBiaozhi() {
        return biaozhi;
    }

    public void setBiaozhi(String biaozhi) {
        this.biaozhi = biaozhi;
    }

    public String getKechengxz() {
        return kechengxz;
    }

    public void setKechengxz(String kechengxz) {
        this.kechengxz = kechengxz;
    }

    public String getLeibie() {
        return leibie;
    }

    public void setLeibie(String leibie) {
        this.leibie = leibie;
    }

    public String getXueshi() {
        return xueshi;
    }

    public void setXueshi(String xueshi) {
        this.xueshi = xueshi;
    }

    public String getXuefen() {
        return xuefen;
    }

    public void setXuefen(String xuefen) {
        this.xuefen = xuefen;
    }

    public String getKaoshixz() {
        return kaoshixz;
    }

    public void setKaoshixz(String kaoshixz) {
        this.kaoshixz = kaoshixz;
    }

    public String getBuchongxq() {
        return buchongxq;
    }

    public void setBuchongxq(String buchongxq) {
        this.buchongxq = buchongxq;
    }
    public Object clone() {
        Subject obj = null;
        try {
            obj = (Subject) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public String toString() {
        return "Subject[mingcheng="+mingcheng
                +",chengji="+chengji
                +",biaozhi="+biaozhi
                +",kechengxz="+kechengxz
                +",leibie="+leibie
                +",xueshi="+xueshi
                +",xuefen="+xuefen
                +",kaoshixz="+kaoshixz
                +",buchongxq="+buchongxq
                +"]";
    }
}
