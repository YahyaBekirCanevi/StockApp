package com.canevi.stockapp.network

import android.content.Context
import com.canevi.stockapp.util.Environment
import com.canevi.stockapp.util.NetworkUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException

object ApiClient {

    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String, context: Context): Retrofit? {
        // Check if the network is available
        if (!NetworkUtils.isNetworkAvailable(context)) {
            // Show a message or handle the offline case
            throw IOException("Network is not available. Please check your connection.")
        }

        if (retrofit == null) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor { chain ->
                    val request: Request = chain.request().newBuilder().build()
                    try {
                        return@addInterceptor chain.proceed(request)
                    } catch (e: SocketTimeoutException) {
                        throw IOException("Request timed out. Please try again.")
                    } catch (e: HttpException) {
                        throw IOException("HTTP error: ${e.message}")
                    } catch (e: IOException) {
                        throw IOException("Failed to connect to server. Please check the URL.")
                    }
                }
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit
    }
}