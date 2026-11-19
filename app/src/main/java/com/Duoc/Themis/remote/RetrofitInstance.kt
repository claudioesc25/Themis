
package com.Duoc.Themis.remote

import android.content.Context
import com.Duoc.Themis.repository.SesionUsuario
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL =
        "https://x8ki-letl-twmt.n7.xano.io/api:q1QBI9WH/"

    private lateinit var sesionUsuario: SesionUsuario

    fun inicializar(context: Context) {
        sesionUsuario = SesionUsuario(context.applicationContext)
    }

    private val cliente: OkHttpClient by lazy {

        OkHttpClient.Builder()
            .addInterceptor { chain ->

                val token = if (::sesionUsuario.isInitialized) {
                    sesionUsuario.obtenerToken()
                } else {
                    null
                }

                val request = chain.request()
                    .newBuilder()
                    .apply {
                        if (!token.isNullOrBlank()) {
                            addHeader(
                                "Authorization",
                                "Bearer $token"
                            )
                        }
                    }
                    .build()

                chain.proceed(request)
            }
            .build()
    }

    val api: ApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(cliente)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ApiService::class.java)
    }
}
