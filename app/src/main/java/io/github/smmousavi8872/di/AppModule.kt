package io.github.smmousavi8872.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.smmousavi8872.reactiveflow.ReactiveFlow

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun bindReactiveFlow() = ReactiveFlow()

}