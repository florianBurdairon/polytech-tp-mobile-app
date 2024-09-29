package fr.burdairon.florian.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.burdairon.florian.viewmodels.MainViewModel
import fr.burdairon.florian.views.components.FavoriteList
import fr.burdairon.florian.views.components.ProductList
import fr.burdairon.florian.views.destinations.FormScreenDestination
import fr.burdairon.florian.views.destinations.RestaurantScreenDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Destination(start = true)
@Composable
fun MainScreen(navigator: DestinationsNavigator, snackbarHostState: SnackbarHostState, snackbarScope: CoroutineScope) {
    val mainViewModel: MainViewModel = getViewModel()

    val uiState by mainViewModel.uiState.collectAsState()
    var nameFilter by rememberSaveable { mutableStateOf("") }

    DisposableEffect(Unit) {
        mainViewModel.getAll(nameFilter)
        mainViewModel.getFavorite()
        onDispose { }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ProductList(
            mainUiState = uiState,
            // Open form to add a new product
            onNewProduct = { name ->
                navigator.navigate(FormScreenDestination(name ?: ""))
            },
            // Open form to update product
            onProductUpdate = {
                navigator.navigate(FormScreenDestination(product = it))
            },
            // Remove the product from the list
            onProductRemove = {
                mainViewModel.deleteProduct(it)
                snackbarScope.launch {
                    snackbarHostState.showSnackbar("Le produit a bien été supprimé.")
                }
            },
            // List of buttons to add a new product
            buttonValues = listOf("Pizza", "Burger", "Tacos", null),
            // Scrollable header for the product list
            // Contains the favorite list and the search bar
            header = {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    // Display the favorite list
                    FavoriteList(
                        mainUiState = uiState,
                        onProductUpdate = {
                            navigator.navigate(FormScreenDestination(product = it))
                        },
                        onProductRemove = {
                            mainViewModel.removeFavorite(it)
                            snackbarScope.launch {
                                snackbarHostState.showSnackbar("Le produit a bien été supprimé des favoris.")
                            }
                        }
                    )
                    // Display the search bar
                    TextField(
                        value = nameFilter,
                        onValueChange = {
                            nameFilter = it
                            mainViewModel.getAll(nameFilter)
                        },
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
                        label = { Text("Filtrer par nom") }
                    )
                }
            }
        )
        // Display the button to access the restaurant list (API)
        ExtendedFloatingActionButton(
            onClick = {
                navigator.navigate(RestaurantScreenDestination())
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
            icon = { Icon(Icons.Filled.LocationOn, "Voir les restaurants") },
            text = { Text("Voir les restaurants") },
        )
    }
}

