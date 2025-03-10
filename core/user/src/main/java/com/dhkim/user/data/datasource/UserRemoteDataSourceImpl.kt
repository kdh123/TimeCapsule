package com.dhkim.user.data.datasource

import android.util.Log
import com.dhkim.common.CommonResult
import com.dhkim.network.di.FirebaseModule
import com.dhkim.user.R
import com.dhkim.user.domain.model.Friend
import com.dhkim.user.domain.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.HttpException
import retrofit2.Retrofit
import java.util.UUID
import javax.inject.Inject

typealias isSuccessful = Boolean

internal class UserRemoteDataSourceImpl @Inject constructor(
    @com.dhkim.network.di.RetrofitModule.KakaoPush private val pushApi: Retrofit,
    @FirebaseModule.FirebaseDatabase private val database: DatabaseReference,
    @FirebaseModule.UserFirebaseDatabase private val userDatabase: DatabaseReference
) : UserRemoteDataSource {

    private val pushService = pushApi.create(UserApi::class.java)

    override fun getMyInfo(myId: String): Flow<User> {
        return callbackFlow {
            val userListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val data = dataSnapshot.value as? Map<*, *>
                    Log.e("data212", "data2 : $data")
                    val friends = (data?.get("friends") as? Map<*, *>)?.values
                    val requests = (data?.get("requests") as? Map<*, *>)?.values

                    val currentFriends = friends?.map {
                        val data = it as Map<*, *>

                        Friend(
                            id = data["id"] as? String ?: "",
                            nickname = data["nickname"] as? String ?: "",
                            profileImage = data["profileImage"] as? String ?: "${R.drawable.ic_smile_blue}",
                            uuid = data["uuid"] as? String ?: "",
                            isPending = data["pending"] as? Boolean ?: true,
                        )
                    } ?: listOf()

                    val currentRequests = requests?.map {
                        val data = it as Map<*, *>

                        Friend(
                            id = data["id"] as? String ?: "",
                            nickname = data["nickname"] as? String ?: "",
                            profileImage = data["profileImage"] as? String ?: "${R.drawable.ic_smile_blue}",
                            uuid = data["uuid"] as? String ?: "",
                            isPending = data["pending"] as? Boolean ?: true,
                        )
                    } ?: listOf()

                    val uuid = data?.get("uuid") as? String ?: ""
                    val profileImage = data?.get("profileImage") as? String ?: ""

                    val user = User(
                        id = if (data != null) myId else "",
                        uuid = uuid,
                        profileImage = profileImage,
                        friends = currentFriends,
                        requests = currentRequests
                    )

                    trySend(user)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    trySend(User())
                }
            }

            userDatabase.child(myId).addValueEventListener(userListener)
            awaitClose()
        }
    }

    override fun searchUser(userId: String): Flow<CommonResult<User?>> {
        return callbackFlow {
            database.child("users").child(userId).get().addOnSuccessListener { data ->
                val user = data.value as? Map<*, *>
                val profileImage = user?.get("profileImage") as? String ?: "0"

                data.value?.let {
                    trySend(CommonResult.Success(User(id = userId, profileImage = profileImage)))
                } ?: kotlin.run {
                    trySend(CommonResult.Success(null))
                }
            }.addOnFailureListener {
                trySend(CommonResult.Error(-1))
            }
            awaitClose()
        }
    }

    override fun updateUser(user: User): Flow<isSuccessful> {
        return callbackFlow {
            database.child("users").child(user.id).setValue(user).addOnSuccessListener {
                trySend(true)
            }.addOnFailureListener {
                trySend(false)
            }
            awaitClose()
        }
    }

    override fun acceptFriend(
        myId: String,
        myProfileImage: String,
        myUuid: String,
        userId: String,
        userProfileImage: String,
        userUuid: String
    ): Flow<isSuccessful> {
        return callbackFlow {
            val childUpdates = hashMapOf<String, Any?>(
                "/users/$myId/requests/$userId" to null,
                "/users/$myId/friends/$userId" to Friend(id = userId, profileImage = userProfileImage, uuid = userUuid, isPending = false),
                "/users/$userId/friends/$myId" to Friend(id = myId, profileImage = myProfileImage, uuid = myUuid, isPending = false)
            )

            database.updateChildren(childUpdates)
                .addOnSuccessListener {
                    trySend(true)
                }.addOnFailureListener {
                    trySend(false)
                }

            awaitClose()
        }
    }

    override fun addFriend(myId: String, myProfileImage: String, myUuid: String, userId: String, userProfileImage: String): Flow<isSuccessful> {
        return callbackFlow {
            val childUpdates = hashMapOf<String, Any>(
                "/users/$myId/friends/$userId" to Friend(id = userId, profileImage = userProfileImage, isPending = true),
                "/users/$userId/requests/$myId" to Friend(id = myId, profileImage = myProfileImage, uuid = myUuid, isPending = true),
            )

            database.updateChildren(childUpdates)
                .addOnSuccessListener {
                    trySend(true)
                }.addOnFailureListener {
                    trySend(false)
                }

            awaitClose()
        }
    }

    override fun updateFriend(myId: String, friend: Friend): Flow<isSuccessful> {
        return callbackFlow {
            val childUpdates = hashMapOf<String, Any>(
                "/users/$myId/friends/${friend.id}" to friend,
            )

            database.updateChildren(childUpdates)
                .addOnSuccessListener {
                    trySend(true)
                }.addOnFailureListener {
                    trySend(false)
                }

            awaitClose()
        }
    }

    override fun deleteFriend(myId: String, userId: String): Flow<isSuccessful> {
        return callbackFlow {
            val childUpdates = hashMapOf<String, Any?>(
                "/users/$myId/friends/$userId" to null,
                "/users/$userId/friends/$myId" to null,
                "/users/$userId/requests/$myId" to null,
            )

            database.updateChildren(childUpdates)
                .addOnSuccessListener {
                    trySend(true)
                }.addOnFailureListener {
                    trySend(false)
                }

            awaitClose()
        }
    }

    override fun addRequest(myId: String, myProfileImage: String, userId: String): Flow<isSuccessful> {
        return callbackFlow {
            database
                .child("users")
                .child(userId)
                .child("requests")
                .child(myId)
                .setValue(Friend(id = myId, profileImage = myProfileImage, isPending = true))
            awaitClose()
        }
    }

    override fun updateFcmToken(userId: String, uuid: String): Flow<isSuccessful> {
        return callbackFlow {
            database.child("users").child(userId).child("uuid").setValue(uuid)
                .addOnSuccessListener {
                    trySend(true)
                }
                .addOnFailureListener {
                    trySend(false)
                }
            awaitClose()
        }
    }

    override suspend fun registerPush(uuid: String, fcmToken: String): CommonResult<Int> {
        return try {
            val deviceId = UUID.randomUUID().toString()
            Log.e("register", "uuid: $uuid, deviceId: $deviceId, fcmToken: $fcmToken")
            pushService.registerPush(uuid = uuid, deviceId = deviceId, pushToken = fcmToken).run {
                if (isSuccessful) {
                    CommonResult.Success(body() ?: -1)
                } else {
                    CommonResult.Error(-1)
                }
            }
        } catch (e: HttpException) {
            CommonResult.Error(-1)
        } catch (e: Exception) {
            CommonResult.Error(-1)
        }
    }
}