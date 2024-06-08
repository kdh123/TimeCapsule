package com.dhkim.timecapsule.profile.data.dataSource

import com.dhkim.timecapsule.BuildConfig
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

typealias expiredDate = Int

interface UserApi {
    @FormUrlEncoded
    @POST("register")
    suspend fun registerPush(
        @Header("Authorization") token: String = BuildConfig.KAKAO_ADMIN_KEY,
        @Field("uuid") uuid: String,
        @Field("device_id") deviceId: String,
        @Field("push_type") pushType: String = "fcm",
        @Field("push_token") pushToken: String
    ): Response<expiredDate>
}