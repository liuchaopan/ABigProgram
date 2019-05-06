package com.pan.abigprogram

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity(), View.OnTouchListener {

    private var mDetector: GestureDetector? = null //手势检测
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mDetector = GestureDetector(this, SimpleGestureListener())
        flipper.setOnTouchListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return mDetector?.onTouchEvent(event) ?: false
    }


    private inner class SimpleGestureListener : GestureDetector.SimpleOnGestureListener() {
        internal val FLING_MIN_DISTANCE = 100
        internal val FLING_MIN_VELOCITY = 200

        //不知道为什么，不加上onDown函数的话，onFling就不会响应，真是奇怪
        override fun onDown(e: MotionEvent): Boolean {
            // TODO Auto-generated method stub
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
