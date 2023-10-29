package com.pangidoannsh.mystories.data

sealed class ResultRes<out R> private constructor() {
    data class Success<out T>(val data: T) : ResultRes<T>()
    data class Error(val error: String) : ResultRes<Nothing>()
    object Loading : ResultRes<Nothing>()
}