package com.jiajie.design.api;

import java.util.List;

/**
 * GirlsResponse
 * Created by jiajie on 16/7/31.
 */
public class DataResponse<T> {

    private boolean error;
    private List<T> results;

    public DataResponse() {
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
        return "error:" + error + "\nresults:" + results;
    }

}
