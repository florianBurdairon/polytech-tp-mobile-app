package fr.burdairon.florian.di

import android.app.Application
import fr.burdairon.florian.dao.AppDatabase
import fr.burdairon.florian.dao.ProductDao
import fr.burdairon.florian.repositories.ProductRepository
import fr.burdairon.florian.repositories.ProductRepositoryImpl
import fr.burdairon.florian.repositories.RestaurantRepository
import fr.burdairon.florian.repositories.RestaurantRepositoryImpl
import fr.burdairon.florian.viewmodels.FormViewModel
import fr.burdairon.florian.viewmodels.MainViewModel
import fr.burdairon.florian.viewmodels.RestaurantViewModel
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

    fun provideRestaurantRepository(): RestaurantRepository {
        return RestaurantRepositoryImpl()
    }

    single { provideProductRepository(get()) }
    single { provideRestaurantRepository() }
}


val viewModelModule = module {
    viewModel {
        MainViewModel(repository = get())
    }
    viewModel {
        FormViewModel(repository = get())
    }
    viewModel {
        RestaurantViewModel(repository = get())
    }
}