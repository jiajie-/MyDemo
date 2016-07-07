package com.jiajie.design.api;

import java.util.List;

/**
 * SearchResponse
 * Created by jiajie on 16/7/7.
 */
public class SearchResponse<T> {

    private int count;
    private boolean error;
    private List<T> results;

    public SearchResponse() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "count:" + count + "\nerror:" + error + "\nresults:" + results;
    }
}
