package com.pan.abigprogram.http

sealed class Errors : Throwable() {
    object NetworkError : Errors()
    object EmptyInputError : Errors()
    object EmptyResultsError : Errors()
    object SingleError : Errors()
}