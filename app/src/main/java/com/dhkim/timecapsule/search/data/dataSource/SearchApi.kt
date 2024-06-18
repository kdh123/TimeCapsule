package com.dhkim.timecapsule.search.data.dataSource

import com.dhkim.timecapsule.BuildConfig
import com.dhkim.timecapsule.search.data.model.PlaceDto
import com.dhkim.timecapsule.search.data.model.AddressDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchApi {

    @GET("search/keyword")
    suspend fun getNearPlaceByKeyword(
        @Header("Authorization") token: String = BuildConfig.KAKAO_API_KEY,
        @Query("query") query: String,
        @Query("y") lat: String,
        @Query("x") lng: String,
        @Query("radius") range:Int = 20000,
        @Query("page") page: Int,
        @Query("size") size: Int = 15
    ): Response<PlaceDto>

    @GET("search/keyword")
    suspend fun getPlaceByKeyword(
        @Header("Authorization") token: String = BuildConfig.KAKAO_API_KEY,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int = 15
    ): Response<PlaceDto>

    @GET("search/category")
    suspend fun getPlaceByCategory(
        @Header("Authorization") token: String = BuildConfig.KAKAO_API_KEY,
        @Query("category_group_code") category: String,
        @Query("y") lat: String,
        @Query("x") lng: String,
        @Query("radius") range:Int = 20000,
        @Query("page") page: Int,
        @Query("size") size: Int = 15
    ): Response<PlaceDto>

    @GET("geo/coord2address.json")
    suspend fun getAddress2(
        @Header("Authorization") token: String = BuildConfig.KAKAO_API_KEY,
        @Query("y") lat: String,
        @Query("x") lng: String
    ): Response<AddressDto>
}