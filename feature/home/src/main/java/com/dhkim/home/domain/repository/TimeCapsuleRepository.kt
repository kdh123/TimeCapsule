package com.dhkim.home.domain.repository

import com.dhkim.home.data.dataSource.remote.Uuid
import com.dhkim.home.domain.model.MyTimeCapsule
import com.dhkim.home.domain.model.ReceivedTimeCapsule
import com.dhkim.home.domain.model.SendTimeCapsule
import kotlinx.coroutines.flow.Flow

typealias isSuccessful = Boolean

interface TimeCapsuleRepository {

    suspend fun shareTimeCapsule(
        myId: String,
        myProfileImage: String,
        timeCapsuleId: String,
        sharedFriends: List<Uuid>,
        openDate: String,
        content: String,
        lat: String,
        lng: String,
        placeName: String,
        address: String,
        checkLocation: Boolean
    ): isSuccessful

    suspend fun deleteTimeCapsule(
        myId: String,
        sharedFriends: List<Uuid>,
        timeCapsuleId: String
    ): isSuccessful

    fun getMyAllTimeCapsule(): Flow<List<MyTimeCapsule>>
    fun getMyTimeCapsule(id: String): MyTimeCapsule?
    suspend fun getMyTimeCapsulesInDate(startDate: String, endDate: String): Flow<List<MyTimeCapsule>>
    suspend fun saveMyTimeCapsule(timeCapsule: MyTimeCapsule)
    suspend fun editMyTimeCapsule(timeCapsule: MyTimeCapsule)
    suspend fun deleteMyTimeCapsule(id: String)

    suspend fun getSendAllTimeCapsule(): Flow<List<SendTimeCapsule>>
    suspend fun getSendTimeCapsule(id: String): SendTimeCapsule?
    suspend fun getSendTimeCapsulesInDate(startDate: String, endDate: String): Flow<List<SendTimeCapsule>>
    suspend fun saveSendTimeCapsule(timeCapsule: SendTimeCapsule)
    suspend fun editSendTimeCapsule(timeCapsule: SendTimeCapsule)
    suspend fun deleteSendTimeCapsule(id: String)

    fun getReceivedAllTimeCapsule(): Flow<List<ReceivedTimeCapsule>>
    suspend fun getReceivedTimeCapsule(id: String): ReceivedTimeCapsule?
    suspend fun getReceivedTimeCapsulesInDate(startDate: String, endDate: String): Flow<List<ReceivedTimeCapsule>>
    suspend fun saveReceivedTimeCapsule(timeCapsule: ReceivedTimeCapsule)
    suspend fun updateReceivedTimeCapsule(timeCapsule: ReceivedTimeCapsule)
    suspend fun deleteReceivedTimeCapsule(id: String)
}