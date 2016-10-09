package com.yimayhd.api.constants;

/**
 * EsBasicEnum
 *
 * @author lilin
 * @date 16/9/29
 */
public enum EsBasicEnum {
    CAR("jiuxiu","car","九休的车车");
    private String index;
    private String type;
    private String des;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    EsBasicEnum(String index, String type, String des) {
        this.index = index;
        this.type = type;
        this.des = des;
    }
}
