package com.pan.abigprogram.ui.login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.pan.abigprogram.MainActivity
import com.pan.abigprogram.R
import com.pan.abigprogram.di.loginKodeinModule
import com.pan.abigprogram.di.sinaKodeinModule
import com.pan.abigprogram.ext.clicksThrottleFirst
import com.pan.abigprogram.ext.livedata.map
import com.pan.abigprogram.ext.livedata.toReactiveStream
import com.pan.library.view.activity.BaseActivity
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.sso.SsoHandler
import com.uber.autodispose.autoDisposable
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.Kodein
import org.kodein.di.generic.instance


class LoginActivity : BaseActivity() {

    override val kodein: Kodein = Kodein.lazy {
        extend(parentKodein)
        import(loginKodeinModule)
        import(sinaKodeinModule)
    }

    private val mViewModel: LoginViewModel by instance()
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能   */
    private var mAccessToken: Oauth2AccessToken? = null
    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效  */
    private var mSsoHandler: SsoHandler? = null

    override val layoutId: Int
        get() = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binds()
        observeInputEvent()
    }


    private fun updateUiWithUser(displayName: String) {
        val welcome = getString(R.string.welcome)
        // TODO : initiate successful logged in experience
        Toast.makeText(
                applicationContext,
                "$welcome $displayName",
                Toast.LENGTH_LONG
        ).show()
    }

    private fun binds() {
        mViewModel.loginIndicatorVisible
                .map { if (it) View.VISIBLE else View.GONE }
                .toReactiveStream()
                .autoDisposable(scopeProvider)
                .subscribe { loading.visibility = it }
        mViewModel.userInfo
                .toReactiveStream()
                .autoDisposable(scopeProvider)
                .subscribe {
                    updateUiWithUser(it.name)
                    MainActivity.launch(this)
                }
        mViewModel.autoLogin
                .toReactiveStream()
                .autoDisposable(scopeProvider)
                .subscribe {
                    username.setText(it.username, TextView.BufferType.EDITABLE)
                    password.setText(it.password, TextView.BufferType.EDITABLE)
                }

        login.clicksThrottleFirst()
                .autoDisposable(scopeProvider)
                .subscribe {
                    loading.visibility = View.VISIBLE
                    mViewModel.login(username.text.toString(), password.text.toString())
                    getSystemService(Context.INPUT_METHOD_SERVICE).run {
                        (this as InputMethodManager).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
                    }
                }
    }

    /**
     * Extension function to simplify setting an afterTextChanged action to EditText components.
     */
    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun observeInputEvent() {
        mViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        username.afterTextChanged {
            mViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                mViewModel.loginDataChanged(
                        username.text.toString(),
                        password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        mViewModel.login(
                                username.text.toString(),
                                password.text.toString()
                        )
                }
                false
            }

        }
    }
}
