package com.smmousavi.reactiveflow.di

import com.smmousavi.reactiveflow.flow.ReactiveFlow
import com.smmousavi.reactiveflow.flow.ReactiveFlowInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReactiveFlowModule {

    @Singleton
    @Binds
    abstract fun bindReactiveFlow(reactiveFlow: ReactiveFlow): ReactiveFlowInterface

}
