package com.mcgrady.xproject.account.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Failed(val exception: Exception) : Result<Nothing>()
    data class Error(val msg: String) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[msg=$msg]"
            is Failed -> "Exception[exception=$exception]"
        }
    }
}