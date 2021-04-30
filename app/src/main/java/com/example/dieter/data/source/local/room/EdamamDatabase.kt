package com.example.dieter.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dieter.data.source.local.entity.NutrientEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class EdamamDatabaseModule {
    @Provides
    fun providesEdamamDao(database: EdamamDatabase): EdamamDao {
        return database.dao
    }

    @Provides
    @Singleton
    fun getEdamamDatabase(@ApplicationContext context: Context): EdamamDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            EdamamDatabase::class.java,
            "edamam_db"
        ).build()
    }
}

@Database(
    entities = [NutrientEntity::class],
    version = 1,
    exportSchema = false
)
abstract class EdamamDatabase : RoomDatabase() {
    abstract val dao: EdamamDao
}
