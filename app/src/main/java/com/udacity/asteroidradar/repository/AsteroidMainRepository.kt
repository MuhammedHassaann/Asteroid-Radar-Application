package com.udacity.asteroidradar.repository

import android.content.Context
import android.util.Log
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

    val allAsteroids: LiveData<List<Asteroid>> = asteroidDao.getAsteroids()

    //get startDate
    private val startDate = DateFormatter.getCurrentDate()

    //get endDate
    private val endDate = DateFormatter.getDateAfter7Days()

    suspend fun getAllAsteroids(): List<Asteroid>{
        return withContext(Dispatchers.IO){
            asteroidDao.getAllAsteroids()
        }
    }

    suspend fun updateDatabase(list: List<Asteroid>) {
        withContext(Dispatchers.IO) {
            asteroidDao.addAllAsteroids(list)
        }
    }

    suspend fun addAsteroidsToDatabase() {
        withContext(Dispatchers.IO) {
            try {
                val result =
                    AsteroidInstance.api.getAsteroids(startDate, endDate, BuildConfig.API_KEY)
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