package com.pan.abigprogram.data

import arrow.core.Either
import com.pan.abigprogram.entity.UserInfo
import com.pan.abigprogram.http.Errors
import com.pan.abigprogram.manager.UserManager
import io.reactivex.Flowable

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    fun logout() {
        dataSource.logout()
    }

    fun login(username: String, password: String): Flowable<Either<Errors, UserInfo>> {
        // 保存用户登录信息
        return dataSource.savePrefsUser(username, password)
                .andThen(dataSource.login())
                .doOnNext { either ->
                    either.fold({
                        // 如果登录失败，清除登录信息
                        dataSource.clearPrefsUser()
                        Unit
                    }, {
                        UserManager.INSTANCE = it
                    })
                }
                // 如果登录失败，清除登录信息
                .doOnError { dataSource.clearPrefsUser() }
    }

    fun fetchAutoLogin(): Flowable<AutoLoginEvent> {
        return dataSource.fetchAutoLogin()
    }

}
