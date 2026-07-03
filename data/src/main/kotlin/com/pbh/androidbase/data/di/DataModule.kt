package com.pbh.androidbase.data.di

import android.content.Context
import androidx.room.Room
import com.pbh.androidbase.core.common.DefaultDispatcherProvider
import com.pbh.androidbase.core.common.DispatcherProvider
import com.pbh.androidbase.core.network.AuthHeaderInterceptor
import com.pbh.androidbase.core.network.RetrofitFactory
import com.pbh.androidbase.data.BuildConfig
import com.pbh.androidbase.data.local.AppDatabase
import com.pbh.androidbase.data.local.dao.ItemDao
import com.pbh.androidbase.data.local.datastore.SessionStore
import com.pbh.androidbase.data.remote.api.AuthApi
import com.pbh.androidbase.data.remote.api.ItemApi
import com.pbh.androidbase.data.remote.source.AuthRemoteDataSource
import com.pbh.androidbase.data.remote.source.MockAuthRemoteDataSource
import com.pbh.androidbase.data.remote.source.RealAuthRemoteDataSource
import com.pbh.androidbase.data.repository.AuthRepositoryImpl
import com.pbh.androidbase.data.repository.ItemRepositoryImpl
import com.pbh.androidbase.domain.repository.AuthRepository
import com.pbh.androidbase.domain.repository.ItemRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindItemRepository(impl: ItemRepositoryImpl): ItemRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(sessionStore: SessionStore): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(AuthHeaderInterceptor(sessionStore::currentToken))
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level =
                        if (BuildConfig.DEBUG) {
                            HttpLoggingInterceptor.Level.BASIC
                        } else {
                            HttpLoggingInterceptor.Level.NONE
                        }
                },
            ).build()

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        json: Json,
    ): Retrofit =
        RetrofitFactory.create(
            baseUrl = BuildConfig.BASE_URL,
            client = client,
            json = json,
        )

    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    fun provideItemApi(retrofit: Retrofit): ItemApi = retrofit.create(ItemApi::class.java)

    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(authApi: AuthApi): AuthRemoteDataSource =
        if (BuildConfig.USE_MOCK) {
            MockAuthRemoteDataSource()
        } else {
            RealAuthRemoteDataSource(authApi)
        }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room
            .databaseBuilder(context, AppDatabase::class.java, "android_base.db")
            .fallbackToDestructiveMigration(false)
            .build()

    @Provides
    fun provideItemDao(database: AppDatabase): ItemDao = database.itemDao()
}
