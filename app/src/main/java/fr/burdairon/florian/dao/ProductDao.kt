package fr.burdairon.florian.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import fr.burdairon.florian.model.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM Products")
    fun getAll(): List<Product>

    @Query("SELECT * FROM Products WHERE id = :id")
    fun getById(id: Int): Product

    @Insert
    fun insert(product: Product)

    @Delete
    fun delete(product: Product)

    @Update
    fun update(product: Product)
}