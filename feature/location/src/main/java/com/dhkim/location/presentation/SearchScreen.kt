@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.dhkim.location.presentation

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.dhkim.designsystem.MyStoryTheme
import com.dhkim.location.R
import com.dhkim.location.domain.model.Place
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException

@Composable
fun SearchScreen(
    uiState: SearchUiState,
    searchResult: LazyPagingItems<Place>,
    onQuery: (String) -> Unit,
    onBack: (Place) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = query,
            onQuery = {
                query = it
                onQuery(it)
            })
        Box(modifier = Modifier.fillMaxSize()) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(10.dp)
                        .width(48.dp)
                        .align(Alignment.TopCenter),
                    color = Color.White,
                    trackColor = colorResource(id = R.color.primary),
                )
            }
            if (searchResult.itemCount > 0) {
                PlaceList(places = searchResult, onBack = onBack)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: String, onQuery: (String) -> Unit) {
    val focusRequester = remember { FocusRequester() }

    TextField(
        value = query,
        onValueChange = {
            onQuery(it)
        },
        colors = TextFieldDefaults.textFieldColors(
            disabledTextColor = colorResource(id = R.color.primary),
            disabledIndicatorColor = colorResource(id = R.color.primary),
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        label = {
            Text(
                text = "장소 검색",
                style = MyStoryTheme.typography.labelMedium
            )
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .testTag("searchBar")
    )

    LaunchedEffect(true) {
        awaitFrame()
        focusRequester.requestFocus()
    }
}

@Composable
fun PlaceList(places: LazyPagingItems<Place>, onBack: (Place) -> Unit) {
    val state = places.loadState.refresh
    if (state is LoadState.Error) {
        if ((state.error) is HttpException) {

        }
        Log.e("errr", "err")
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("searchResult")
    ) {
        items(
            count = places.itemCount,
            key = places.itemKey(key = {
                it.id
            }),
            contentType = places.itemContentType()
        ) { index ->
            val item = places[index]
            if (item != null) {
                Place(place = item, onBack = onBack)
            }
        }
    }
}

@Composable
fun Place(place: Place, onBack: (Place) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable {
                onBack(place)
            },
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = place.name,
                style = MyStoryTheme.typography.bodyLargeBold,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Text(
                text = place.category,
                style = MyStoryTheme.typography.bodySmallGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = place.distance,
                style = MyStoryTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Text(
                text = place.address,
                style = MyStoryTheme.typography.bodySmallGray
            )
        }
        if (place.phone.isNotEmpty()) {
            Text(
                text = place.phone,
                style = MyStoryTheme.typography.bodySmallPrimary,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchScreenDarkPreview() {
    val result = MutableStateFlow<PagingData<Place>>(PagingData.empty())

    MyStoryTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SearchScreen(
                uiState = SearchUiState(),
                searchResult = result.collectAsLazyPagingItems(),
                onQuery = {},
                onBack = {}
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun SearchScreenPreview() {
    val result = MutableStateFlow<PagingData<Place>>(PagingData.empty())

    MyStoryTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SearchScreen(
                uiState = SearchUiState(),
                searchResult = result.collectAsLazyPagingItems(),
                onQuery = {},
                onBack = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlacePreview() {
    val place = Place(
        id = "1",
        name = "스타벅스",
        lat = "34.234",
        lng = "123.34356",
        address = "서울시 강남구 강남동",
        category = "음식점 > 카페",
        distance = "500",
        phone = "010-1234-1234",
        url = "https://wwww.naver.com"
    )
    Place(place = place) {

    }
}