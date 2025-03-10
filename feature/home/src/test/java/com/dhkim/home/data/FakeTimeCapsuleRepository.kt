package com.dhkim.home.data

import com.dhkim.story.data.dataSource.local.TimeCapsuleLocalDataSource
import com.dhkim.story.data.dataSource.remote.Uuid
import com.dhkim.story.data.dataSource.toMyTimeCapsule
import com.dhkim.story.data.dataSource.toReceivedTimeCapsule
import com.dhkim.story.domain.model.MyTimeCapsule
import com.dhkim.story.domain.model.ReceivedTimeCapsule
import com.dhkim.story.domain.model.SendTimeCapsule
import com.dhkim.story.domain.repository.TimeCapsuleRepository
import com.dhkim.story.domain.repository.isSuccessful
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FakeTimeCapsuleRepository (
    private val localDataSource: TimeCapsuleLocalDataSource = FakeTimeCapsuleLocalDataSource()
) : TimeCapsuleRepository {

    override fun shareTimeCapsule(
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
    ): Flow<isSuccessful> {
        return flowOf(true)
    }

    override suspend fun deleteTimeCapsule(myId: String, sharedFriends: List<Uuid>, timeCapsuleId: String): isSuccessful {
        return true
    }

    override fun getMyAllTimeCapsule(): Flow<List<MyTimeCapsule>> {
        return localDataSource.getMyAllTimeCapsule().map { timeCapsules ->
            timeCapsules?.map {
                it.toMyTimeCapsule()
            } ?: listOf()
        }
    }

    override fun getMyTimeCapsule(id: String): MyTimeCapsule? {
        return localDataSource.getMyTimeCapsule(id)?.toMyTimeCapsule()
    }

    override suspend fun getMyTimeCapsulesInDate(startDate: String, endDate: String): Flow<List<MyTimeCapsule>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveMyTimeCapsule(timeCapsule: MyTimeCapsule) {
        val entity = timeCapsule.run {
            com.dhkim.database.entity.MyTimeCapsuleEntity(
                id, date, openDate, lat, lng, placeName, address, content, images, videos, checkLocation, isOpened, sharedFriends
            )
        }

        localDataSource.saveMyTimeCapsule(timeCapsule = entity)
    }

    override suspend fun editMyTimeCapsule(timeCapsule: MyTimeCapsule) {
        val entity = timeCapsule.run {
            com.dhkim.database.entity.MyTimeCapsuleEntity(
                id, date, openDate, lat, lng, placeName, address, content, images, videos, checkLocation, isOpened, sharedFriends
            )
        }

        localDataSource.updateMyTimeCapsule(entity)
    }

    override suspend fun deleteMyTimeCapsule(id: String) {
        localDataSource.deleteMyTimeCapsule(id)
    }

    override suspend fun getReceivedTimeCapsule(id: String): ReceivedTimeCapsule? {
        return localDataSource.getReceivedTimeCapsule(id)?.toReceivedTimeCapsule()
    }

    override suspend fun getReceivedTimeCapsulesInDate(startDate: String, endDate: String): Flow<List<ReceivedTimeCapsule>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveReceivedTimeCapsule(timeCapsule: ReceivedTimeCapsule) {
        val entity = timeCapsule.run {
            com.dhkim.database.entity.ReceivedTimeCapsuleEntity(
                id, date, openDate, sender, profileImage, lat, lng, placeName, address, content, checkLocation, isOpened
            )
        }

        localDataSource.saveReceivedTimeCapsule(timeCapsule = entity)
    }

    override suspend fun updateReceivedTimeCapsule(timeCapsule: ReceivedTimeCapsule) {
        val entity = timeCapsule.run {
            com.dhkim.database.entity.ReceivedTimeCapsuleEntity(
                id, date, openDate, sender, profileImage, lat, lng, placeName, address, content, checkLocation, isOpened
            )
        }

        localDataSource.updateReceivedTimeCapsule(entity)
    }

    override suspend fun deleteReceivedTimeCapsule(id: String) {
        localDataSource.deleteReceivedTimeCapsule(id)
    }


    override suspend fun getSendAllTimeCapsule(): Flow<List<SendTimeCapsule>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSendTimeCapsule(id: String): SendTimeCapsule? {
        TODO("Not yet implemented")
    }

    override suspend fun getSendTimeCapsulesInDate(startDate: String, endDate: String): Flow<List<SendTimeCapsule>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveSendTimeCapsule(timeCapsule: SendTimeCapsule) {
        TODO("Not yet implemented")
    }

    override suspend fun editSendTimeCapsule(timeCapsule: SendTimeCapsule) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSendTimeCapsule(id: String) {
        TODO("Not yet implemented")
    }

    override fun getReceivedAllTimeCapsule(): Flow<List<ReceivedTimeCapsule>> {
        return localDataSource.getReceivedAllTimeCapsule().map { timeCapsules ->
            timeCapsules?.map {
                it.toReceivedTimeCapsule()
            } ?: listOf()
        }
    }
}