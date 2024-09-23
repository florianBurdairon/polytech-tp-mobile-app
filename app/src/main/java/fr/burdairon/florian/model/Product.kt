package fr.burdairon.florian.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.burdairon.florian.views.ProductType
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Products")
@Parcelize
class Product(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val image: String,
    val type: ProductType,
    val name: String,
    val date: String,
    val color: String,
    val country: String,
    val isFavorite: Boolean
) : Parcelable