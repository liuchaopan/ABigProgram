package com.pan.abigprogram.http.service


import com.pan.abigprogram.utils.toast
import com.pan.library.transformer.GlobalErrorTransformer
import retrofit2.HttpException

fun <T> globalHandleError(): GlobalErrorTransformer<T> = GlobalErrorTransformer(
        globalDoOnErrorConsumer = { error ->
            when (error) {
                is HttpException -> {
                    when (error.code()) {
                        401 -> toast { "401 Unauthorized" }
                        404 -> toast { "404 failure" }
                        500 -> toast { "500 server failure" }
                        else -> toast { "network failure" }
                    }
                }
                else -> toast { "network failure" }
            }
        }
)