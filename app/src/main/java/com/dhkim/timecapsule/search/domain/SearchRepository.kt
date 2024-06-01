package com.dhkim.timecapsule.search.domain

import androidx.paging.PagingData
import com.dhkim.timecapsule.home.domain.Category
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun getPlaceByKeyword(query: String, lat: String, lng: String): Flow<PagingData<Place>>
    suspend fun getPlaceByCategory(category: Category, lat: String, lng: String): Flow<PagingData<Place>>
}