package com.yimayhd.api.vo;

/**
 * LocationPoint
 *
 * @author lilin
 * @date 16/9/28
 */
public class LocationPoint {
    private double lat;
    private double lon;

    public LocationPoint() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public LocationPoint(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
