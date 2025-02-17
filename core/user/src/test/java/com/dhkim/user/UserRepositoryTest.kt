package com.dhkim.user

import com.dhkim.user.repository.UserRepositoryImpl
import com.dhkim.user.datasource.UserLocalDataSource
import com.dhkim.user.datasource.UserRemoteDataSource
import com.dhkim.user.di.UserModule
import com.dhkim.user.model.Friend
import com.dhkim.user.model.User
import com.dhkim.user.repository.UserRepository
import com.dhkim.user.usecase.GetMyInfoUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import javax.inject.Singleton

@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
@UninstallModules(UserModule::class)
class UserRepositoryTest {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class FakeUserModule {

        @Binds
        @Singleton
        abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

        @Binds
        @Singleton
        abstract fun bindUserLocalDataSource(fakeUserLocalDataSource: FakeUserLocalDataSource): UserLocalDataSource

        @Binds
        @Singleton
        abstract fun bindUserRemoteDataSource(fakeUserRemoteDataSource: FakeUserRemoteDataSource): UserRemoteDataSource
    }

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userRepository: UserRepository
    
    @Inject
    lateinit var getMyInfoUseCase: GetMyInfoUseCase

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun `remote 내 정보 가져오기`() = runTest {
        val data = getMyInfoUseCase().first()
        val user = User(
            id = "id0",
            profileImage = "1230",
            uuid = "1236508",
            friends = listOf(
                Friend(
                    id = "id13",
                    profileImage = "158",
                    uuid = "15608465",
                    isPending = false
                ),
                Friend(
                    id = "id7",
                    profileImage = "84561",
                    uuid = "84561",
                    isPending = true
                ),
            ),
            requests = listOf()
        )
        println("user : $user")
        assertEquals(data, user)
    }

    @Test
    fun `친구 추가(요청)`() = runTest {
        val myInfo = getMyInfoUseCase().first()
        val prevFriendSize = myInfo.friends.size

        userRepository.addFriend(
            userId = "id10",
            userProfileImage = "84561"
        )

        val currentFriendSize = getMyInfoUseCase().first().friends.size

        assertEquals(prevFriendSize + 1, currentFriendSize)
    }

    @Test
    fun `친구 승낙`() = runTest {
        val myInfo = getMyInfoUseCase().first()
        val prevFriendSize = myInfo.friends.filter { it.isPending }.size

        userRepository.acceptFriend(
            userId = "id7",
            userProfileImage = "84561",
            userUuid = "uuid7"
        )

        val currentFriendSize = getMyInfoUseCase().first().friends.filter { it.isPending }.size
        assertEquals(prevFriendSize -1, currentFriendSize)
    }

    @Test
    fun `친구 삭제`() = runTest {
        val myInfo = getMyInfoUseCase().first()
        val prevFriendSize = myInfo.friends.size

        userRepository.deleteFriend("id7")

        val currentFriendSize = getMyInfoUseCase().first().friends.size
        assertEquals(prevFriendSize -1, currentFriendSize)
    }
}