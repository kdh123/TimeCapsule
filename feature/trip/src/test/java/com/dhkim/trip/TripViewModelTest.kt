package com.dhkim.trip

import com.dhkim.trip.data.dataSource.local.TripLocalDataSource
import com.dhkim.trip.data.dataSource.local.TripRepositoryImpl
import com.dhkim.trip.data.di.TripModule
import com.dhkim.trip.domain.TripRepository
import com.dhkim.trip.presentation.TripViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import junit.framework.TestCase.assertEquals
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
@UninstallModules(TripModule::class)
class TripViewModelTest {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class TripModule {

        @Binds
        @Singleton
        abstract fun bindTripRepository(tripRepositoryImpl: TripRepositoryImpl): TripRepository

        @Binds
        @Singleton
        abstract fun bindTripLocalDataSource(fakeTripLocalDataSourceImpl: FakeTripLocalDataSource): TripLocalDataSource
    }

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var viewModel: TripViewModel
    @Inject lateinit var tripRepository: TripRepository

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = TripViewModel(tripRepository = tripRepository)
    }

    @Test
    fun `uiState 테스트`() = runBlocking {
        delay(300)
        assertEquals(viewModel.uiState.value.nextTrips.size, 2)
        assertEquals(viewModel.uiState.value.prevTrips.size, 4)
    }

    @Test
    fun `여행 아이템 삭제 테스트`() = runBlocking {

    }

}