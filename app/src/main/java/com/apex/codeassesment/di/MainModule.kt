package com.apex.codeassesment.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.apex.codeassesment.data.UserRepository
import com.apex.codeassesment.data.UserRepositoryInterface
import com.apex.codeassesment.data.local.LocalDataSource
import com.apex.codeassesment.data.local.PreferencesManager
import com.apex.codeassesment.data.local.PreferencesManagerInterface
import com.apex.codeassesment.data.remote.RemoteDataSource
import com.apex.codeassesment.data.remote.UserApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
object MainModule {

    private const val TIME_OUT = 30L
    private const val baseUrl = "https://randomuser.me"

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("random-user-preferences", Context.MODE_PRIVATE)
    }

    @Provides
    fun providePreferencesManager(context: Context): PreferencesManagerInterface =
        PreferencesManager(context)

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Singleton
    @Provides
    fun provideGson() : Gson = GsonBuilder().create()


    @Provides
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()



    @Provides
    fun provideloggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.HEADERS)
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun provideOkHTTPClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .build()
                chain.proceed(request)
            }).build()
    }

    @Provides
    fun provideUserApi(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): UserApi {
        return Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient)
            .addConverterFactory(gsonConverterFactory).build()
            .create(UserApi::class.java)
    }

    @Provides
    fun provideUserRepositoryInterface(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
        userApi: UserApi

    ): UserRepositoryInterface = UserRepository(localDataSource, remoteDataSource, userApi )

}