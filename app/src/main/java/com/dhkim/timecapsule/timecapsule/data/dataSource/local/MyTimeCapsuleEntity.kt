package com.dhkim.timecapsule.timecapsule.data.dataSource.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dhkim.timecapsule.timecapsule.domain.MyTimeCapsule

@Entity(tableName = "myTimeCapsule")
data class MyTimeCapsuleEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "openDate") val openDate: String,
    @ColumnInfo(name = "lat") val lat: String,
    @ColumnInfo(name = "lng") val lng: String,
    @ColumnInfo(name = "placeName") val placeName: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "media") val medias: List<String>,
    @ColumnInfo(name = "checkLocation") val checkLocation: Boolean,
    @ColumnInfo(name = "isOpened") val isOpened: Boolean,
    @ColumnInfo(name = "sharedFriends") val sharedFriends: List<String>
) {
    fun toMyTimeCapsule(): MyTimeCapsule {
        return MyTimeCapsule(
            id, date, openDate, lat, lng, placeName, address, content, medias, checkLocation, isOpened, sharedFriends
        )
    }
}
