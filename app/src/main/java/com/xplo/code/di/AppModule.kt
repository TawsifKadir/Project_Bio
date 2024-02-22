package com.xplo.code.di


import com.xplo.code.data.db.DbController
import com.xplo.code.data.db.dao.HouseholdDao
import com.xplo.code.data.db.dao.PostDao
import com.xplo.code.data.db.repo.DbRepo
import com.xplo.code.data.db.repo.DbRepoImpl
import com.xplo.data.network.api.ContentApi
import com.xplo.data.network.api.UserApi
import com.xplo.data.repo.ContentRepo
import com.xplo.data.repo.ContentRepoImpl
import com.xplo.data.repo.UserRepo
import com.xplo.data.repo.UserRepoImpl
import com.xplo.data.core.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }

    @Singleton
    @Provides
    fun providePostDao(): PostDao = DbController.getAppDb().postDao()

    @Singleton
    @Provides
    fun provideHouseholdDao(): HouseholdDao = DbController.getAppDb().householdDao()


    @Singleton
    @Provides
    fun provideDbRepo(dao: PostDao, householdDao: HouseholdDao): DbRepo =
        DbRepoImpl(dao, householdDao)

    @Singleton
    @Provides
    fun provideUserRepo(api: UserApi): UserRepo = UserRepoImpl(api)

    @Singleton
    @Provides
    fun provideContentRepo(api: ContentApi): ContentRepo = ContentRepoImpl(api)
}