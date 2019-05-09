package com.pan.library.handler

import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter

class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {


    fun init() {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }


    override fun uncaughtException(t: Thread, e: Throwable) {
        Log.d(TAG, "Thread: $t")
        printCrashInfo2Log(e)
    }


    private fun printCrashInfo2Log(ex: Throwable) {
        val sb = StringBuilder()
        sb.append("<<<<<<<<<<<<<<<----START---->>>>>>>>>>>>>>>>>>\r\n")
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause: Throwable? = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append(result)
        sb.append("<<<<<<<<<<<<<<<<<----END---->>>>>>>>>>>>>>>>>>\r\n")
        Log.e(TAG, sb.toString())
    }

    companion object {
        private val TAG = CrashHandler::class.java.simpleName
        val instance = CrashHandler()
    }
}
