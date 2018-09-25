package id.eaz.soccerclub.api

import android.content.Context
import com.google.gson.GsonBuilder
import id.eaz.soccerclub.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient(private val mContext: Context? = null) {

    private val maxTimeoutApi = 10
    var retrofit: Retrofit

    init {
        val gsonN = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls()
                .create()

        val okHttpClientSet = {
            val okBuild = OkHttpClient.Builder()
            if(mContext != null) okBuild.addInterceptor(ConnectivityInterceptor(mContext))
            if (BuildConfig.DEBUG) {
                //with loging
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                okBuild.addInterceptor(logging)
            }
            okBuild.connectTimeout(maxTimeoutApi.toLong(), TimeUnit.SECONDS)
                    .writeTimeout(maxTimeoutApi.toLong(), TimeUnit.SECONDS)
                    .readTimeout(maxTimeoutApi.toLong(), TimeUnit.SECONDS).build()
        }

        retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL + "/" + BuildConfig.API_KEY + "/")
                .client(okHttpClientSet())
                .addConverterFactory(GsonConverterFactory.create(gsonN))
                .build()
    }
}