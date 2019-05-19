package com.pan.abigprogram.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pan.abigprogram.data.LoginRepository
import com.pan.library.util.SingletonHolderSingleArg

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory(
        private val repo: LoginRepository
) : ViewModelProvider.Factory {

    companion object : SingletonHolderSingleArg<LoginViewModelFactory, LoginRepository>(::LoginViewModelFactory)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
