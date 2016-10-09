package com.yimayhd.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.yimayhd.api.service.CarService;
import com.yimayhd.api.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * CarController
 *
 * @author lilin
 * @date 16/9/29
 */
@RequestMapping("chart")
@Controller
public class CarController {

    @Autowired
    private CarService carService;

    @RequestMapping("msg")
    @ResponseBody
    public String searchNearbyCars(){
        return JSONObject.toJSONString(carService.searchByGeoDistance(39.828952, 116.390549, 10d, new PageVo(20, 1)));
    }
}
