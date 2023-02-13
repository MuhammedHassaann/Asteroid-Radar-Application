package com.udacity.asteroidradar.repository

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.data.local.AsteroidDatabase
import com.udacity.asteroidradar.data.remote.AsteroidInstance
import com.udacity.asteroidradar.data.remote.NetworkUtils
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.utils.DateFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidMainRepository(context: Context) {
    private val asteroidDao by lazy { AsteroidDatabase.getInstance(context).asteroidDao() }


    //get startDate
    @RequiresApi(Build.VERSION_CODES.O)
    private val today = DateFormatter.getCurrentDate()

    //get tomorrow
    @RequiresApi(Build.VERSION_CODES.O)
    private val tomorrow = DateFormatter.getTomorrowDate()

    //get endDate
    @RequiresApi(Build.VERSION_CODES.O)
    private val endDate = DateFormatter.getDateAfter7Days()

    @RequiresApi(Build.VERSION_CODES.O)
    val allAsteroids: LiveData<List<Asteroid>> = asteroidDao.getAsteroids(today,endDate)

    suspend fun getAllAsteroids(startDate: String, endDate: String): List<Asteroid>{
        return withContext(Dispatchers.IO){
            asteroidDao.getAllAsteroids(startDate = startDate, endDate = endDate)
        }
    }

    suspend fun updateDatabase(list: List<Asteroid>) {
        withContext(Dispatchers.IO) {
            asteroidDao.addAllAsteroids(list)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addAsteroidsToDatabase() {
        withContext(Dispatchers.IO) {
            try {
                val result =
                    AsteroidInstance.api.getAsteroids(today, endDate, BuildConfig.API_KEY)
                val asteroidList =
                    NetworkUtils.parseAsteroidsJsonResult(JSONObject(result.body().toString()))
                updateDatabase(asteroidList)
            } catch (e: Exception) {
                Log.e("Response failed:", e.toString())
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun downloadNext7Days() {
        withContext(Dispatchers.IO) {
            try {
                val result =
                    AsteroidInstance.api.getAsteroids(tomorrow, endDate, BuildConfig.API_KEY)
                val asteroidList =
                    NetworkUtils.parseAsteroidsJsonResult(JSONObject(result.body().toString()))
                updateDatabase(asteroidList)
            } catch (e: Exception) {
                Log.e("Response failed:", e.toString())
            }
        }
    }

    suspend fun getAsteroidsOfToday(today: String): List<Asteroid> {
        return withContext(Dispatchers.IO) {
             asteroidDao.getAsteroidsOfToday(today = today)
        }
    }

    suspend fun getAsteroidsOfTheWeek(startDate: String, endDate: String): List<Asteroid> {
        return withContext(Dispatchers.IO){
            asteroidDao.getAsteroidsOfTheWeek(startDate = startDate, endDate = endDate)
        }
    }

    suspend fun getPicOfTheDay(): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                val result = AsteroidInstance.api.getPictureOfTheDay(BuildConfig.API_KEY)
                val pic = listOf<String>(result.body()?.url.toString(),result.body()?.title.toString())
                pic
            } catch (e: Exception) {
                Log.e("Image Failed: ", e.toString())
                emptyList<String>()
            }
        }
    }
}