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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import fr.burdairon.florian.model.Product
import fr.burdairon.florian.viewmodels.ProductViewModel
import fr.burdairon.florian.views.destinations.FormScreenDestination
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Destination(start = true)
@Composable
fun MainScreen(navigator: DestinationsNavigator, resultRecipient: ResultRecipient<FormScreenDestination, Product>, snackbarHostState: SnackbarHostState) {
    val scope = rememberCoroutineScope()
    val productViewModel: ProductViewModel = getViewModel()

    val uiState by productViewModel.uiState.collectAsState()

    resultRecipient.onNavResult {
        if (it is NavResult.Value) {
            productViewModel.addProduct(it.value)
            scope.launch {
                snackbarHostState.showSnackbar("Le produit a bien été ajouté")
            }
        }
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
                onClick = { navigator.navigate(FormScreenDestination(defaultName = "Kebab")) }
            ) {
                Text("Kebab")
            }
        }

        ProductList(uiState) {
            productViewModel.deleteProduct(it)
        }
    }
}

