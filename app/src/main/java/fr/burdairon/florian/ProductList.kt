package fr.burdairon.florian

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun ProductList(productList: List<Product>, onProductRemove: (Product) -> Unit) {
    if (productList.isEmpty()) {
        Text("Aucun produit")
        return
    }
    Text("Liste des produits")
    LazyColumn {
        items(productList.size) { product ->
            ProductRow(productList[product]) {
                // Remove the product from the list
                onProductRemove(productList[product])
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductRow(product: Product, onLongPress : () -> Unit = {}) {
    val context = LocalContext.current
    Row (
        modifier = Modifier.fillMaxWidth().height(100.dp).padding(10.dp).combinedClickable(
            onClick = {
                // Display the product details in toast
                val productDetails = "${product.name}, ${product.type}, ${product.date}, ${product.color}, ${product.country}, ${if (product.isFavorite) "oui" else "non"}"
                Toast.makeText(context, productDetails, Toast.LENGTH_SHORT).show()
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