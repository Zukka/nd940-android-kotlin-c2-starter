package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.utils.getToday
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    enum class MenuFilter { SAVED, NEXT_WEEK, TODAY }

    private val menuFilter = MutableLiveData<MenuFilter>()

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    val asteroids = Transformations.switchMap(menuFilter) {filterData ->
        return@switchMap when (filterData) {
            MenuFilter.TODAY -> asteroidsRepository.todayAsteroids
            MenuFilter.NEXT_WEEK -> asteroidsRepository.weekAsteroids
            MenuFilter.SAVED -> asteroidsRepository.asteroids
        }
    }

    init {
        menuFilter.value = MenuFilter.TODAY
        getAsteroids()
        getImageOfTheDay()

    }

    val imageOfTheDay = asteroidsRepository.imageOfTheDay

    val potentiallyHazardous = asteroidsRepository

    private fun getAsteroids() {
        viewModelScope.launch {
            try {
                asteroidsRepository.getAsteroids(getToday())
            } catch (e: Exception) {
                println(e.stackTrace)
            }
        }
    }

    private fun getImageOfTheDay() {
        viewModelScope.launch {
            try {
                asteroidsRepository.getImageOfTheDay()
            } catch (e: Exception) {
                println(e.stackTrace)
            }
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsCompleted() {
        _navigateToSelectedAsteroid.value = null
    }

    fun setFilterAll() {
        menuFilter.value = MenuFilter.SAVED
    }

    fun setFilterNextWeek() {
        menuFilter.value = MenuFilter.NEXT_WEEK
    }

    fun setFilterToday() {
        menuFilter.value = MenuFilter.TODAY
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}