package com.yimayhd.data;

import com.alibaba.fastjson.JSONObject;
import com.yimayhd.api.constants.EsBasicEnum;
import com.yimayhd.api.vo.CarVo;
import com.yimayhd.api.vo.LocationPoint;
import com.yimayhd.api.vo.PageVo;
import com.yimayhd.client.CarClient;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * CarTest
 *
 * @author lilin
 * @date 16/9/27
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext.xml",
})
public class CarTest {
    @Autowired
    private CarClient carClient;

    private final double maxLat = 40.017472;
    private final double minLat = 39.828952;

    private final double maxLon = 116.542984;
    private final double minLon = 116.216141;

    @Test
    public void carIndexTest() {
        CarVo carVo = new CarVo();
        carVo.setId(123l);
        carVo.setTitle("红色宝马");
        carClient.insert(EsBasicEnum.CAR.getIndex(), EsBasicEnum.CAR.getType(), String.valueOf(carVo.getId()), JSONObject.toJSONString(carVo));
    }

    @Test
    public void carGetTest() {
        CarVo c = carClient.get(EsBasicEnum.CAR.getIndex(), EsBasicEnum.CAR.getType(), "123", CarVo.class);
        System.out.println(JSONObject.toJSONString(c));
    }

    @Test
    public void upsertTest() {
        CarVo carVo = new CarVo();
        carVo.setId(124l);
        carVo.setTitle("GLA 200");
        carVo.setRegisterDate(new Date());

        carClient.upsert(EsBasicEnum.CAR.getIndex(), EsBasicEnum.CAR.getType(), String.valueOf(carVo.getId()), JSONObject.toJSONString(carVo));
    }

    @Test
    public void bulkInsertTest() {
        List<CarVo> carList = new ArrayList<CarVo>(10000);
        for (int i = 10000; i <= 100000; i++) {
            CarVo carVo = new CarVo();
            carVo.setId(i);
            carVo.setTitle("测试测量" + i);
            carVo.setRegisterDate(new Date());
            carVo.setOnline(i%2);
            double[] bigDecimals = randomLonLat(minLon, maxLon, minLat, maxLat);
            LocationPoint locationPoint = new LocationPoint(bigDecimals[0], bigDecimals[1]);
            carVo.setLocationPoint(locationPoint);
            carList.add(carVo);
        }
        boolean result = carClient.bulkInsertCar(carList);
        assertTrue(result);

    }


    @Test
    public void bulkTest() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        while (true) {
            List<CarVo> carList = new ArrayList<CarVo>(10000);
            for (int i = 1; i <= 20000; i++) {
                CarVo carVo = new CarVo();
                carVo.setId(i);
                carVo.setTitle("测试测量" + i);
                carVo.setRegisterDate(new Date());
                carVo.setOnline(1);
                double[] bigDecimals = randomLonLat(minLon, maxLon, minLat, maxLat);
                LocationPoint locationPoint = new LocationPoint(bigDecimals[0], bigDecimals[1]);
                carVo.setLocationPoint(locationPoint);
                carList.add(carVo);
            }
            stopWatch.reset();
            stopWatch.start();
            boolean result = carClient.bulkInsertCar(carList);
            assertTrue(result);
            System.out.println("消耗时间" + stopWatch.getTime() + "ms");


            try {
                Thread.sleep(100l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


    public static double[] randomLonLat(double minLon, double maxLon, double minLat, double maxLat) {
        BigDecimal db = new BigDecimal(Math.random() * (maxLon - minLon) + minLon);
        double lon = Double.parseDouble(db.setScale(6, BigDecimal.ROUND_HALF_UP).toString());//小数后6位
        db = new BigDecimal(Math.random() * (maxLat - minLat) + minLat);
        double lat = Double.parseDouble(db.setScale(6, BigDecimal.ROUND_HALF_UP).toString());
        return new double[]{lat, lon};
    }


    @Test
    public void geoSearchTest() {
        List<CarVo> carVos = carClient.geoSearch(39.828952, 116.390549,10d,new PageVo(20,1));

        System.out.println("结果为" + carVos.size());
        System.out.println(JSONObject.toJSONString(carVos));
    }


    @Test
    public void setIndexTypeMappingTest() {
        String mapping = "{\n" +
                "            \"car\":{\n" +
                "                \"properties\":{\n" +
                "                    \"id\":{\n" +
                "                        \"type\":\"long\"\n" +
                "                    },\n" +
                "                    \"locationPoint\":{\n" +
                "                        \"type\":\"geo_point\"\n" +
                "                    },\n" +
                "                    \"registerDate\":{\n" +
                "                        \"type\":\"long\"\n" +
                "                    },\n" +
                "                    \"title\":{\n" +
                "                        \"type\":\"string\"\n" +
                "                    },\n" +
                "                    \"online\":{\n" +
                "                        \"type\":\"integer\",\n" +
                "                        \"index\":\"not_analyzed\"\n" +
                "                    }\n" +
                "                }\n" +
                "            }}";
        boolean b = carClient.setIndexTypeMapping(EsBasicEnum.CAR.getIndex(), EsBasicEnum.CAR.getType(), mapping);
        assertTrue(b);
    }


    @Test
    public void getById(){
        CarVo carVo = carClient.searchById(EsBasicEnum.CAR.getIndex(), EsBasicEnum.CAR.getType(),"1", CarVo.class);
        assertNotNull(carVo);
    }
}
