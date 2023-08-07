package io.github.smmousavi8872.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.smmousavi8872.reactiveflow.ReactiveFlow
import io.github.smmousavi8872.reactiveflow.ReactiveFlowInterface
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReactiveFlowModule {

    @Singleton
    @Binds
    abstract fun bindReactiveFlow(reactiveFlow: ReactiveFlow): ReactiveFlowInterface

}