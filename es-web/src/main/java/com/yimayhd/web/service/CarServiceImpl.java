package com.yimayhd.web.service;

import com.yimayhd.api.service.CarService;
import com.yimayhd.api.vo.CarVo;
import com.yimayhd.api.vo.PageVo;
import com.yimayhd.client.CarClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CarServiceImpl
 *
 * @author lilin
 * @date 16/9/29
 */
@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarClient carClient;

    public CarVo searchById(long id) {
        return null;
    }

    public List<CarVo> searchByGeoDistance(double lat, double lon, double distance, PageVo pageVo) {
        return carClient.geoSearch(lat,lon,distance,pageVo);
    }
}
