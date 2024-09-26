package fr.burdairon.florian.views.components

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.burdairon.florian.model.Product
import fr.burdairon.florian.utils.MainUiState

@Composable
fun ProductList(mainUiState: MainUiState, onProductUpdate: (Product) -> Unit, onProductRemove: (Product) -> Unit) {
    if (mainUiState.productList.isEmpty()) {
        Text("Aucun produit")
    }
    else {
        Text("Liste des produits")
        Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(color = androidx.compose.ui.graphics.Color.DarkGray))
        LazyColumn {
            items(mainUiState.productList.size) { product ->
                ProductRow(mainUiState.productList[product],
                    onClick = {
                        // Open form to update product
                        onProductUpdate(mainUiState.productList[product])
                    },
                    onLongPress = {
                        // Remove the product from the list
                        onProductRemove(mainUiState.productList[product])
                    }
                )
                Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(color = androidx.compose.ui.graphics.Color.DarkGray))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductRow(product: Product, onClick : () -> Unit = {}, onLongPress : () -> Unit = {}) {
    Row (
        modifier = Modifier.fillMaxWidth().height(100.dp).padding(10.dp).combinedClickable(
            onClick = {
                onClick()
            },
            onLongClick = {
                onLongPress()
            }
        ),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start)
    ) {
        // Display the product
        ImageDisplay(Uri.parse(product.image), product.type)
        Column {
            Text(product.name)
            Text(product.type.toString())
            Text(product.color)
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(product.date)
            Text(product.country)
        }
    }
}