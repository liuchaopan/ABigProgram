package com.pan.abigprogram.data

import arrow.core.Either
import com.pan.abigprogram.entity.UserInfo
import com.pan.abigprogram.http.Errors
import com.pan.abigprogram.http.service.ServiceManager
import com.pan.abigprogram.http.service.bean.LoginRequestModel
import com.pan.abigprogram.repository.UserInfoRepository
import com.pan.library.util.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(private val userRepository: UserInfoRepository
                      , private val serviceManager: ServiceManager) {

    fun login(): Flowable<Either<Errors, UserInfo>> {
        val authObservable = serviceManager.loginService
                .authorizations(LoginRequestModel.generate())

        val ownerInfoObservable = serviceManager.userService
                .fetchUserOwner()

        return authObservable                       // 1.用户登录认证
                .flatMap { ownerInfoObservable }    // 2.获取用户详细信息
                .subscribeOn(RxSchedulers.io)
                .map {
                    Either.right(it)
                }
    }

    fun logout() {
        clearPrefsUser()
    }

    fun savePrefsUser(username: String, password: String): Completable {
        return Completable.fromAction {
            userRepository.username = username
            userRepository.password = password
        }
    }

    fun clearPrefsUser(): Completable {
        return Completable.fromAction {
            userRepository.username = ""
            userRepository.password = ""
        }
    }

    fun fetchAutoLogin(): Flowable<AutoLoginEvent> {
        val username = userRepository.username
        val password = userRepository.password
        val isAutoLogin = userRepository.isAutoLogin
        return Flowable.just(when (username.isNotEmpty() && password.isNotEmpty() && isAutoLogin) {
            true -> AutoLoginEvent(true, username, password)
            false -> AutoLoginEvent(false, "", "")
        })
    }
}

data class AutoLoginEvent(
        val autoLogin: Boolean,
        val username: String,
        val password: String
)
