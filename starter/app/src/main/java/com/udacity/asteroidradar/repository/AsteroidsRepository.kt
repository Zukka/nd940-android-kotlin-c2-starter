package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.utils.getDatePlusAWeek
import com.udacity.asteroidradar.utils.getToday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    suspend fun getAsteroids(startDate: String) {
        withContext(Dispatchers.IO) {
            val asteroids = Network.asteroidService.getAsteroids(startDate).await()
            println(asteroids)
            database.asteroidDao.insertAll(*parseAsteroidsJsonResult(JSONObject(asteroids)).asDatabaseModel())
        }
    }

    suspend fun removeOldAsteroid(todayDate: String) {
        withContext(Dispatchers.IO) {
            database.asteroidDao.removeOldAsteroids(todayDate)
        }
    }

    suspend fun getImageOfTheDay() {
        withContext(Dispatchers.IO) {
            val image = Network.asteroidService.getImageOfTheDay().await()
            _imageOfTheDay.postValue(image.asDomainModel())
        }
    }

    private val _imageOfTheDay = MutableLiveData<ImageOfTheDay>()

    val imageOfTheDay: LiveData<ImageOfTheDay>
        get() = _imageOfTheDay

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()) {
        it.asDomainModel()
    }

    val todayAsteroids: LiveData<List<Asteroid>> = Transformations.map(
            database.asteroidDao.getTodayAsteroids(
                    getToday()
            )
    ) {
        it.asDomainModel()
    }

    val weekAsteroids: LiveData<List<Asteroid>> = Transformations.map(
            database.asteroidDao.getWeekAsteroids(
                    getToday(),
                    getDatePlusAWeek()
            )
    ) {
        it.asDomainModel()
    }

}