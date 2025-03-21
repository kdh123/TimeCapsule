@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.dhkim.home.presentation

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.dhkim.designsystem.MyStoryTheme
import com.dhkim.home.R
import com.dhkim.home.presentation.add.AddTimeCapsuleAction
import com.dhkim.home.presentation.add.AddTimeCapsuleUiState
import com.dhkim.location.domain.model.Place
import kotlinx.coroutines.android.awaitFrame
import retrofit2.HttpException

@Composable
fun LocationSearchScreen(
    uiState: AddTimeCapsuleUiState,
    onAction: (AddTimeCapsuleAction) -> Unit
) {
    val searchResult = uiState.placeResult.collectAsLazyPagingItems()
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(query = uiState.placeQuery, onAction = onAction)
        Box(modifier = Modifier.fillMaxWidth()) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .width(64.dp)
                        .align(Alignment.Center),
                    color = Color.White,
                    trackColor = colorResource(id = R.color.primary),
                )
            }
            if (searchResult.itemCount > 0) {
                PlaceList(places = searchResult, onAction = onAction)
            } else {
                if (!uiState.isLoading && uiState.placeQuery.isNotEmpty()) {
                    //Text(text = "검색 결과가 존재하지 않습니다.", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: String, onAction: (AddTimeCapsuleAction) -> Unit) {
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        value = query,
        onValueChange = {
            onAction(AddTimeCapsuleAction.Query(it))
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            disabledTextColor = MaterialTheme.colorScheme.primary,
            disabledIndicatorColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        label = {
            Text(text = "장소 검색")
        },
        singleLine = true,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester)
    )

    LaunchedEffect(true) {
        awaitFrame()
        focusRequester.requestFocus()
    }
}

@Composable
fun PlaceList(places: LazyPagingItems<Place>, onAction: (AddTimeCapsuleAction) -> Unit) {
    val state = places.loadState.refresh
    if (state is LoadState.Error) {
        if ((state.error) is HttpException) {

        }
        Log.e("errr", "err")
    }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            count = places.itemCount,
            key = places.itemKey(key = {
                it.id
            }),
            contentType = places.itemContentType()
        ) { index ->
            val item = places[index]
            if (item != null) {
                Place(place = item, onAction = onAction)
            }
        }
    }
}

@Composable
fun Place(place: Place, onAction: (AddTimeCapsuleAction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable {
                onAction(AddTimeCapsuleAction.PlaceClick(place))
            },
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = place.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Text(
                maxLines = 1,
                text = place.category,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray,
                fontSize = 12.sp,
            )
        }

        if (place.address.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = place.address,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
        }

        if (place.phone.isNotEmpty()) {
            Text(
                text = place.phone,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LocationSearchScreenDarkPreview() {
    MyStoryTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LocationSearchScreen(
                uiState = AddTimeCapsuleUiState(),
                onAction = {}
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun LocationSearchScreenPreview() {
    MyStoryTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LocationSearchScreen(
                uiState = AddTimeCapsuleUiState(),
                onAction = {}
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