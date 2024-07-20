package com.dhkim.location

import androidx.paging.testing.asSnapshot
import com.dhkim.location.data.dataSource.remote.LocationApi
import com.dhkim.location.data.dataSource.remote.LocationRemoteDataSource
import com.dhkim.location.data.dataSource.remote.LocationRemoteDataSourceImpl
import com.dhkim.location.data.di.LocationApiModule
import com.dhkim.location.data.di.LocationModule
import com.dhkim.location.data.model.PlaceDocument
import com.dhkim.location.data.repository.LocationRepositoryImpl
import com.dhkim.location.domain.LocationRepository
import com.dhkim.location.presentation.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
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
@UninstallModules(LocationModule::class, LocationApiModule::class)
class SearchViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var locationRepository: LocationRepository

    private lateinit var viewModel: SearchViewModel

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = SearchViewModel(locationRepository)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class FakeLocationModule {

        @Binds
        @Singleton
        abstract fun bindLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository

        @Binds
        @Singleton
        abstract fun bindLocationRemoteDataSource(locationRemoteDataSource: LocationRemoteDataSourceImpl): LocationRemoteDataSource

        @Binds
        @Singleton
        abstract fun bindFakeLocationApi(fakeLocationApi: FakeLocationApi): LocationApi
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `UI 상태 테스트`() = runTest {
        viewModel.onQuery("롯데타워")
        advanceTimeBy(1500L)

        val uiState = viewModel.uiState.value
        val places = uiState.places.asSnapshot()
        val documents = mutableListOf<PlaceDocument>().apply {
            repeat(15) {
                add(
                    PlaceDocument(
                        address_name = "서울시 강남구$it",
                        category_group_code = "code$it",
                        category_group_name = "group$it",
                        category_name = "categoryName$it",
                        distance = "$it",
                        id = "placeId$it",
                        phone = "010-1234-1234",
                        place_name = "장소$it",
                        place_url = "url$it",
                        road_address_name = "강남로$it",
                        x = "34.3455",
                        y = "123.4233"
                    )
                )
            }
        }.map {
            it.toPlace()
        }

        val isContain = documents.map {
            places.contains(it)
        }.firstOrNull { !it }

        assertEquals(isContain, null)
    }
}