package com.dhkim.location.data.dataSource.remote

import com.dhkim.core.location.BuildConfig
import com.dhkim.location.data.model.AddressDto
import com.dhkim.location.data.model.PlaceDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

internal interface LocationApi {

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
    suspend fun getAddress(
        @Header("Authorization") token: String = BuildConfig.KAKAO_API_KEY,
        @Query("y") lat: String,
        @Query("x") lng: String
    ): Response<AddressDto>
}