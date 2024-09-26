package fr.burdairon.florian.views

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.burdairon.florian.R
import fr.burdairon.florian.model.Product
import fr.burdairon.florian.utils.convertMillisToDate
import fr.burdairon.florian.utils.saveCachedFileToStorage
import fr.burdairon.florian.viewmodels.FormViewModel
import fr.burdairon.florian.views.components.ImageDisplay
import fr.burdairon.florian.views.components.ImagePicker
import fr.burdairon.florian.views.destinations.MainScreenDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun FormScreen(
    navigator: DestinationsNavigator,
    snackbarHostState: SnackbarHostState,
    defaultName: String = "",
    product: Product? = null
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val fileProvider = stringResource(id = R.string.fileprovider)
    val formViewModel: FormViewModel = getViewModel()

    var productType by rememberSaveable { mutableStateOf(product?.type ?: ProductType.CONSOMMABLE) }
    var name by rememberSaveable { mutableStateOf(product?.name ?: defaultName) }
    var date by rememberSaveable { mutableStateOf(product?.date ?: "") }
    var color: String by rememberSaveable { mutableStateOf(product?.color ?: "") }
    var country by rememberSaveable { mutableStateOf(product?.country ?: "") }
    var isFavorite by rememberSaveable { mutableStateOf(product?.isFavorite ?: false) }

    var imageUri: Uri? by rememberSaveable { mutableStateOf(
        product?.let { Uri.parse(product.image) }
    ) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        // Display the product type image
        ImageDisplay(imageUri, productType)
        ImagePicker(
            uri = null,
            directory = context.cacheDir,
            onSetUri = { uri ->
                val file = saveCachedFileToStorage(context, uri)
                imageUri = file?.let { FileProvider.getUriForFile(context, fileProvider, it) }
            }
        )

        // Display the product type radio buttons
        ProductTypeSelector(productType, onProductTypeSelected = { productType = it })

        TextField(value = name, onValueChange = { name = it }, label = { Text("Nom du produit*") })

        // Display the date picker dialog
        DateField(date, label = { Text("Date d'achat*") }, onDateSelected = { date = it }, onDismiss = {})

        // Display the color picker dialog
        ColorField(color) { color = it.hexCode }

        TextField(value = country, onValueChange = { country = it }, label = { Text("Pays d'origine") })

        // Display the favorite checkbox
        LabeledCheckbox(isFavorite, label = { Text("Ajouter aux favoris") }, onCheckedChange = { isFavorite = it })

        Row {
            Button(onClick = { navigator.navigate(MainScreenDestination()) }) {
                Text("Annuler")
            }
            // Display the validate button
            ValidateButton(productType, name, date, color, country, isFavorite, scope, snackbarHostState) { validate ->
                if (validate) {
                    if (product == null) {
                        formViewModel.addProduct(
                            Product(
                                id = 0,
                                image = imageUri?.toString() ?: "",
                                type = productType,
                                name = name,
                                date = date,
                                color = color,
                                country = country,
                                isFavorite = isFavorite
                            )
                        )
                        scope.launch {
                            snackbarHostState.showSnackbar("Le produit a bien été ajouté.")
                        }
                    }
                    else {
                        formViewModel.updateProduct(
                            Product(
                                id = product.id,
                                image = imageUri?.toString() ?: product.image,
                                type = productType,
                                name = name,
                                date = date,
                                color = color,
                                country = country,
                                isFavorite = isFavorite
                            )
                        )
                        scope.launch {
                            snackbarHostState.showSnackbar("Le produit a bien été modifié.")
                        }
                    }
                    navigator.navigate(MainScreenDestination())
                }
            }
        }

    }
}

@Composable
fun ProductTypeSelector(productType: ProductType, onProductTypeSelected: (ProductType) -> Unit) {
    Row(
        Modifier.selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (type in ProductType.entries) {
            RadioButton(selected = productType == type, onClick = { onProductTypeSelected(type) })
            Text(text = type.toString())
        }
    }
}

