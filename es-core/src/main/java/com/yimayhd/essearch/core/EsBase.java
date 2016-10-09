package com.yimayhd.essearch.core;

import com.alibaba.fastjson.JSONObject;
import com.yimayhd.essearch.vo.SearchResult;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * EsBase
 *
 * @author lilin
 * @date 16/9/27
 */
public abstract class EsBase {

    @Autowired
    private EsClient client;

    public boolean insert(String _index, String _type, String _id, String jsonStr) {
        IndexResponse response = getIndexRequestBuilder(_index, _type, _id, jsonStr).get();
        return response.isCreated();
    }

    public IndexRequestBuilder getIndexRequestBuilder(String _index, String _type, String _id, String jsonStr) {
        return client.getClient().prepareIndex(_index, _type, _id)
                .setSource(jsonStr);
    }

    public <T> T get(String _index, String _type, String _id, Class<T> t) {
        GetResponse response = client.getClient().prepareGet(_index, _type, _id).
                setOperationThreaded(false)
                .get();
        if (response.isExists()) {
            return JSONObject.parseObject(response.getSourceAsString(), t);
        } else {
            return null;
        }
    }

    public boolean delete(String _index, String _type, String _id) {
        DeleteResponse response = client.getClient().prepareDelete(_index, _type, _id)
                .get();
        return response.isFound();
    }

    public boolean upsert(String _index, String _type, String _id, String jsonStr) {
        IndexRequest indexRequest = new IndexRequest(_index, _type, _id)
                .source(jsonStr);
        UpdateRequest updateRequest = new UpdateRequest(_index, _type, _id)
                .doc(jsonStr)
                .upsert(indexRequest);
        UpdateResponse updateResponse;
        try {
            updateResponse = client.getClient().update(updateRequest).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        }
        return updateResponse.isCreated();
    }

    public boolean bulkInsert(List<IndexRequestBuilder> indexRequestBuilders, ActionFuture<UpdateResponse>... updateResponseActionFuture) {
        BulkRequestBuilder bulkRequest = client.getClient().prepareBulk();
        for (IndexRequestBuilder indexRequestBuilder : indexRequestBuilders) {
            bulkRequest.add(indexRequestBuilder);
        }
        BulkResponse bulkResponse = bulkRequest.get();
        return !bulkResponse.hasFailures();
    }

    /**
     * 设置typ mapping 的前提条件是index 存在
     * @param _index
     * @param _type
     * @param mapping
     * @return
     */
    public boolean setIndexTypeMapping(String _index, String _type, String mapping) {

        boolean existIndex = client.getClient().admin().indices().prepareExists(_index).get().isExists();
        if(!existIndex){
            boolean createSuc = client.getClient().admin().indices().prepareCreate(_index).get().isAcknowledged();
            if(!createSuc){
                return false;
            }
        }
        return client.getClient().admin().indices().preparePutMapping(_index).setType(_type).setSource(mapping).get().isAcknowledged();
    }

    public <T> SearchResult<T> search(String _index, String _type, Class<T> t, int from, int size, QueryBuilder queryBuilders, SortBuilder... sortBuilders) {

        SearchRequestBuilder searchRequestBuilder = client.getClient().prepareSearch()
                .setIndices(_index)
                .setTypes(_type)
                .setFrom(from)
                .setSize(size)
                .setQuery(queryBuilders);
        for (SortBuilder sortBuilder : sortBuilders) {
            searchRequestBuilder.addSort(sortBuilder);
        }

        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        List<T> tList = new ArrayList<T>(searchResponse.getHits().getHits().length);
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            tList.add(JSONObject.parseObject(hit.getSourceAsString(), t));
        }

        SearchResult<T> searchResult = new SearchResult<T>();
        searchResult.setResults(tList);
        return searchResult;
    }

    public <T> T searchById(String _index, String _type, String _id, Class<T> t) {
        GetResponse response = client.getClient().prepareGet(_index, _type, _id)
                .setOperationThreaded(false)
                .get();
        if (response.isExists()) {
            return JSONObject.parseObject(response.getSourceAsString(), t);
        } else {
            return null;
        }
    }

}
