package com.vismiokt.landing_diary.data

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.vismiokt.landing_diary.LdApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {



    @Singleton
    @Provides
    fun providesDatabase (application: Application) = LdDatabase.getDatabase(application)

    @Singleton
    @Provides
    fun providesPlantsDao (database: LdDatabase) = database.plantDao()

    @Provides
    fun providesPlantsRepository(plantDao: PlantDao) : PlantsRepository = OfflinePlantsRepository(plantDao)
}

//@Module
//@InstallIn(ViewModelComponent::class)
//internal object ViewModelPlantsModule {
//    @Provides
//    @ViewModelScoped
//    fun provideIdPlant(handle: SavedStateHandle) =
//        MovieRepository(handle.getString("movie-id"));
//}