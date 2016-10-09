package com.yimayhd.api.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * CarVo
 *
 * @author lilin
 * @date 16/9/27
 */
public class CarVo implements Serializable{
    private long id;
    private String title;
    private Date registerDate;
    private LocationPoint locationPoint;
    private int online;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public LocationPoint getLocationPoint() {
        return locationPoint;
    }

    public void setLocationPoint(LocationPoint locationPoint) {

        this.locationPoint = locationPoint;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }
}
