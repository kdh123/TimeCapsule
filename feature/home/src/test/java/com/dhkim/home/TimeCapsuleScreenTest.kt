package com.dhkim.home

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToIndex
import com.dhkim.home.data.FakeTimeCapsuleLocalDataSource
import com.dhkim.home.data.FakeTimeCapsuleRepository
import com.dhkim.home.data.FakeUserLocalDataSource
import com.dhkim.home.data.FakeUserRemoteDataSource
import com.dhkim.home.data.dataSource.local.TimeCapsuleLocalDataSource
import com.dhkim.home.data.di.TimeCapsuleModule
import com.dhkim.home.domain.TimeCapsuleRepository
import com.dhkim.home.presentation.TimeCapsuleScreen
import com.dhkim.home.presentation.TimeCapsuleSideEffect
import com.dhkim.home.presentation.TimeCapsuleViewModel
import com.dhkim.user.data.UserRepositoryImpl
import com.dhkim.user.data.dataSource.UserLocalDataSource
import com.dhkim.user.data.dataSource.UserRemoteDataSource
import com.dhkim.user.data.di.UserModule
import com.dhkim.user.domain.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import javax.inject.Singleton

@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
@UninstallModules(TimeCapsuleModule::class, UserModule::class)
class TimeCapsuleScreenTest {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class FakeTimeCapsuleModule {

        @Binds
        @Singleton
        abstract fun bindTimeCapsuleRepository(fakeTimeCapsuleRepository: FakeTimeCapsuleRepository): TimeCapsuleRepository

        @Binds
        @Singleton
        abstract fun bindTimeCapsuleLocalDataSource(fakeTimeCapsuleLocalDataSource: FakeTimeCapsuleLocalDataSource): TimeCapsuleLocalDataSource
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class UserModule {

        @Binds
        @Singleton
        abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

        @Binds
        @Singleton
        abstract fun bindUserRemoteDataSource(fakeUserRemoteDataSource: FakeUserRemoteDataSource): UserRemoteDataSource

        @Binds
        @Singleton
        abstract fun bindUserLocalDataSource(fakeUserLocalDataSource: FakeUserLocalDataSource): UserLocalDataSource
    }

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val composeRule = createComposeRule()

    @Inject
    lateinit var timeCapsuleRepository: TimeCapsuleRepository

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var viewModel: TimeCapsuleViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = TimeCapsuleViewModel(timeCapsuleRepository, userRepository)
    }

    @Test
    fun `UI 테스트`() = runBlocking {
        // Start the app
        delay(300L)
        val uiState = viewModel.uiState.value

        composeRule.setContent {
            TimeCapsuleScreen(
                uiState = uiState,
                sideEffect = TimeCapsuleSideEffect.None,
                onDeleteTimeCapsule = { _, _ -> },
                onNavigateToAdd = {},
                onNavigateToOpen = { _, _ -> },
                onNavigateToDetail = { _, _ -> },
                onNavigateToNotification = {},
                onNavigateToSetting = {},
                onNavigateToProfile = {},
                onNavigateToMore = {},
            )
        }

        composeRule.waitUntilExists(
            hasTestTag("openableTimeCapsules"),
            300L
        )

        composeRule.waitUntilExists(
            hasTestTag("lockTimeCapsuleid2"),
            300L
        )

        composeRule.onNodeWithTag("timeCapsuleItems").performScrollToIndex(6)

        composeRule.waitUntilExists(
            hasTestTag("lockTimeCapsuleid1"),
            300L
        )

        //composeRule.onNodeWithTag("unopenedTimeCapsules").assertIsDisplayed()

        //composeTestRule.onNodeWithText("Continue").performClick()
        //composeTestRule.onNodeWithTag("title").assert(hasText("나의 이야기"))
        //composeTestRule.onNodeWithText("나의 이야기").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    fun ComposeContentTestRule.waitUntilExists(
        matcher: SemanticsMatcher,
        timeoutMillis: Long = 1000L
    ) = waitUntilNodeCount(matcher, 1, timeoutMillis)
}