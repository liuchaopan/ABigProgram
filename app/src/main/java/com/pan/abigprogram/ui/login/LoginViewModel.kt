package com.pan.abigprogram.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import arrow.core.Option
import arrow.core.none
import arrow.core.some
import com.pan.abigprogram.R
import com.pan.abigprogram.data.AutoLoginEvent
import com.pan.abigprogram.data.LoginRepository
import com.pan.abigprogram.data.Result
import com.pan.abigprogram.entity.UserInfo
import com.pan.abigprogram.ext.arrow.whenNotNull
import com.pan.abigprogram.ext.livedata.toReactiveStream
import com.pan.abigprogram.http.Errors
import com.pan.abigprogram.http.service.globalHandleError
import com.pan.abigprogram.utils.toast
import com.pan.library.viewmodel.AutoDisposeViewModel
import com.uber.autodispose.autoDisposable
import io.reactivex.Single
import retrofit2.HttpException

class LoginViewModel(private val loginRepository: LoginRepository) : AutoDisposeViewModel() {

    private val error: MutableLiveData<Option<Throwable>> = MutableLiveData()
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm
    val loginIndicatorVisible: MutableLiveData<Boolean> = MutableLiveData()
    val userInfo: MutableLiveData<UserInfo> = MutableLiveData()
    val autoLogin: MutableLiveData<AutoLoginEvent> = MutableLiveData()

    init {
        autoLogin.toReactiveStream()
                .filter { it.autoLogin }
                .doOnNext { login(it.username, it.password) }
                .autoDisposable(this)
                .subscribe()

        error.toReactiveStream()
                .map { errorOpt ->
                    errorOpt.flatMap {
                        when (it) {
                            is Errors.EmptyInputError -> "username or password can't be null.".some()
                            is HttpException ->
                                when (it.code()) {
                                    401 -> "username or password failure.".some()
                                    else -> "network failure".some()
                                }
                            else -> none()
                        }
                    }
                }
                .autoDisposable(this)
                .subscribe { errorMsg ->
                    errorMsg.whenNotNull {
                        toast { it }
                    }
                }

        initAutoLogin()
                .autoDisposable(this)
                .subscribe()
    }

    private fun initAutoLogin(): Single<AutoLoginEvent> {
        return loginRepository.fetchAutoLogin()
                .singleOrError()
                .onErrorReturn { AutoLoginEvent(false, "", "") }
                .doOnSuccess { event ->
                    applyState(autoLogin = event, loginIndicator = false)
                }
    }

    fun login(username: String?, password: String?) {
        when (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            true -> applyState(error = Errors.EmptyInputError.some())
            false -> loginRepository
                    .login(username, password)
                    .compose(globalHandleError())
                    .map { either ->
                        either.fold({
                            Result.failure<UserInfo>(it)
                        }, {
                            Result.success(it)
                        })
                    }
                    .startWith(Result.loading())
                    .startWith(Result.idle())
                    .onErrorReturn { Result.failure(it) }
                    .autoDisposable(this)
                    .subscribe { state ->
                        when (state) {
                            is Result.Loading -> applyState(loginIndicator = true)
                            is Result.Idle -> applyState(loginIndicator = false)
                            is Result.Failure -> applyState(error = state.error.some(), loginIndicator = false)
                            is Result.Success -> applyState(user = state.data.some(), loginIndicator = false)
                        }
                    }
        }
    }

    private fun applyState(user: Option<UserInfo> = none(),
                           error: Option<Throwable> = none(),
                           loginIndicator: Boolean? = null,
                           autoLogin: AutoLoginEvent? = null) {
        this.error.postValue(error)
        this.userInfo.postValue(user.orNull())

        loginIndicator?.apply(loginIndicatorVisible::postValue)

        autoLogin?.let {
            this.autoLogin.postValue(autoLogin)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
