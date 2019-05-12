package com.pan.abigprogram.repository

import android.content.SharedPreferences
import com.pan.library.util.SingletonHolderSingleArg
import com.pan.library.util.prefs.boolean
import com.pan.library.util.prefs.string

class UserInfoRepository(prefs: SharedPreferences) {

    var accessToken: String by prefs.string("user_access_token", "")

    var username by prefs.string("username", "")

    var password by prefs.string("password", "")

    var isAutoLogin: Boolean by prefs.boolean("auto_login", true)

    companion object :
            SingletonHolderSingleArg<UserInfoRepository, SharedPreferences>(::UserInfoRepository)
}