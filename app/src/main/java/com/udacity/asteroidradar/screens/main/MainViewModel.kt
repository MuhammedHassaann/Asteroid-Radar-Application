package com.udacity.asteroidradar.screens.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.repository.AsteroidMainRepository
import com.udacity.asteroidradar.utils.DateFormatter
import com.udacity.asteroidradar.utils.PermissionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    //get startDate
    private val startDate = DateFormatter.getCurrentDate()

    //get endDate
    private val endDate = DateFormatter.getDateAfter7Days()

    private val repository: AsteroidMainRepository by lazy { AsteroidMainRepository(app) }

    val asteroids: LiveData<List<Asteroid>> = repository.allAsteroids

    private val _allAsteroids = MutableLiveData<List<Asteroid>>()
    val allAsteroids: LiveData<List<Asteroid>> get() = _allAsteroids

    private val _todayAsteroids = MutableLiveData<List<Asteroid>>()
    val todayAsteroids: LiveData<List<Asteroid>> get() = _todayAsteroids

    private val _weekAsteroids = MutableLiveData<List<Asteroid>>()
    val weekAsteroids: LiveData<List<Asteroid>> get() = _weekAsteroids

    private val _imgOfTheDay = MutableLiveData<String>()
    val imgOfTheDay: LiveData<String> get() = _imgOfTheDay

    private val _imgTitle = MutableLiveData<String>()
    val imgTitle: LiveData<String> get() = _imgTitle

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> get() = _showDialog

    init {
        addAsteroidsToDatabase()
        val connected = PermissionManager.checkInternetConnectivity(context = app.applicationContext)
        if (!connected){
            showConfirmationDialog()
        }else{
            dismissDialog()
        }
    }



    fun getAllAsteroids(){
        viewModelScope.launch{
            _allAsteroids.value = repository.getAllAsteroids()
        }
    }

    fun addAsteroidsToDatabase(){
        viewModelScope.launch{
            repository.addAsteroidsToDatabase()
        }
    }

    fun getAsteroidsOfToday(){
        viewModelScope.launch {
            _todayAsteroids.value = repository.getAsteroidsOfToday(startDate)
        }
    }

    fun getAsteroidsOfTheWeek(){
        viewModelScope.launch{
            _weekAsteroids.value = repository.getAsteroidsOfTheWeek(startDate,endDate)
        }
    }

    fun getPicOfTheDay(){
        viewModelScope.launch{
            try {
                _imgOfTheDay.value = repository.getPicOfTheDay()[0]
                _imgTitle.value = repository.getPicOfTheDay()[1]
            }catch (e: Exception){
                Log.i("pic error", "empty list ${e.message.toString()}")
            }
        }
    }


    fun showConfirmationDialog() {
        _showDialog.value = true
    }

    fun dismissDialog() {
        _showDialog.value = false
    }


}