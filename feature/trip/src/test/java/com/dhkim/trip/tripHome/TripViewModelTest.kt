package com.dhkim.trip.tripHome

import com.dhkim.trip.FakeTripLocalDataSource
import com.dhkim.trip.data.dataSource.local.TripLocalDataSource
import com.dhkim.trip.data.dataSource.local.TripRepositoryImpl
import com.dhkim.trip.data.di.TripModule
import com.dhkim.trip.domain.repository.TripRepository
import com.dhkim.trip.domain.model.Trip
import com.dhkim.trip.domain.usecase.DeleteTripUseCase
import com.dhkim.trip.domain.usecase.GetAllTripsUseCase
import com.dhkim.trip.presentation.tripHome.TripAction
import com.dhkim.trip.presentation.tripHome.TripViewModel
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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

    @Inject
    lateinit var getAllTripsUseCase: GetAllTripsUseCase

    @Inject
    lateinit var deleteTripUseCase: DeleteTripUseCase

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = TripViewModel(
            getAllTripsUseCase = getAllTripsUseCase,
            deleteTripUseCase = deleteTripUseCase,
            ioDispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `uiState 테스트`() = runBlocking {
        viewModel.uiState.first()
        delay(100)

        val items = viewModel.uiState.value.trips?.filter {
            it.data !is String
        }

        assertEquals(items?.count { (it.data as Trip).isNextTrip }, 2)
        assertEquals(items?.count { !(it.data as Trip).isNextTrip }, 4)
    }

    @Test
    fun `여행 아이템 삭제 테스트`() = runBlocking {
        viewModel.uiState.first()
        viewModel.onAction(TripAction.DeleteTrip(tripId = "id0"))
        viewModel.uiState.restart()

        val items = viewModel.uiState.value.trips?.filter {
            it.data !is String
        }
        assertEquals(items?.count { (it.data as Trip).isNextTrip }, 1)
        assertEquals(items?.count { !(it.data as Trip).isNextTrip }, 4)
    }
}