package com.yimayhd.client;

import com.alibaba.fastjson.JSONObject;
import com.yimayhd.api.constants.EsBasicEnum;
import com.yimayhd.api.vo.CarVo;
import com.yimayhd.api.vo.PageVo;
import com.yimayhd.essearch.core.EsBase;
import com.yimayhd.essearch.vo.SearchResult;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.sort.SortBuilders.geoDistanceSort;

/**
 * Car
 *
 * @author lilin
 * @date 16/9/27
 */
@Component
public class CarClient extends EsBase {

    public boolean bulkInsertCar(List<CarVo> carVoList){
        List<IndexRequestBuilder> indexRequestBuilders = new ArrayList<IndexRequestBuilder>(carVoList.size());
        for (CarVo carVo : carVoList) {
            indexRequestBuilders.add(getIndexRequestBuilder(EsBasicEnum.CAR.getIndex(), EsBasicEnum.CAR.getType(), String.valueOf(carVo.getId()), JSONObject.toJSONString(carVo)));
        }

        return bulkInsert(indexRequestBuilders);
    }

    public  List<CarVo> geoSearch(double lat, double lon,double distance,PageVo pageVo) {

        QueryBuilder geoQueryBuilder = geoDistanceRangeQuery("locationPoint")
                .point(lat, lon)
                .from("0km")
                .to(distance +"km")
                .includeLower(true)
                .includeUpper(true)
                .optimizeBbox("memory")
                .geoDistance(GeoDistance.ARC);

        BoolQueryBuilder qb = boolQuery().must(geoQueryBuilder).must(matchQuery("online", "0"));

        SortBuilder sortBuilder = geoDistanceSort("locationPoint")
                .point(lat, lon)
                .order(SortOrder.ASC)
                .unit(DistanceUnit.KILOMETERS)
                .sortMode("min")
                .geoDistance(GeoDistance.ARC);


        SearchResult<CarVo> search = search(EsBasicEnum.CAR.getIndex(), EsBasicEnum.CAR.getType(), CarVo.class, pageVo.getPageSize() * (pageVo.getPageIndex()-1), pageVo.getPageSize() * pageVo.getPageIndex(), qb, sortBuilder);
        return search.getResults();

    }
}
