package fr.burdairon.florian.di

import android.app.Application
import fr.burdairon.florian.dao.AppDatabase
import fr.burdairon.florian.dao.ProductDao
import fr.burdairon.florian.repositories.ProductRepository
import fr.burdairon.florian.repositories.ProductRepositoryImpl
import fr.burdairon.florian.viewmodels.ProductViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val databaseModule = module {
    fun provideDatabase(application: Application): AppDatabase {
        return AppDatabase.getInstance(application)
    }

    fun provideProductDao(database: AppDatabase): ProductDao {
        return  database.productDao
    }

    single { provideDatabase(androidApplication()) }
    single { provideProductDao(get()) }
}

val repositoryModule = module {
    fun provideProductRepository(dao: ProductDao): ProductRepository {
        return ProductRepositoryImpl(dao)
    }

    single { provideProductRepository(get()) }
}


val viewModelModule = module {
    viewModel {
        ProductViewModel(repository = get())
    }
}