@Composable
fun LabeledCheckbox(isFavorite: Boolean, label: @Composable (() -> Unit), onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = isFavorite, onCheckedChange = { onCheckedChange(it) })
        label()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField(
    date: String,
    label: @Composable (() -> Unit),
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var dialogVisibility by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    TextField(value = date, onValueChange = { },
        label = label,
        enabled = false,
        modifier = Modifier.clickable { dialogVisibility = true },
        colors = TextFieldDefaults.colors(
            disabledContainerColor = TextFieldDefaults.colors().unfocusedContainerColor,
            disabledTextColor = TextFieldDefaults.colors().unfocusedTextColor,
            disabledPlaceholderColor = TextFieldDefaults.colors().unfocusedPlaceholderColor,
            disabledLabelColor = TextFieldDefaults.colors().unfocusedLabelColor,
            disabledLeadingIconColor = TextFieldDefaults.colors().unfocusedLeadingIconColor,
            disabledTrailingIconColor = TextFieldDefaults.colors().unfocusedTrailingIconColor
        )
    )

    if (!dialogVisibility) {
        return
    }

    DatePickerDialog(
        onDismissRequest = {
            onDismiss()
            dialogVisibility = false
        },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
                dialogVisibility = false
            }) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
                dialogVisibility = false
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorField(color: String, onColorSelected: (ColorEnvelope) -> Unit) {
    var dialogVisibility by remember { mutableStateOf(false) }
    val controller = rememberColorPickerController()

    TextField(value = color, onValueChange = { },
        label = { Text("Couleur") },
        enabled = false,
        modifier = Modifier.clickable { dialogVisibility = true },
        colors = TextFieldDefaults.colors(
            disabledContainerColor = TextFieldDefaults.colors().unfocusedContainerColor,
            disabledTextColor = TextFieldDefaults.colors().unfocusedTextColor,
            disabledPlaceholderColor = TextFieldDefaults.colors().unfocusedPlaceholderColor,
            disabledLabelColor = TextFieldDefaults.colors().unfocusedLabelColor,
            disabledLeadingIconColor = TextFieldDefaults.colors().unfocusedLeadingIconColor,
            disabledTrailingIconColor = TextFieldDefaults.colors().unfocusedTrailingIconColor
        )
    )

    if (!dialogVisibility) {
        return
    }

    var tempColor = if (color.isNotBlank()) ColorEnvelope(hexCode = color, color = Color(android.graphics.Color.parseColor("#$color")), fromUser = true) else null
    val initialColor = if (color.isNotBlank()) ColorEnvelope(hexCode = color, color = Color(android.graphics.Color.parseColor("#$color")), fromUser = true) else null


    BasicAlertDialog(
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        onDismissRequest = {
            onColorSelected(tempColor!!)
            dialogVisibility = false
        },
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(24.dp)),
        content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Choisir une couleur", modifier = Modifier.padding(top = 20.dp), style = MaterialTheme.typography.titleLarge)
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .padding(horizontal = 20.dp),
                    controller = controller,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        tempColor = colorEnvelope
                    },
                    initialColor = initialColor?.color ?: Color.White
                )
                Row(
                    horizontalArrangement = Arrangement.Absolute.Right,
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {
                    Button(
                        modifier = Modifier.padding(end = 10.dp),
                        onClick = {
                            dialogVisibility = false
                        }) {
                        Text("Cancel")
                    }
                    Button( onClick = {
                        onColorSelected(tempColor!!)
                        dialogVisibility = false
                    }) {
                        Text("OK")
                    }
                }
            }


        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidateButton(
    productType: ProductType,
    name: String,
    date: String,
    color: String,
    country: String,
    isFavorite: Boolean,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onValidate: (Boolean) -> Unit = {}
) {
    var dialogVisibility by remember { mutableStateOf(false) }

    Button(
        onClick = {
            if (name.isNotBlank() && date.isNotBlank()) {
                dialogVisibility = true
            }
            else {
                scope.launch {
                    snackbarHostState.showSnackbar("Veuillez remplir les champs obligatoires")
                }
            }
        }
    ) {
        Text("Valider")
    }

    if (!dialogVisibility) {
        return
    }

    BasicAlertDialog(
        onDismissRequest = {
            onValidate(false)
            dialogVisibility = false
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(24.dp)),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Voulez-vous ajouter ce produit ?")
            Text("Type : $productType")
            Text("Nom : $name")
            Text("Date : $date")
            Text("Couleur : $color")
            Text("Pays : $country")
            Text("Favori : ${if (isFavorite) "oui" else "non"}")
            Row(
                horizontalArrangement = Arrangement.Absolute.Right,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier.padding(end = 10.dp),
                    onClick = {
                        onValidate(false)
                        dialogVisibility = false
                    }
                ) {
                    Text("Annuler")
                }
                Button(
                    onClick = {
                        onValidate(true)
                        dialogVisibility = false
                    }
                ) {
                    Text("OK")
                }
            }
        }
    }
}

enum class ProductType {
    CONSOMMABLE{
        override fun getDrawable(): Int {
            return R.drawable.consommable_product
        }

        override fun toString(): String {
            return "Consommable"
        }
    },
    DURABLE{
        override fun getDrawable(): Int {
            return R.drawable.durable_product
        }

        override fun toString(): String {
            return "Durable"
        }
    },
    OTHER{
        override fun getDrawable(): Int {
            return R.drawable.other_product
        }

        override fun toString(): String {
            return "Autre"
        }
    };

    abstract fun getDrawable(): Int
}

