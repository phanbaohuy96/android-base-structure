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

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase = LoginUseCase(authRepository)

    @Provides
    fun provideObserveSessionUseCase(authRepository: AuthRepository): ObserveSessionUseCase = ObserveSessionUseCase(authRepository)

    @Provides
    fun provideLogoutUseCase(authRepository: AuthRepository): LogoutUseCase = LogoutUseCase(authRepository)

    @Provides
    fun provideGetItemsUseCase(itemRepository: ItemRepository): GetItemsUseCase = GetItemsUseCase(itemRepository)

    @Provides
    fun provideRefreshItemsUseCase(itemRepository: ItemRepository): RefreshItemsUseCase = RefreshItemsUseCase(itemRepository)

    @Provides
    fun provideGetItemDetailUseCase(itemRepository: ItemRepository): GetItemDetailUseCase = GetItemDetailUseCase(itemRepository)
}
