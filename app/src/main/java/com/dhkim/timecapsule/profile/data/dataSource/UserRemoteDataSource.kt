package com.dhkim.timecapsule.profile.data.dataSource

import com.dhkim.timecapsule.profile.data.di.FirebaseModule
import com.dhkim.timecapsule.profile.domain.User
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    @FirebaseModule.FirebaseDatabase private val database: DatabaseReference
) {

    fun updateUser(user: User) {
        database.child("users").child(user.id).setValue(user)
    }
}