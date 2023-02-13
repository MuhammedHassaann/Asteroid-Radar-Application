package com.udacity.asteroidradar.screens.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.repository.AsteroidMainRepository
import com.udacity.asteroidradar.utils.DateFormatter
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    //get startDate
    @RequiresApi(Build.VERSION_CODES.O)
    private val today = DateFormatter.getCurrentDate()

    @RequiresApi(Build.VERSION_CODES.O)
    private val tomorrow = DateFormatter.getTomorrowDate()

    //get endDate
    @RequiresApi(Build.VERSION_CODES.O)
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

    private val _showDialog = MutableLiveData<Boolean>(false)
    val showDialog: LiveData<Boolean> get() = _showDialog

    init {
        addAsteroidsToDatabase()
        checkInternetConnection(app)
    }


    fun getAllAsteroids() {
        viewModelScope.launch {
            _allAsteroids.value = repository.getAllAsteroids(startDate = today, endDate = endDate)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addAsteroidsToDatabase() {
        viewModelScope.launch {
            repository.addAsteroidsToDatabase()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAsteroidsOfToday() {
        viewModelScope.launch {
            _todayAsteroids.value = repository.getAsteroidsOfToday(today)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAsteroidsOfTheWeek() {
        viewModelScope.launch {
            _weekAsteroids.value = repository.getAsteroidsOfTheWeek(tomorrow, endDate)
        }
    }

    fun getPicOfTheDay() {
        viewModelScope.launch {
            try {
                _imgOfTheDay.value = repository.getPicOfTheDay()[0]
                _imgTitle.value = repository.getPicOfTheDay()[1]
            } catch (e: Exception) {
                Log.i("pic error", "empty list ${e.message.toString()}")
            }
        }
    }


    fun showDialog() {
        _showDialog.value = true
    }

    fun dismissDialog() {
        _showDialog.value = false
    }


    private fun checkInternetConnection(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest =
            NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(
            networkRequest,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    // Internet connection is available
                    getPicOfTheDay()
                    addAsteroidsToDatabase()
                    //dismissDialog()
                    Log.i("TAG", "onAvailable: ")
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    // Internet connection is lost
                    //showDialog()
                    Log.i("TAG", "onLost: ")
                }
            })
    }
}