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
import com.pbh.androidbase.data.remote.source.ItemRemoteDataSource
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

/** Hilt bindings for repository interfaces implemented by the data module. */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    /** Binds the auth repository port to the data implementation. */
    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}

/** Hilt providers for data-layer infrastructure and remote source selection. */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    /** Provides coroutine dispatchers used by repositories. */
    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

    /** Provides JSON configured to tolerate unknown API fields. */
    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
        }

    /** Provides the OkHttp client with auth and environment-aware logging interceptors. */
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

    /** Provides Retrofit configured with the flavor-specific base URL. */
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

    /** Creates the auth Retrofit API. */
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    /** Creates the item Retrofit API. */
    @Provides
    fun provideItemApi(retrofit: Retrofit): ItemApi = retrofit.create(ItemApi::class.java)

    /** Selects mock or real auth according to `BuildConfig.USE_MOCK`. */
    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(authApi: AuthApi): AuthRemoteDataSource =
        if (BuildConfig.USE_MOCK) {
            MockAuthRemoteDataSource()
        } else {
            RealAuthRemoteDataSource(authApi)
        }

    /** Opens the Room database that owns local app caches. */
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room
            .databaseBuilder(context, AppDatabase::class.java, "android_base.db")
            .fallbackToDestructiveMigration(false)
            .build()

    /** Provides the item DAO from [AppDatabase]. */
    @Provides
    fun provideItemDao(database: AppDatabase): ItemDao = database.itemDao()

    /** Provides the item repository with seed fallback limited to mock builds. */
    @Provides
    @Singleton
    fun provideItemRepository(
        @ApplicationContext context: Context,
        itemDao: ItemDao,
        remoteDataSource: ItemRemoteDataSource,
        dispatcherProvider: DispatcherProvider,
    ): ItemRepository =
        ItemRepositoryImpl(
            context = context,
            itemDao = itemDao,
            remoteDataSource = remoteDataSource,
            dispatcherProvider = dispatcherProvider,
            useSeedFallback = BuildConfig.USE_MOCK,
        )
}
