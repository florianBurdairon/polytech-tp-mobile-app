package fr.burdairon.florian.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.burdairon.florian.viewmodels.RestaurantViewModel
import fr.burdairon.florian.views.components.RestaurantList
import fr.burdairon.florian.views.destinations.MainScreenDestination
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun RestaurantScreen(navigator: DestinationsNavigator) {
    val restaurantViewModel: RestaurantViewModel = getViewModel()

    val uiState by restaurantViewModel.uiState.collectAsState()

    DisposableEffect(Unit) {
        restaurantViewModel.getRestaurants()
        onDispose { }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            navigator.navigate(MainScreenDestination())
        }) {
            Text("Retour")
        }
        RestaurantList(restaurantUiState = uiState) {
            // When the user scrolls to the end of the list, call the API to get more restaurants
            restaurantViewModel.loadMore()
        }
    }
}