package com.jiajie.design.api;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * GankServiceApi
 * Created by jiajie on 16/7/7.
 */
public interface GankServiceApi {

    /**
     * search: http://gank.io/api/search/query/listview/category/Android/count/10/page/1
     * behind category
     * type: all/Android/iOS/休息视频/福利/拓展资源/前端/瞎推荐/App
     * count <= 50
     */
    @GET("search/query/listview/category/{type}/count/{count}/page/{page}")
    Call<SearchResponse<SearchResult>> search(@Path("type") String type,
                                              @Path("count") int count,
                                              @Path("page") int page);

}
