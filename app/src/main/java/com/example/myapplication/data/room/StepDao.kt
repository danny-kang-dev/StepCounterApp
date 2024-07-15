package com.example.myapplication.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stepEntity: StepEntity)

    @Delete
    suspend fun delete(stepEntity: StepEntity)

    @Update
    suspend fun update(studentEntity: StepEntity)

    @Query("SELECT * FROM StepEntity where date = :date")
    fun getStepByDate(date: String): Flow<StepEntity>
}