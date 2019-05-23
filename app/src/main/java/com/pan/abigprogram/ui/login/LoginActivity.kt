package com.pan.abigprogram.ui.login

import android.content.Context
import android.content.Intent
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
import com.sina.weibo.sdk.auth.AccessTokenKeeper
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
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
    private lateinit var mAccessToken: Oauth2AccessToken
    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效  */
    private val mSsoHandler: SsoHandler by instance()

    override val layoutId: Int
        get() = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binds()
        observeInputEvent()
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity.onActivityResult}
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data)
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
        sina_login.clicksThrottleFirst()
                .autoDisposable(scopeProvider)
                .subscribe {
                    mSsoHandler.authorize(SelfWbAuthListener())
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

    private inner class SelfWbAuthListener : com.sina.weibo.sdk.auth.WbAuthListener {
        override fun onSuccess(token: Oauth2AccessToken) {
            this@LoginActivity.runOnUiThread(Runnable {
                mAccessToken = token
                if (mAccessToken.isSessionValid) {
                    // 显示 Token
                    updateUiWithUser(mAccessToken.phoneNum)
                    // 保存 Token 到 SharedPreferences
                    AccessTokenKeeper.writeAccessToken(this@LoginActivity, mAccessToken)
                }
            })
        }

        override fun cancel() {

        }

        override fun onFailure(errorMessage: WbConnectErrorMessage) {
            Toast.makeText(this@LoginActivity, errorMessage.errorMessage, Toast.LENGTH_LONG).show()
        }
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
