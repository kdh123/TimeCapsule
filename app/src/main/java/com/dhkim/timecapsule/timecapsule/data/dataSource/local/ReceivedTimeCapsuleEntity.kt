package com.dhkim.timecapsule.timecapsule.data.dataSource.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dhkim.timecapsule.timecapsule.domain.ReceivedTimeCapsule

@Entity(tableName = "receivedTimeCapsule")
data class ReceivedTimeCapsuleEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "openDate") val openDate: String,
    @ColumnInfo(name = "sender") val sender: String,
    @ColumnInfo(name = "lat") val lat: String,
    @ColumnInfo(name = "lng") val lng: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "checkLocation") val checkLocation: Boolean,
    @ColumnInfo(name = "isOpened") val isOpened: Boolean
) {
    fun toReceivedTimeCapsule(): ReceivedTimeCapsule {
        return ReceivedTimeCapsule(
            id, date, openDate, sender, lat, lng, address, content, checkLocation, isOpened
        )
    }
}