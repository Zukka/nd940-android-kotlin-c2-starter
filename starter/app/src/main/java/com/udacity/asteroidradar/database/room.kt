package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM databaseAsteroid ORDER BY close_approach_date")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM databaseAsteroid WHERE close_approach_date = :today ORDER BY  close_approach_date")
    fun getTodayAsteroids(today: String): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM databaseAsteroid WHERE close_approach_date BETWEEN :startDate AND :endDate ORDER BY  close_approach_date")
    fun getWeekAsteroids(startDate: String, endDate: String): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidEntity)

    @Query("DELETE FROM databaseAsteroid WHERE close_approach_date < :today")
    fun removeOldAsteroids(today: String)
}

@Database(entities = [AsteroidEntity::class], version = 1)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AsteroidsDatabase::class.java,
                    "asteroid_database").build()
        }
    }
    return INSTANCE
}
