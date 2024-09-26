package fr.burdairon.florian.views.components

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.burdairon.florian.model.Product
import fr.burdairon.florian.utils.MainUiState

@Composable
fun FavoriteList(uiState: MainUiState, onProductUpdate: (Product) -> Unit, onProductRemove: (Product) -> Unit) {
    if (uiState.favoriteList.isEmpty()) {
        Text("Aucun favori")
    }
    else {
        Text("Liste des favoris")
        LazyRow {
            items(uiState.favoriteList.size) { product ->
                FavoriteItem(uiState.favoriteList[product],
                    onClick = {
                        // Open form to update product
                        onProductUpdate(uiState.favoriteList[product])
                    },
                    onLongPress = {
                        // Remove the product from the list
                        onProductRemove(uiState.favoriteList[product])
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteItem(product: Product, onClick : () -> Unit = {}, onLongPress : () -> Unit = {}) {
    Column (
        modifier = Modifier.size(150.dp).combinedClickable(
            onClick = {
                onClick()
            },
            onLongClick = {
                onLongPress()
            }
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
    ) {
        // Display the product
        ImageDisplay(Uri.parse(product.image), product.type, modifier = Modifier.size(100.dp))
        Text(product.name)
    }
}