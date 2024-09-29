package fr.burdairon.florian.utils

sealed class AppResult<out T> {

    data class Success<out T>(
        val successData : T
    ) : AppResult<T>()

    class Error(
        private val exception: java.lang.Exception,
        val message: String = exception.localizedMessage?.toString() ?: ""
    ) : AppResult<Nothing>()
}