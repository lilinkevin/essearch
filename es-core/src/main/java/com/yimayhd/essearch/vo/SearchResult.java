package com.yimayhd.essearch.vo;

import java.util.List;

/**
 * SearchResult
 *
 * @author lilin
 * @date 16/9/28
 */
public class SearchResult<T> {

    private List<T> results;

    private long requestTime;

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }
}
