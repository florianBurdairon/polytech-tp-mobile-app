package fr.burdairon.florian.views.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import fr.burdairon.florian.views.ProductType


@Composable
fun ImageDisplay(uri: Uri?, productType: ProductType, modifier: Modifier = Modifier) {
    if (uri == null || uri.toString().isEmpty()) {
        Image(
            painter = painterResource(id = productType.getDrawable()),
            contentDescription = "Product type image",
            modifier = modifier
        )
    } else {
        AsyncImage(
            model = uri,
            contentDescription = "Product image",
            modifier = modifier
        )
    }
}