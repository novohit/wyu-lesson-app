package com.wyu.data;
public class Course implements Cloneable{
    public String banji;
    public String renshu;
    public String bianhao;
    public String kecheng;
    public String jiaoshi;
    public String yuanxi;
    public String shijian;
    public String didian;
    public String zhouci;
    public String danshuang;
    public String fenzu;


    public String getBanji() {
        return banji;
    }

    public void setBanji(String banji) {
        this.banji = banji;
    }

    public String getRenshu() {
        return renshu;
    }

    public void setRenshu(String renshu) {
        this.renshu = renshu;
    }

    public String getBianhao() {
        return bianhao;
    }

    public void setBianhao(String bianhao) {
        this.bianhao = bianhao;
    }

    public String getKecheng() {
        return kecheng;
    }

    public void setKecheng(String kecheng) {
        this.kecheng = kecheng;
    }

    public String getJiaoshi() {
        return jiaoshi;
    }

    public void setJiaoshi(String jiaoshi) {
        this.jiaoshi = jiaoshi;
    }

    public String getYuanxi() {
        return yuanxi;
    }

    public void setYuanxi(String yuanxi) {
        this.yuanxi = yuanxi;
    }

    public String getShijian() {
        return shijian;
    }

    public void setShijian(String shijian) {
        this.shijian = shijian;
    }

    public String getDidian() {
        return didian;
    }

    public void setDidian(String didian) {
        this.didian = didian;
    }

    public String getZhouci() {
        return zhouci;
    }

    public void setZhouci(String zhouci) {
        this.zhouci = zhouci;
    }

    public String getDanshuang() {
        return danshuang;
    }

    public void setDanshuang(String danshuang) {
        this.danshuang = danshuang;
    }

    public String getFenzu() {
        return fenzu;
    }

    public void setFenzu(String fenzu) {
        this.fenzu = fenzu;
    }

    @Override
    public String toString() {
        return "Course[banji="+banji
                +",renshu="+renshu
                +",bianhao="+bianhao
                +",kecheng="+kecheng
                +",jiaoshi="+jiaoshi
                +",yuanxi="+yuanxi
                +",shijian="+shijian
                +",didian="+didian
                +",zhouci="+zhouci
                +",danshuang="+danshuang
                +",fenzu="+fenzu
                +"]";
    }
    public Object clone() {
        Course obj = null;
        try {
            obj = (Course) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
