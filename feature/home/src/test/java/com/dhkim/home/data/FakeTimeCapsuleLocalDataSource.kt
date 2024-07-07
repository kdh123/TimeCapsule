package com.dhkim.home.data

import com.dhkim.database.entity.MyTimeCapsuleEntity
import com.dhkim.database.entity.ReceivedTimeCapsuleEntity
import com.dhkim.database.entity.SendTimeCapsuleEntity
import com.dhkim.home.data.dataSource.local.TimeCapsuleLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeTimeCapsuleLocalDataSource @Inject constructor() : TimeCapsuleLocalDataSource {

    private var myTimeCapsules: MutableList<MyTimeCapsuleEntity> = mutableListOf<MyTimeCapsuleEntity>().apply {
        repeat(10) {
            val openDate = if (it % 2 == 0) {
                "2024-06-07"
            } else {
                "2024-09-12"
            }

            val isOpened = it % 4 == 0

            val myTimeCapsuleEntity = MyTimeCapsuleEntity(
                id = "id$it",
                date = "2024-07-07",
                openDate = openDate,
                lat = "0.0",
                lng = "0.0",
                placeName = "광화문$it",
                address = "서울시 어딘가$it",
                content = "안녕하세요$it",
                images = listOf(),
                videos = listOf(),
                checkLocation = false,
                isOpened = isOpened,
                sharedFriends = listOf()
            )
            add(myTimeCapsuleEntity)
        }
    }

    private var receivedTimeCapsules: MutableList<ReceivedTimeCapsuleEntity> = mutableListOf<ReceivedTimeCapsuleEntity>().apply {
        repeat(10) {
            val receivedTimeCapsuleEntity = ReceivedTimeCapsuleEntity(
                id = "receivedId$it",
                date = "2024-07-07",
                openDate = "2024-09-18",
                lat = "0.0",
                lng = "0.0",
                placeName = "광화문$it",
                address = "서울시 어딘가$it",
                content = "안녕하세요$it",
                checkLocation = false,
                isOpened = false,
                sender = "이름$it",
                profileImage = "0"
            )
            add(receivedTimeCapsuleEntity)
        }
    }

    override fun getMyAllTimeCapsule(): Flow<List<MyTimeCapsuleEntity>?> {
        return flowOf(myTimeCapsules)
    }

    override fun getMyTimeCapsule(id: String): MyTimeCapsuleEntity? {
        return myTimeCapsules.firstOrNull { it.id == id }
    }

    override fun getMyTimeCapsulesInDate(startDate: String, endDate: String): Flow<List<MyTimeCapsuleEntity>?> {
        TODO("Not yet implemented")
    }

    override fun saveMyTimeCapsule(timeCapsule: MyTimeCapsuleEntity) {
        myTimeCapsules.add(timeCapsule)
    }

    override fun updateMyTimeCapsule(timeCapsule: MyTimeCapsuleEntity) {
        val index = myTimeCapsules.indexOfFirst { it.id == timeCapsule.id }
        myTimeCapsules[index] = timeCapsule
    }

    override fun deleteMyTimeCapsule(id: String) {
        myTimeCapsules.removeIf { it.id == id }
    }

    override fun getReceivedAllTimeCapsule(): Flow<List<ReceivedTimeCapsuleEntity>?> {
        return flowOf(receivedTimeCapsules)
    }

    override fun getReceivedTimeCapsule(id: String): ReceivedTimeCapsuleEntity? {
        return receivedTimeCapsules.firstOrNull { it.id == id }
    }

    override fun getReceivedTimeCapsulesInDate(startDate: String, endDate: String): Flow<List<ReceivedTimeCapsuleEntity>?> {
        TODO("Not yet implemented")
    }

    override fun saveReceivedTimeCapsule(timeCapsule: ReceivedTimeCapsuleEntity) {
        receivedTimeCapsules.add(timeCapsule)
    }

    override fun updateReceivedTimeCapsule(timeCapsule: ReceivedTimeCapsuleEntity) {
        val index = receivedTimeCapsules.indexOfFirst { it.id == timeCapsule.id }
        receivedTimeCapsules[index] = timeCapsule
    }

    override fun deleteReceivedTimeCapsule(id: String) {
        receivedTimeCapsules.removeIf { it.id == id }
    }

    override fun getSendAllTimeCapsule(): Flow<List<SendTimeCapsuleEntity>?> {
        TODO("Not yet implemented")
    }

    override fun getSendTimeCapsule(id: String): SendTimeCapsuleEntity? {
        TODO("Not yet implemented")
    }

    override fun getSendTimeCapsulesInDate(startDate: String, endDate: String): Flow<List<SendTimeCapsuleEntity>?> {
        TODO("Not yet implemented")
    }

    override fun saveSendTimeCapsule(timeCapsule: SendTimeCapsuleEntity) {
        TODO("Not yet implemented")
    }

    override fun updateSendTimeCapsule(timeCapsule: SendTimeCapsuleEntity) {
        TODO("Not yet implemented")
    }

    override fun deleteSendTimeCapsule(id: String) {
        TODO("Not yet implemented")
    }
}