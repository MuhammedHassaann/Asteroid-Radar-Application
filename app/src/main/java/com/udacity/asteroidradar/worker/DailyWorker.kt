package com.udacity.asteroidradar.worker

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result =
        try {
            repository.downloadNext7Days()
            Result.success()
        }catch (e: HttpException){
            Result.retry()
        }
}