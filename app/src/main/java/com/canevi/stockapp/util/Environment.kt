package com.canevi.stockapp.util

import com.canevi.stockapp.BuildConfig

object Environment {
    val BASE_URL: String
        get() {
            return "https://stock-manage-app-xrga.onrender.com/"

            /*when (BuildConfig.BUILD_TYPE) {
                "debug" -> "http://localhost:8080/" // Development environment
                "release" -> "https://stock-manage-app-xrga.onrender.com/" // Production environment
                else -> "http://localhost:8080/"
            }*/
        }
}