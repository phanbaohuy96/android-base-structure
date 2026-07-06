package com.pbh.androidbase.di

import com.pbh.androidbase.domain.repository.AuthRepository
import com.pbh.androidbase.domain.repository.ItemRepository
import com.pbh.androidbase.domain.usecase.GetItemDetailUseCase
import com.pbh.androidbase.domain.usecase.GetItemsUseCase
import com.pbh.androidbase.domain.usecase.LoginUseCase
import com.pbh.androidbase.domain.usecase.LogoutUseCase
import com.pbh.androidbase.domain.usecase.ObserveSessionUseCase
import com.pbh.androidbase.domain.usecase.RefreshItemsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** App composition-root providers for domain use cases consumed by ViewModels. */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /** Provides login orchestration for the auth feature. */
    @Provides
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase = LoginUseCase(authRepository)

    /** Provides session observation for the app start-route guard. */
    @Provides
    fun provideObserveSessionUseCase(authRepository: AuthRepository): ObserveSessionUseCase = ObserveSessionUseCase(authRepository)

    /** Provides logout orchestration for the home feature. */
    @Provides
    fun provideLogoutUseCase(authRepository: AuthRepository): LogoutUseCase = LogoutUseCase(authRepository)

    /** Provides cached item observation for the home list. */
    @Provides
    fun provideGetItemsUseCase(itemRepository: ItemRepository): GetItemsUseCase = GetItemsUseCase(itemRepository)

    /** Provides item refresh orchestration for the home list. */
    @Provides
    fun provideRefreshItemsUseCase(itemRepository: ItemRepository): RefreshItemsUseCase = RefreshItemsUseCase(itemRepository)

    /** Provides item detail loading for the detail screen. */
    @Provides
    fun provideGetItemDetailUseCase(itemRepository: ItemRepository): GetItemDetailUseCase = GetItemDetailUseCase(itemRepository)
}
