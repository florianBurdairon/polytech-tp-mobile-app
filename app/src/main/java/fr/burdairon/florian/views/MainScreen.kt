package fr.burdairon.florian.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.burdairon.florian.viewmodels.MainViewModel
import fr.burdairon.florian.views.components.FavoriteList
import fr.burdairon.florian.views.components.ProductList
import fr.burdairon.florian.views.destinations.FormScreenDestination
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Destination(start = true)
@Composable
fun MainScreen(navigator: DestinationsNavigator, snackbarHostState: SnackbarHostState) {
    val scope = rememberCoroutineScope()
    val mainViewModel: MainViewModel = getViewModel()

    val uiState by mainViewModel.uiState.collectAsState()

    DisposableEffect(Unit) {
        mainViewModel.getAll()
        mainViewModel.getFavorite()
        onDispose { }
    }


    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text ("Ajouter un produit")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = { navigator.navigate(FormScreenDestination(defaultName = "Pizza")) }
            ) {
                Text("Pizza")
            }
            Button(
                onClick = { navigator.navigate(FormScreenDestination(defaultName = "Burger")) }
            ) {
                Text("Burger")
            }
            Button(
                onClick = { navigator.navigate(FormScreenDestination(defaultName = "Tacos")) }
            ) {
                Text("Tacos")
            }
            Button(
                onClick = { navigator.navigate(FormScreenDestination()) }
            ) {
                Text("Autre")
            }
        }

        FavoriteList(
            uiState = uiState,
            onProductUpdate = {
                navigator.navigate(FormScreenDestination(product = it))
            },
            onProductRemove = {
                mainViewModel.removeFavorite(it)
                scope.launch {
                    snackbarHostState.showSnackbar("Le produit a bien été supprimé des favoris.")
                }
            }
        )

        ProductList(
            uiState = uiState,
            onProductUpdate = {
                navigator.navigate(FormScreenDestination(product = it))
            },
            onProductRemove = {
                mainViewModel.deleteProduct(it)
                scope.launch {
                    snackbarHostState.showSnackbar("Le produit a bien été supprimé.")
                }
            }
        )
    }
}

