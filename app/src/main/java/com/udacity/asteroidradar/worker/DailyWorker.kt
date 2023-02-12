package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.repository.AsteroidMainRepository
import retrofit2.HttpException

class DailyWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: AsteroidMainRepository
): CoroutineWorker(context,params) {

    companion object{
        val WORK_NAME="DailyWorker"
    }
    override suspend fun doWork(): Result =
        try {
            repository.addAsteroidsToDatabase()
            Result.success()
        }catch (e: HttpException){
            Result.retry()
        }
}