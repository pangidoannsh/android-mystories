package com.pangidoannsh.mystories.data.api

import com.pangidoannsh.mystories.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        private const val BASE_URL = "https://story-api.dicoding.dev/v1/"

        private var token = ""

        fun setToken(newToken: String) {
            token = newToken
        }

        private fun getHeaderInterceptor(): Interceptor {
            return Interceptor { chain ->
                var request = chain.request()

                if (request.header("No-Authentication") == null) {
                    if (token.isNotEmpty()) {
                        request = request.newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .build()
                    }
                }
                chain.proceed(request)
            }
        }

        fun getApiService(): ApiService {

            val client = OkHttpClient.Builder()
                .addInterceptor(getHeaderInterceptor())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}