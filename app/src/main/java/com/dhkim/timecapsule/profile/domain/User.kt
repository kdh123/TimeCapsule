package com.dhkim.timecapsule.profile.domain

typealias UserId = String

data class User(
    val id: String = "",
    val uuid: String = "",
    val profileImageUrl: String = "",
    val friends: List<Friend> = listOf(),
    val requests: List<Friend> = listOf()
)

data class Friend(
    val id: String = "",
    val uuid: String = "",
    val isPending: Boolean = true
)
