package com.yimayhd.api.service;

import com.yimayhd.api.vo.CarVo;
import com.yimayhd.api.vo.PageVo;

import java.util.List;

/**
 * CarService
 *
 * @author Li Cong
 * @date 16/9/29
 */
public interface CarService {

    /**
     * 按照id查找某一辆车的位置
     * @param id
     * @return
     */
    public CarVo searchById(long id);

    /**
     * 按照经纬度，查找一定范围内的车辆
     * @param lat
     * @param lon
     * @param distance
     * @return
     */
    public List<CarVo> searchByGeoDistance(double lat,double lon,double distance,PageVo pageVo);
}
