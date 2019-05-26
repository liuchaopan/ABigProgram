package com.pan.abigprogram

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.pan.abigprogram.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*


class SplashActivity : AppCompatActivity(), View.OnTouchListener, View.OnClickListener {

    private var mDetector: GestureDetector? = null //手势检测
    private var mTimer: Timer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
        setContentView(R.layout.activity_splash)
        mDetector = GestureDetector(this, SimpleGestureListener())
        flipper.setOnTouchListener(this)
        cancel_action.setOnClickListener(this)
        mTimer = Timer().also {
            it.schedule(object : TimerTask() {
                override fun run() {
                    login()
                }
            }, 12000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mTimer?.cancel()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return mDetector?.onTouchEvent(event) ?: false
    }

    override fun onClick(v: View?) {
        login()
    }

    private fun login() {
        flipper.stopFlipping()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private inner class SimpleGestureListener : GestureDetector.SimpleOnGestureListener() {
        internal val FLING_MIN_DISTANCE = 100
        internal val FLING_MIN_VELOCITY = 200

        //不知道为什么，不加上onDown函数的话，onFling就不会响应，真是奇怪
        override fun onDown(e: MotionEvent): Boolean {
            cancel_action.visibility = View.VISIBLE
            cancel_action.postDelayed({ cancel_action.visibility = View.GONE }, 1000)
            Snackbar.make(flipper, "ondown", Snackbar.LENGTH_SHORT).show()
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float,
                             velocityY: Float): Boolean {
            // Fling left
            if (e1.x - e2.x > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {

                flipper.showNext()

                Snackbar.make(flipper, "Fling Left", Snackbar.LENGTH_SHORT).show()
            } else if (e2.x - e1.x > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                // Fling right

                flipper.showPrevious()

                Snackbar.make(flipper, "Fling Right", Snackbar.LENGTH_SHORT).show()
            }
            return true
        }
    }
}
