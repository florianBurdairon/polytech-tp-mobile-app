package fr.burdairon.florian.views.components

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.burdairon.florian.model.Restaurant
import fr.burdairon.florian.utils.RestaurantUiState

@Composable
fun RestaurantList(restaurantUiState: RestaurantUiState, onEndScroll: () -> Unit = {}) {
    val listState = rememberLazyListState()

    // Check if the list is empty and the API call has failed
    if (restaurantUiState.restaurantList.isEmpty() && !restaurantUiState.apiSuccess) {
        Text("Impossible de récupérer les données")
    }
    // Check if the list is empty and the API call has succeeded
    else if (restaurantUiState.restaurantList.isEmpty()) {
        Text("Aucun restaurant trouvé")
    }
    // If the list is not empty, display it
    else {
        Text("Liste des restaurants", modifier = Modifier.padding(10.dp))
        Spacer(modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(color = androidx.compose.ui.graphics.Color.DarkGray))
        LazyColumn(state = listState) {
            // For each restaurant, display a row
            items(restaurantUiState.restaurantList.size) { restaurant ->
                RestaurantRow(restaurantUiState.restaurantList[restaurant])
                Spacer(modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(color = androidx.compose.ui.graphics.Color.DarkGray))
            }
            // If the API call has failed, display an error message
            if (!restaurantUiState.apiSuccess) {
                item {
                    Text(
                        text = "Impossible de récupérer les données",
                        modifier = Modifier.padding(10.dp).fillMaxWidth() ,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // When the user scrolls to the end of the list, call the onEndScroll function
        LaunchedEffect(key1 = listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
                .collect { lastVisibleItem ->
                    if (lastVisibleItem != null && lastVisibleItem.index == restaurantUiState.restaurantList.size - 5) {
                        Log.d("RestaurantList", "onEndScroll")
                        onEndScroll()
                    }
                }
        }
    }
}

@Composable
fun RestaurantRow(restaurant: Restaurant) {
    val context = LocalContext.current

    val location = "${restaurant.meta_geo_point?.lat},${restaurant.meta_geo_point?.lon}"

    Row (
        modifier = Modifier
            .height(75.dp)
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
            .clickable {
                if (restaurant.meta_geo_point == null) return@clickable

                // Open Google Maps with the restaurant location
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("geo:$location?q=${location}")
                }
                context.startActivity(intent)
            },
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(restaurant.name ?: "")
            Text(restaurant.cuisine?.joinToString() ?: "")

        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(restaurant.type ?: "")
            Text(restaurant.meta_name_com ?: "")
        }
    }
}