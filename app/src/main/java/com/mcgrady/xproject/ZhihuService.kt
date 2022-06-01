/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcgrady.xproject

import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * Created by mcgrady on 2021/8/9.
 */
interface ZhihuService {

    /**
     * 最新日报
     */
    @Headers(Api.ZHIHU_DOMAIN + Api.ZHIHU_DOMAIN_NAME)
    @GET("/api/4/news/latest")
    suspend fun getDailyList()

//    @Headers(DOMAIN_NAME_HEADER + Api.ZHIHU_DOMAIN_NAME)
//    @retrofit2.http.GET("/api/4/news/latest")
//    fun getDailyList2(): Observable<BaseResponse<ZhihuDailyStoriesBean?>?>?
//
//    /**
//     * 往期日报
//     */
//    @Headers(DOMAIN_NAME_HEADER + Api.ZHIHU_DOMAIN_NAME)
//    @retrofit2.http.GET("/api/4/news/before/{date}")
//    fun getBeforeDailyList(@Path("date") date: String?): Observable<ZhihuDailyStoriesBean?>?
//
//    /**
//     * 日报详情
//     */
//    @Headers(DOMAIN_NAME_HEADER + Api.ZHIHU_DOMAIN_NAME)
//    @retrofit2.http.GET("/api/4/news/{id}")
//    fun getDailyDetail(@Path("id") id: Int): Observable<ZhihuDailyDetailBean?>?
}
