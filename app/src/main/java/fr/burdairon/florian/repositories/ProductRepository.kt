package fr.burdairon.florian.repositories

import android.util.Log
import fr.burdairon.florian.dao.ProductDao
import fr.burdairon.florian.model.Product
import fr.burdairon.florian.utils.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ProductRepository {
    suspend fun getAll(): AppResult<List<Product>>
    suspend fun addProduct(product: Product): AppResult<Unit>
    suspend fun deleteProduct(product: Product): AppResult<Unit>
    suspend fun updateProduct(product: Product): AppResult<Unit>
}

class ProductRepositoryImpl(private val dao: ProductDao) : ProductRepository {
    override suspend fun getAll(): AppResult<List<Product>> {
        val data = withContext(Dispatchers.IO) {
            dao.getAll()
        }
        return if (data.isNotEmpty()) {
            Log.d("product db", "from db")
            AppResult.Success(data)
        } else
            AppResult.Error(Exception("No data in db"))
    }

    override suspend fun addProduct(product: Product): AppResult<Unit> {
        try {
            dao.insert(product)
            return AppResult.Success(Unit)
        } catch (e: Exception) {
            return AppResult.Error(e)
        }
    }

    override suspend fun deleteProduct(product: Product): AppResult<Unit> {
        try {
            dao.delete(product)
            return AppResult.Success(Unit)
        } catch (e: Exception) {
            return AppResult.Error(e)
        }
    }

    override suspend fun updateProduct(product: Product): AppResult<Unit> {
        try {
            dao.update(product)
            return AppResult.Success(Unit)
        } catch (e: Exception) {
            return AppResult.Error(e)
        }
    }

}