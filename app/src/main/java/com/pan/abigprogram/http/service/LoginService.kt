package com.pan.abigprogram.http.service

import com.pan.abigprogram.entity.UserAccessToken
import com.pan.abigprogram.http.service.bean.LoginRequestModel
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {

    @POST("authorizations")
    @Headers("Accept: application/json")
    fun authorizations(
            @Body authRequestModel: LoginRequestModel
    ): Flowable<UserAccessToken>
}