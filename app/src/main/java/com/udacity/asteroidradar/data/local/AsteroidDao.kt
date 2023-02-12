package com.udacity.asteroidradar.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.models.Asteroid

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllAsteroids(list: List<Asteroid>)

    @Query(value = "SELECT * FROM asteroid_table ORDER BY close_approach_date ASC")
    fun getAsteroids(): LiveData<List<Asteroid>>

    @Query(value = "SELECT * FROM asteroid_table ORDER BY close_approach_date ASC")
    suspend fun getAllAsteroids(): List<Asteroid>

    @Query(value = "SELECT * FROM asteroid_table WHERE close_approach_date = :today ORDER BY close_approach_date ASC")
    suspend fun getAsteroidsOfToday(today: String): List<Asteroid>

    @Query(value = "SELECT * FROM asteroid_table WHERE close_approach_date BETWEEN :startDate AND :endDate ORDER BY close_approach_date ASC")
    suspend fun getAsteroidsOfTheWeek(startDate: String , endDate:String): List<Asteroid>

    @Query(value = "DELETE FROM asteroid_table")
    suspend fun deleteAllAsteroids()
}